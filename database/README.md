# Base de datos principal para pgAdmin

1. En pgAdmin, conectarse a la base administrativa `postgres` y ejecutar `00_crear_base.sql`.
2. Abrir Query Tool sobre la nueva base `sgte-db01` y ejecutar `01_crear_esquema.sql`.
3. Ejecutar `02_catalogos_demo_uteq.sql` en `sgte-db01`. Crea UTEQ, Facultad de Ciencias de la Computacion, cuatro carreras y dos periodos con los mismos codigos `DEMO` del backend auxiliar, tres roles y cinco tipos de notificacion.
4. Con el backend auxiliar cargado mediante su `02_datos_demo_uteq.sql`, registrar desde el portal los coordinadores `DEMO000041` a `DEMO000044` y el decano `DEMO000045`.
5. Ejecutar `03_flujos_demo_uteq.sql` en `sgte-db01`. Crea una plantilla activa para cada carrera, con revision por su coordinador y aprobacion del decano.
6. Registrar desde el portal los estudiantes `DEMO000001` a `DEMO000040` que se necesiten para probar solicitudes.

El segundo script crea los esquemas `academico`, `sistema`, `tramites` y `documentos`, 27 tablas de entidades y la tabla de union `sistema.usuarios_roles`. Incluye tipos, nulabilidad, claves primarias y foraneas, y restricciones `UNIQUE` segun las anotaciones JPA actuales.

El archivo `01_crear_esquema.sql` prepara una base vacia. Los archivos `02` y `03` insertan datos ficticios de demostracion en una transaccion y se pueden ejecutar de nuevo sin duplicarlos. No crean usuarios ni credenciales; el registro del portal los genera a partir de la validacion contra el backend auxiliar. Las direcciones `example.test` del auxiliar no reciben correo real.

Cada plantilla requiere un PDF y el frontend exige al menos un archivo para enviar una solicitud. Por tanto, la prueba de creacion de solicitudes tambien depende de que la integracion de almacenamiento de archivos este disponible. Los cinco tipos de notificacion coinciden con los codigos que usa `NotificacionTramiteServiceImpl`.

Estos scripts no agregan las funciones SQL `tramites.fn_sl_plantillastramites` ni `sgte.fn_get_*_chat_context`, que algunas consultas del backend invocan. La carga de datos no sustituye esas funciones. El listado de detalle por carrera y el flujo basico de solicitudes usan consultas JPA distintas.

Los valores `@Builder.Default` se asignan en Java, no en la base de datos. Por eso el SQL no agrega `DEFAULT` que las entidades JPA no declaran. La aplicacion se conecta a `sgte-db01` mediante `src/main/resources/application.properties`.
