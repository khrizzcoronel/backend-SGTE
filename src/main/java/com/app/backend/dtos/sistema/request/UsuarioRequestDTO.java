package com.app.backend.dtos.sistema.request;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {
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