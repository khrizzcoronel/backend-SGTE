package com.app.backend.dtos.sistema.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutenticacionRespuestaDTO {
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String correoInstitucional;
    private String correoPersonal;
    private String telefono;
    private Boolean estado;
    private Integer idCarrera;
    private String carrera;
    private Integer idFacultad;
    private String facultad;
    private Integer idRol;
    private String rol;
    private String mensaje;
    private String token;
}