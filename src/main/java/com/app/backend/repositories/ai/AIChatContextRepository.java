package com.app.backend.repositories.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AIChatContextRepository {

    private final DataSource dataSource;

    @SuppressWarnings("null")
    private JdbcTemplate jdbc() {
        return new JdbcTemplate(dataSource);
    }

    /** Contexto del coordinador: solicitudes de su carrera en el período activo */
    public String getCoordinadorContext(Integer idCarrera) {
        try {
            String result = jdbc().queryForObject(
                    "SELECT sgte.fn_get_coordinador_chat_context(?::integer)::text",
                    String.class, idCarrera);
            return result != null ? result : "{}";
        } catch (Exception e) {
            log.warn("[AIChatContextRepository] fn_get_coordinador_chat_context no disponible (idCarrera={}): {}",
                    idCarrera, e.getMessage());
            return "{}";
        }
    }

    /** Contexto del decano: solicitudes de todas las carreras de su facultad */
    public String getDecanoContext(Integer idFacultad) {
        try {
            String result = jdbc().queryForObject(
                    "SELECT sgte.fn_get_decano_chat_context(?::integer)::text",
                    String.class, idFacultad);
            return result != null ? result : "{}";
        } catch (Exception e) {
            log.warn("[AIChatContextRepository] fn_get_decano_chat_context no disponible (idFacultad={}): {}",
                    idFacultad, e.getMessage());
            return "{}";
        }
    }

    /** Contexto del estudiante: sus solicitudes activas e historial */
    public String getEstudianteContext(Integer userId) {
        try {
            String result = jdbc().queryForObject(
                    "SELECT sgte.fn_get_estudiante_chat_context(?::integer)::text",
                    String.class, userId);
            return result != null ? result : "{}";
        } catch (Exception e) {
            log.warn("[AIChatContextRepository] fn_get_estudiante_chat_context no disponible (userId={}): {}",
                    userId, e.getMessage());
            return "{}";
        }
    }
}