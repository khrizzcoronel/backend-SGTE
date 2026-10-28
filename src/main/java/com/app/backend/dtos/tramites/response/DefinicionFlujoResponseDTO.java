package com.app.backend.dtos.tramites.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO liviano para CRUD de definiciones de flujo.
public class DefinicionFlujoResponseDTO {
    private Integer idFlujo;
    private String nombreFlujo;
    private String descripcionFlujo;
    private Boolean estaActivo;
    private Integer version;
    private Integer creadoPorId;
}

