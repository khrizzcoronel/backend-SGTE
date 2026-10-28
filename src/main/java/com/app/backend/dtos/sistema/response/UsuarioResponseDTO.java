package com.app.backend.dtos.sistema.response;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String correoPersonal;
    private String correoInstitucional;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String genero;
    private String direccion;
    private Boolean estado;
    private List<Integer> rolesIds;
}