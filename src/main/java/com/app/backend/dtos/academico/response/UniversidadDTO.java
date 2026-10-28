package com.app.backend.dtos.academico.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversidadDTO {
    private Integer idUniversidad;
    private String nombreUniversidad;
    private Boolean esPublica;
}