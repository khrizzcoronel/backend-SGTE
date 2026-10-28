package com.app.backend.dtos.academico.response;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemestreDTO {
    private Integer idSemestre;
    private String codigoPeriodo;
    private String nombrePeriodo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean estaActivo;
}