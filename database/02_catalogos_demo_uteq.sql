-- Ejecutar en pgAdmin conectado a "sgte-db01", despues de 01_crear_esquema.sql.
-- Catalogos necesarios para registrar los perfiles DEMO000001..DEMO000045
-- del backend auxiliar. No crea usuarios ni credenciales.
BEGIN;

DO $catalogos$
DECLARE
    v_universidad_id integer;
    v_facultad_id integer;
    v_total integer;
    v_carrera record;
    v_periodo record;
    v_rol record;
    v_tipo record;
BEGIN
    -- Universidad no tiene clave natural UNIQUE en la entidad: controlar duplicados.
    SELECT count(*) INTO v_total
    FROM academico.universidades
    WHERE nombre_universidad = 'Universidad Técnica Estatal de Quevedo';
    IF v_total > 1 THEN
        RAISE EXCEPTION 'Hay mas de una universidad UTEQ con el mismo nombre';
    END IF;
    IF v_total = 0 THEN
        INSERT INTO academico.universidades (nombre_universidad, es_publica)
        VALUES ('Universidad Técnica Estatal de Quevedo', true);
    END IF;
    SELECT id_universidad INTO STRICT v_universidad_id
    FROM academico.universidades
    WHERE nombre_universidad = 'Universidad Técnica Estatal de Quevedo'
      AND es_publica = true;

    INSERT INTO academico.facultades
        (nombre_facultad, codigo_facultad, ubicacion_oficina, email_facultad, id_universidad)
    VALUES
        ('Facultad de Ciencias de la Computación', 'DEMO-FCC',
         'Oficina ficticia - Quevedo', 'facultad.demo@example.test', v_universidad_id)
    ON CONFLICT (nombre_facultad) DO NOTHING;

    SELECT id_facultad INTO STRICT v_facultad_id
    FROM academico.facultades
    WHERE nombre_facultad = 'Facultad de Ciencias de la Computación'
      AND id_universidad = v_universidad_id;

    FOR v_carrera IN
        SELECT * FROM (VALUES
            ('DEMO-SOF', 'Software', 10, 'Presencial'),
            ('DEMO-TEL', 'Telemática', 8, 'Presencial'),
            ('DEMO-TI', 'Tecnologías de la Información', 9, 'En Línea'),
            ('DEMO-SI', 'Sistemas de Información', 9, 'En Línea')
        ) AS c(codigo, nombre, semestres, modalidad)
    LOOP
        INSERT INTO academico.carreras
            (nombre_carrera, codigo_carrera, id_facultad, duracion_semestres, modalidad)
        VALUES
            (v_carrera.nombre, v_carrera.codigo, v_facultad_id,
             v_carrera.semestres, v_carrera.modalidad)
        ON CONFLICT (codigo_carrera) DO NOTHING;

        PERFORM 1 FROM academico.carreras
        WHERE codigo_carrera = v_carrera.codigo
          AND nombre_carrera = v_carrera.nombre
          AND id_facultad = v_facultad_id
          AND duracion_semestres = v_carrera.semestres
          AND modalidad = v_carrera.modalidad;
        IF NOT FOUND THEN
            RAISE EXCEPTION 'Carrera DEMO incompatible: %', v_carrera.codigo;
        END IF;
    END LOOP;

    FOR v_periodo IN
        SELECT * FROM (VALUES
            ('DEMO-2025B', 'Período ficticio 2025 B', DATE '2025-09-01', DATE '2026-02-28', false, false),
            ('DEMO-2026B', 'Período ficticio 2026 B', DATE '2026-09-01', DATE '2027-02-28', true, true)
        ) AS p(codigo, nombre, inicio, fin, activo, actual)
    LOOP
        INSERT INTO academico.periodos
            (codigo_periodo, nombre_periodo, fecha_inicio, fecha_fin,
             esta_activo, es_periodo_actual)
        VALUES
            (v_periodo.codigo, v_periodo.nombre, v_periodo.inicio, v_periodo.fin,
             v_periodo.activo, v_periodo.actual)
        ON CONFLICT (codigo_periodo) DO NOTHING;

        PERFORM 1 FROM academico.periodos
        WHERE codigo_periodo = v_periodo.codigo
          AND fecha_inicio = v_periodo.inicio
          AND fecha_fin = v_periodo.fin
          AND esta_activo = v_periodo.activo
          AND es_periodo_actual = v_periodo.actual;
        IF NOT FOUND THEN
            RAISE EXCEPTION 'Periodo DEMO incompatible: %', v_periodo.codigo;
        END IF;
    END LOOP;

    -- El registro y las rutas esperan estos nombres exactamente en minusculas.
    FOR v_rol IN
        SELECT * FROM (VALUES
            ('estudiante', 'Estudiante de la universidad', 1),
            ('coordinador', 'Coordinador de carrera', 2),
            ('decano', 'Decano de facultad', 3)
        ) AS r(nombre, descripcion, jerarquia)
    LOOP
        INSERT INTO sistema.roles (nombre_rol, descripcion_rol, nivel_jerarquico)
        VALUES (v_rol.nombre, v_rol.descripcion, v_rol.jerarquia)
        ON CONFLICT (nombre_rol) DO NOTHING;
    END LOOP;

    -- Codigos leidos directamente por NotificacionTramiteServiceImpl.
    FOR v_tipo IN
        SELECT * FROM (VALUES
            ('SOL_CREADA', 'Solicitud creada'),
            ('SOL_EN_REVISION', 'Solicitud en revisión'),
            ('SOL_APROBADA', 'Solicitud aprobada'),
            ('SOL_RECHAZADA', 'Solicitud rechazada'),
            ('CAMBIO_ESTADO', 'Cambio de estado')
        ) AS t(codigo, nombre)
    LOOP
        INSERT INTO sistema.tipos_notificacion
            (codigo_tipo, nombre_tipo, esta_activo)
        VALUES (v_tipo.codigo, v_tipo.nombre, true)
        ON CONFLICT (codigo_tipo) DO NOTHING;

        PERFORM 1 FROM sistema.tipos_notificacion
        WHERE codigo_tipo = v_tipo.codigo AND esta_activo = true;
        IF NOT FOUND THEN
            RAISE EXCEPTION 'Tipo de notificacion DEMO inactivo: %', v_tipo.codigo;
        END IF;
    END LOOP;

    SELECT count(*) INTO v_total FROM academico.carreras
    WHERE codigo_carrera IN ('DEMO-SOF','DEMO-TEL','DEMO-TI','DEMO-SI')
      AND id_facultad = v_facultad_id;
    IF v_total <> 4 THEN RAISE EXCEPTION 'Se esperaban 4 carreras DEMO; hay %', v_total; END IF;

    SELECT count(*) INTO v_total FROM sistema.roles
    WHERE nombre_rol IN ('estudiante','coordinador','decano');
    IF v_total <> 3 THEN RAISE EXCEPTION 'Se esperaban 3 roles; hay %', v_total; END IF;

    SELECT count(*) INTO v_total FROM sistema.tipos_notificacion
    WHERE codigo_tipo IN ('SOL_CREADA','SOL_EN_REVISION','SOL_APROBADA',
                          'SOL_RECHAZADA','CAMBIO_ESTADO');
    IF v_total <> 5 THEN RAISE EXCEPTION 'Se esperaban 5 tipos de notificacion; hay %', v_total; END IF;

    RAISE NOTICE 'Catalogos listos: UTEQ, facultad, 4 carreras, 2 periodos, 3 roles y 5 notificaciones';
END
$catalogos$;

COMMIT;

SELECT c.codigo_carrera, c.nombre_carrera, c.id_carrera,
       f.nombre_facultad, u.nombre_universidad
FROM academico.carreras c
JOIN academico.facultades f ON f.id_facultad = c.id_facultad
JOIN academico.universidades u ON u.id_universidad = f.id_universidad
WHERE c.codigo_carrera IN ('DEMO-SOF','DEMO-TEL','DEMO-TI','DEMO-SI')
ORDER BY c.codigo_carrera;
