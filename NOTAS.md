# Taller 5 – Descomposición de rica-api en microservicios

### 1. Distribución de componentes

| Paquete o clase actual | Destino |
|---|---|
| `investigadores.*` (dominio, aplicación e infraestructura) | `investigadores-service` |
| `publicaciones.*` | `publicaciones-service` |
| `compartido.GlobalExceptionHandler` y `RecursoNoEncontradoException` | Se duplican en ambos servicios |
| `plataforma.StatusController` y `CorsConfig` | Se duplican y adaptan en ambos servicios |
| `plataforma.ArranqueInformativo` y `SaludoInstitucionalService` | Se descartan por ser componentes demostrativos sin valor de negocio |

### 2. Justificación de la separación

La descomosición del monolito modular permite separar facilmente el dominio de investigadores y publicaciones, con el fin de que sean servicios independientes, de esta manera se pueden ejecutar y desplegar de manera independiente, sin embargo, esto implica que cada servicio debe gestionar sus propias respuestas HTTP y configuración.

### 3. Dependencia entre servicios

Antes de la separación, `PublicacionService` consultaba directamente `InvestigadorRepository` para comprobar la existencia de un investigador. Esta dependencia en memoria no era adecuada para dos servicios independientes, para resolverla, se creó el puerto `VerificadorInvestigador`, definido por el servicio de publicaciones.

### 4. Resultado de la descomposición

La separación establece dos límites de servicio:

- `investigadores-service`: administra la información de los investigadores.
- `publicaciones-service`: administra las publicaciones y sus reglas de negocio.

# Taller 6 – Comunicación entre microservicios

### 1. Comunicación mediante gRPC
Se conectaron `publicaciones-service` e `investigadores-service` mediante gRPC. Así, publicaciones puede consultar si un investigador existe usando su correo institucional, sin depender directamente de cómo se realiza la comunicación.

### 2. Circuit Breaker
Se agregó Resilience4j para evitar que los errores de un servicio afecten al otro. Si investigadores deja de responder, publicaciones puede devolver un error 503 en lugar de indicar que el investigador no existe.

### 3. Eureka
Se creó un servidor Eureka para registrar los microservicios y permitir que se encuentren entre sí. Se registraron investigadores, publicaciones y el Gateway.

### 4. API Gateway
Se creó un Gateway en el puerto 8000 que recibe las peticiones y las dirige al servicio correspondiente. Así, los usuarios no necesitan conocer los puertos internos de cada microservicio.

### 5. Kafka y Transactional Outbox
Se agregó Kafka para enviar eventos cuando se registra un investigador. Para evitar perder eventos, se guardan primero en una tabla llamada `outbox_event` y luego se publican en Kafka. El servicio de publicaciones recibe estos eventos sin necesidad de consultar directamente al servicio de investigadores.

### 6. Saga
Se implementó la eliminación de investigadores de forma coordinada. Primero, el investigador pasa al estado `EN_ELIMINACION`; después, publicaciones marca sus registros como huérfanos y envía una confirmación. Finalmente, investigadores elimina el registro.

# Taller 8 - Taller CQRS

Se implementó CQRS para separar el lado de la escritura de la lectura mediante eventos por Kafka.

## Endpoints implementados

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/consultas/investigadores | Listar fichas |
| GET | /api/consultas/investigadores/{correo} | Consultar ficha |
| GET | /api/consultas/ranking | Ranking por publicaciones |
| GET | /api/status | Estado del servicio |

# Taller 9 - Taller EDA

Se implementó la tolerancia a fallos en los consumidores de Kafka mediante reintentos, , Dead Letter Topic (DLT) e idempotencia, para garantizar un procesamiento más confiable de los eventos.

## Discusión
1. ¿Quién debería enterarse cuando llega un mensaje a la DLT y cómo?

El equipo de desarrollo y el responsable de operaciones deberían recibir una alerta automática cuando llegue un mensaje a la DLT, también sería útil contar con un tablero que permita consultar la cantidad de mensajes fallidos y sus errores.

2. ¿Cómo se reprocesaría un mensaje después de corregir el error?

Después de corregir el problema, un operador autorizado puede validar el mensaje y reenviarlo al tópico original mediante una herramienta de reprocesamiento.

3. ¿Cuándo es válido descartar un mensaje de la DLT y quién lo decide?

Se puede descartar cuando se demuestra que el mensaje es inválido, irrelevante o no corresponde a una operación legítima. La decisión debe tomarla el desarrollador responsable del servicio o el equipo encargado, después de revisar el error y registrar lo ocurrido.

4. ¿Qué pasa si el mensaje de la DLT es válido pero llegó a destiempo?

El mensaje debe analizarse antes de reprocesarlo, porque podría modificar información que ya cambió o fue eliminada. Se deben verificar su fecha, orden y estado actual para evitar inconsistencias.

5. ¿Por qué reintentar un mensaje venenoso sin fin es peor que perderlo y peor que enviarlo a una DLT?

Reintentar indefinidamente bloquea el procesamiento de otros mensajes de su partición y consume recursos sin solucionar el problema. En cambio, enviarlo a una DLT permite conservarlo, identificar la causa del error y continuar procesando los demás eventos, sin afectar el funcionamiento.

# Taller 10 - Observabilidad

## Discusión 
1. ¿Qué pasaría si publicaciones-service no pudiera marcar las publicaciones por un error permanente?

El investigador permanecería en estado EN_ELIMINACION, porque la confirmación necesaria para completar la eliminación nunca llegaría. El equipo de desarrollo podría detectar el problema mediante los logs, el aumento del lag de Kafka y una métrica que mida cuánto tiempo llevan las Sagas en ese estado.

2. ¿Cómo diseñar una compensación?

Se podría publicar un evento llamado investigadores.eliminacion-cancelada, consumido por investigadores-service. Este servicio restauraría al investigador a un estado activo, siempre que la operación no se hubiera completado previamente. La compensación debe ser idempotente para evitar cambios incorrectos si el evento se recibe más de una vez.

3. ¿Qué métrica y alerta permitirían detectar Sagas atascadas?

Se podría crear una métrica que contabilice los investigadores que permanecen en estado EN_ELIMINACION durante más de diez minutos. Una alerta notificaría al equipo responsable cuando exista al menos una Saga que supere ese tiempo, permitiendo investigar el servicio o consumidor que no completó su paso.

4. ¿Conviene una Saga coreografiada u orquestada si se agregan cuatro pasos más?

En ese escenario sería conveniente considerar una Saga orquestada, porque al aumentar el número de pasos resulta más difícil comprender y controlar el flujo mediante eventos distribuidos. Un orquestador permitiría centralizar el seguimiento, las decisiones y las compensaciones, facilitando el mantenimiento y la observabilidad.

La observabilidad permite reconstruir el recorrido de una operación distribuida mediante identificadores de correlación, logs y métricas. Además, el seguimiento del lag de Kafka y de los estados de las Sagas permite detectar fallos y operaciones pendientes antes de que se conviertan en problemas mayores.