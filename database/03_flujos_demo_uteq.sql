-- Ejecutar en "sgte-db01" despues de 02_catalogos_demo_uteq.sql.
-- Requiere que DEMO000041..DEMO000044 (coordinadores) y DEMO000045
-- (decano) se hayan registrado desde el portal usando backend-AUX.
-- Crea una plantilla por carrera, con un paso del coordinador asignado
-- a esa carrera y un paso del decano. No crea solicitudes ni archivos.
BEGIN;

DO $flujos$
DECLARE
    v_facultad_id integer;
    v_periodo_id integer;
    v_rol_coordinador integer;
    v_rol_decano integer;
    v_decano_id integer;
    v_categoria_id integer;
    v_etapa_coordinador integer;
    v_etapa_decano integer;
    v_carrera_id integer;
    v_coordinador_id integer;
    v_flujo_id integer;
    v_plantilla_id integer;
    v_total integer;
    v_carrera record;
    v_nombre_flujo text;
    v_nombre_plantilla constant text := 'Constancia académica (DEMO)';
BEGIN
    SELECT id_facultad INTO STRICT v_facultad_id
    FROM academico.facultades
    WHERE nombre_facultad = 'Facultad de Ciencias de la Computación';

    SELECT id_periodo INTO STRICT v_periodo_id
    FROM academico.periodos
    WHERE codigo_periodo = 'DEMO-2026B' AND es_periodo_actual = true;

    SELECT id_rol INTO STRICT v_rol_coordinador
    FROM sistema.roles WHERE nombre_rol = 'coordinador';
    SELECT id_rol INTO STRICT v_rol_decano
    FROM sistema.roles WHERE nombre_rol = 'decano';

    SELECT u.id_usuario INTO v_decano_id
    FROM sistema.usuarios u
    JOIN academico.decanos d ON d.id_usuario = u.id_usuario
    JOIN sistema.usuarios_roles ur ON ur.id_usuario = u.id_usuario
    WHERE u.cedula = 'DEMO000045'
      AND u.estado = true
      AND d.id_facultad = v_facultad_id
      AND d.esta_activo = true
      AND ur.id_rol = v_rol_decano;
    IF v_decano_id IS NULL THEN
        RAISE EXCEPTION 'Registre primero al decano DEMO000045 desde el portal';
    END IF;

    -- Categoria sin restriccion UNIQUE: localizar por nombre y rechazar duplicados.
    SELECT count(*) INTO v_total FROM tramites.categorias
    WHERE nombre_categoria = 'Trámites académicos DEMO';
    IF v_total > 1 THEN RAISE EXCEPTION 'Categoria DEMO duplicada'; END IF;
    IF v_total = 0 THEN
        INSERT INTO tramites.categorias
            (nombre_categoria, descripcion_categoria, esta_activo)
        VALUES
            ('Trámites académicos DEMO',
             'Plantillas ficticias para comprobar el flujo de solicitudes', true);
    END IF;
    SELECT id_categoria INTO STRICT v_categoria_id
    FROM tramites.categorias
    WHERE nombre_categoria = 'Trámites académicos DEMO' AND esta_activo = true;

    INSERT INTO tramites.etapas (nombre_etapa, descripcion, codigo)
    VALUES
        ('Revisión de coordinación (DEMO)',
         'Primer paso: revisión por el coordinador de la carrera', 'DEMO-REV-COORD'),
        ('Aprobación de decanato (DEMO)',
         'Segundo paso: decisión del decano de la facultad', 'DEMO-APROB-DEC')
    ON CONFLICT (codigo) DO NOTHING;

    SELECT id_etapa INTO STRICT v_etapa_coordinador
    FROM tramites.etapas
    WHERE codigo = 'DEMO-REV-COORD'
      AND nombre_etapa = 'Revisión de coordinación (DEMO)';
    SELECT id_etapa INTO STRICT v_etapa_decano
    FROM tramites.etapas
    WHERE codigo = 'DEMO-APROB-DEC'
      AND nombre_etapa = 'Aprobación de decanato (DEMO)';

    FOR v_carrera IN
        SELECT * FROM (VALUES
            ('DEMO-SOF', 'DEMO000041'),
            ('DEMO-TEL', 'DEMO000042'),
            ('DEMO-TI', 'DEMO000043'),
            ('DEMO-SI', 'DEMO000044')
        ) AS c(codigo, cedula_coordinador)
    LOOP
        SELECT id_carrera INTO STRICT v_carrera_id
        FROM academico.carreras
        WHERE codigo_carrera = v_carrera.codigo AND id_facultad = v_facultad_id;

        v_coordinador_id := NULL;
        SELECT u.id_usuario INTO v_coordinador_id
        FROM sistema.usuarios u
        JOIN academico.coordinadores co ON co.id_usuario = u.id_usuario
        JOIN sistema.usuarios_roles ur ON ur.id_usuario = u.id_usuario
        WHERE u.cedula = v_carrera.cedula_coordinador
          AND u.estado = true
          AND co.id_carrera = v_carrera_id
          AND co.esta_activo = true
          AND ur.id_rol = v_rol_coordinador;
        IF v_coordinador_id IS NULL THEN
            RAISE EXCEPTION 'Registre primero al coordinador % de %',
                v_carrera.cedula_coordinador, v_carrera.codigo;
        END IF;

        v_nombre_flujo := 'DEMO - Constancia académica - ' || v_carrera.codigo;
        SELECT count(*) INTO v_total FROM tramites.flujos_trabajo
        WHERE nombre_flujo = v_nombre_flujo;
        IF v_total > 1 THEN RAISE EXCEPTION 'Flujo DEMO duplicado: %', v_nombre_flujo; END IF;
        IF v_total = 0 THEN
            INSERT INTO tramites.flujos_trabajo
                (nombre_flujo, descripcion, esta_activo, version, creado_por)
            VALUES
                (v_nombre_flujo, 'Flujo ficticio de dos pasos para ' || v_carrera.codigo,
                 true, 1, v_decano_id);
        END IF;
        SELECT id_flujo INTO STRICT v_flujo_id
        FROM tramites.flujos_trabajo
        WHERE nombre_flujo = v_nombre_flujo
          AND esta_activo = true
          AND version = 1
          AND creado_por = v_decano_id;

        INSERT INTO tramites.pasos_flujo
            (id_flujo, id_etapa, orden_paso, rol_requerido,
             id_usuario_encargado, horas_sla)
        VALUES
            (v_flujo_id, v_etapa_coordinador, 1, v_rol_coordinador,
             v_coordinador_id, 48),
            (v_flujo_id, v_etapa_decano, 2, v_rol_decano,
             v_decano_id, 48)
        ON CONFLICT (id_flujo, orden_paso) DO NOTHING;

        PERFORM 1 FROM tramites.pasos_flujo
        WHERE id_flujo = v_flujo_id AND orden_paso = 1
          AND id_etapa = v_etapa_coordinador
          AND rol_requerido = v_rol_coordinador
          AND id_usuario_encargado = v_coordinador_id;
        IF NOT FOUND THEN RAISE EXCEPTION 'Paso 1 incompatible en %', v_nombre_flujo; END IF;

        PERFORM 1 FROM tramites.pasos_flujo
        WHERE id_flujo = v_flujo_id AND orden_paso = 2
          AND id_etapa = v_etapa_decano
          AND rol_requerido = v_rol_decano
          AND id_usuario_encargado = v_decano_id;
        IF NOT FOUND THEN RAISE EXCEPTION 'Paso 2 incompatible en %', v_nombre_flujo; END IF;

        SELECT count(*) INTO v_total FROM tramites.plantillas_tramite
        WHERE nombre_plantilla = v_nombre_plantilla AND id_carrera = v_carrera_id;
        IF v_total > 1 THEN RAISE EXCEPTION 'Plantilla DEMO duplicada en %', v_carrera.codigo; END IF;
        IF v_total = 0 THEN
            INSERT INTO tramites.plantillas_tramite
                (nombre_plantilla, descripcion_plantilla, id_categoria,
                 id_flujo, id_carrera, dias_resolucion_estimados,
                 esta_activo, disponible_externos)
            VALUES
                (v_nombre_plantilla,
                 'Solicitud ficticia de constancia académica para ' || v_carrera.codigo,
                 v_categoria_id, v_flujo_id, v_carrera_id, 5, true, false);
        END IF;
        SELECT id_plantilla INTO STRICT v_plantilla_id
        FROM tramites.plantillas_tramite
        WHERE nombre_plantilla = v_nombre_plantilla
          AND id_carrera = v_carrera_id
          AND id_categoria = v_categoria_id
          AND id_flujo = v_flujo_id
          AND esta_activo = true;

        -- El formulario estudiantil requiere al menos un archivo adjunto.
        SELECT count(*) INTO v_total FROM tramites.requisitos_plantilla
        WHERE id_plantilla = v_plantilla_id AND numero_orden = 1;
        IF v_total > 1 THEN RAISE EXCEPTION 'Requisito DEMO duplicado en %', v_carrera.codigo; END IF;
        IF v_total = 0 THEN
            INSERT INTO tramites.requisitos_plantilla
                (id_plantilla, nombre_requisito, descripcion_requisito,
                 es_obligatorio, tipo_documento, tamano_max_mb,
                 extensiones_permitidas, numero_orden)
            VALUES
                (v_plantilla_id, 'Solicitud firmada en PDF',
                 'Documento ficticio para probar la carga de adjuntos',
                 true, 'PDF', 5, 'pdf', 1);
        END IF;
        PERFORM 1 FROM tramites.requisitos_plantilla
        WHERE id_plantilla = v_plantilla_id AND numero_orden = 1
          AND nombre_requisito = 'Solicitud firmada en PDF'
          AND es_obligatorio = true;
        IF NOT FOUND THEN RAISE EXCEPTION 'Requisito incompatible en %', v_carrera.codigo; END IF;

        SELECT count(*) INTO v_total FROM tramites.ventanas_recepcion
        WHERE id_plantilla = v_plantilla_id AND id_periodo = v_periodo_id;
        IF v_total > 1 THEN RAISE EXCEPTION 'Ventana DEMO duplicada en %', v_carrera.codigo; END IF;
        IF v_total = 0 THEN
            INSERT INTO tramites.ventanas_recepcion
                (id_plantilla, id_periodo, fecha_apertura, fecha_cierre,
                 permite_extension, dias_max_extension)
            VALUES
                (v_plantilla_id, v_periodo_id,
                 DATE '2026-09-01', DATE '2027-02-28', false, 0);
        END IF;
        PERFORM 1 FROM tramites.ventanas_recepcion
        WHERE id_plantilla = v_plantilla_id AND id_periodo = v_periodo_id
          AND fecha_apertura = DATE '2026-09-01'
          AND fecha_cierre = DATE '2027-02-28';
        IF NOT FOUND THEN RAISE EXCEPTION 'Ventana incompatible en %', v_carrera.codigo; END IF;
    END LOOP;

    SELECT count(*) INTO v_total
    FROM tramites.plantillas_tramite p
    JOIN academico.carreras c ON c.id_carrera = p.id_carrera
    WHERE p.nombre_plantilla = v_nombre_plantilla
      AND c.codigo_carrera IN ('DEMO-SOF','DEMO-TEL','DEMO-TI','DEMO-SI');
    IF v_total <> 4 THEN RAISE EXCEPTION 'Se esperaban 4 plantillas DEMO; hay %', v_total; END IF;

    RAISE NOTICE 'Flujos listos: 4 flujos, 8 pasos, 4 plantillas, 4 requisitos y 4 ventanas';
END
$flujos$;

COMMIT;

SELECT c.codigo_carrera, p.nombre_plantilla,
       f.nombre_flujo, count(pf.id_paso) AS pasos
FROM tramites.plantillas_tramite p
JOIN academico.carreras c ON c.id_carrera = p.id_carrera
JOIN tramites.flujos_trabajo f ON f.id_flujo = p.id_flujo
JOIN tramites.pasos_flujo pf ON pf.id_flujo = f.id_flujo
WHERE c.codigo_carrera IN ('DEMO-SOF','DEMO-TEL','DEMO-TI','DEMO-SI')
  AND p.nombre_plantilla = 'Constancia académica (DEMO)'
GROUP BY c.codigo_carrera, p.nombre_plantilla, f.nombre_flujo
ORDER BY c.codigo_carrera;
