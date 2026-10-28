package com.app.backend.dtos.sistema.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredencialRequestDTO {
    private Integer idUsuario;
    private String hashContrasena;
    private Boolean estado;
}