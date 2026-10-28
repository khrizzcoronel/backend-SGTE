package com.app.backend.dtos.sistema.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioFiltroResponseDTO {
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private Integer idRol;
    private String rol;
    private Integer idFacultad;
    private String facultad;
    private Integer idCarrera;
    private String carrera;
}