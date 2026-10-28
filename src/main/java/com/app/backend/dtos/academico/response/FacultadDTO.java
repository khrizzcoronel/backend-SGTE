package com.app.backend.dtos.academico.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacultadDTO {
    private Integer idFacultad;
    private String nombreFacultad;
    private String ubicacionOficina;
    private String emailFacultad;
    private Integer idUniversidad;
}