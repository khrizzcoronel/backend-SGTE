package com.app.backend.dtos.sistema.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredencialResponseDTO {
    private Integer idCredencial;
    private Integer idUsuario;
    private Boolean estado;
}