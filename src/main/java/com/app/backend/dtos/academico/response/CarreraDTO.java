package com.app.backend.dtos.academico.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarreraDTO {
    private Integer idCarrera;
    private String nombreCarrera;
    private String codigoCarrera;
    private Integer idFacultad;
}