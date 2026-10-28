# SGTE — Backend principal

API REST del Sistema de Gestión de Trámites Estudiantiles (SGTE). Gestiona cuentas y autenticación, catálogos académicos, plantillas, flujos de aprobación, solicitudes con documentos y notificaciones para estudiantes, coordinadores y decanos.

## Tecnología

Java 25, Spring Boot 3.5, Maven y PostgreSQL.

## Base de datos

La aplicación usa `sgte-db01` en PostgreSQL y valida el esquema al iniciar (`ddl-auto=validate`). Los scripts para crear la base, el esquema y los datos de demostración están en [`database/`](database/README.md). El registro de cuentas utiliza datos académicos consultados por el frontend al backend auxiliar.

## Desarrollo local

1. Crear la base y el esquema siguiendo `database/README.md`.
2. Copiar `src/main/resources/application.example.properties` como `application.properties` y configurar las variables `DB_PASSWORD`, `AZURE_STORAGE_CONNECTION_STRING`, `JWT_SECRET`, `WHATSAPP_API_KEY` y `GROQ_API_KEY`. El archivo local queda excluido de Git.
3. Iniciar la aplicación:

```bash
./mvnw spring-boot:run
```

La API escucha en `http://localhost:8080`. Para enviar correos, configurar las variables de entorno `SMTP_USERNAME`, `SMTP_PASSWORD` y `SMTP_FROM`. El servicio de WhatsApp usa `http://localhost:7075` según `whatsapp.service.url`.

## Rutas principales

- `POST /api/sistema/registro`: registro de cuentas.
- `POST /api/sistema/autenticacion/iniciar-sesion`: inicio de sesión.
- `/api/solicitudes`: creación y seguimiento de trámites.
