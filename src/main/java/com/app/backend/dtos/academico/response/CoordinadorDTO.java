package com.app.backend.dtos.academico.response;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoordinadorDTO {
    private Integer idCoordinador;
    private Integer idUsuario;
    private Integer idCarrera;
    private String ubicacionOficina;
    private String horarioAtencion;
    private Boolean estaActivo;
    private LocalDate fechaNombramiento;
    private Integer idCoordinadorSga;
}