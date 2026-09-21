# ms-rutaexpress-shipments

CRUD de envíos y máquina de estados (CREADO → ACEPTADO → EN_BODEGA → EN_RUTA → ENTREGADO / CANCELADO). Publica eventos a RabbitMQ (notificaciones) y Kafka (auditoría/reportes).

Spring Boot 3.3.5, Java 17+, Maven (`./mvnw`). Puerto local: **8081**. Responsable: Diego / Claude (código inicial: opencode).

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/shipments` | lista |
| GET | `/api/shipments/{id}` | detalle |
| POST | `/api/shipments` | crea envío |
| PATCH | `/api/shipments/{id}/status` | cambia estado |

## Perfiles

- **por defecto (dev)**: H2 en memoria y **sin seguridad** (solo para desarrollo local).
- **`secure`**: valida el JWT de Azure AD (`AZURE_TENANT_ID`) y aplica roles desde el claim `roles`.
- **`prod`**: PostgreSQL.

## Variables de entorno

`AZURE_TENANT_ID`, `AZURE_API_AUDIENCE` (`api://<API_CLIENT_ID>`), RABBITMQ_HOST/PORT/USER/PASS, KAFKA_BOOTSTRAP, DB_HOST/PORT/NAME/USER/PASS (perfil prod)

## Pruebas

`./mvnw test` ejecuta 14 pruebas: servicio de envíos (máquina de estados) y seguridad por perfil `secure`. No necesitan brokers ni base de datos externos (H2 en memoria; los listeners de RabbitMQ/Kafka se desactivan en los tests).

## Ejecutar

```bash
./mvnw test
./mvnw spring-boot:run
SPRING_PROFILES_ACTIVE=secure AZURE_TENANT_ID=<tenant> ./mvnw spring-boot:run
```

Los DTOs compartidos están copiados en `src/main/java/com/rutaexpress/contracts`; la fuente de verdad de los contratos está en el repo `Cloud-Native-1` (`contratos/`).
