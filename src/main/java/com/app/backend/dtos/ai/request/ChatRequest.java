package com.app.backend.dtos.ai.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    /** Módulo que origina el chat: "coordinador", "estudiante", "decano" */
    private String module;

    /** Mensaje del usuario */
    private String message;

    /** ID del usuario autenticado (para obtener contexto de BD) */
    private Integer userId;

    /** ID de carrera del coordinador/estudiante (para filtrar contexto) */
    private Integer idCarrera;

    /** ID de facultad del decano (para filtrar contexto) */
    private Integer idFacultad;
}