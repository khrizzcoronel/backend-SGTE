package com.app.backend.dtos.academico.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecanoDTO {
    private Integer idDecano;
    private Integer idUsuario;
    private Integer idFacultad;
    private LocalDateTime fechaNombramiento;
    private Boolean estaActivo;
    private Integer idDecanoSga;
}