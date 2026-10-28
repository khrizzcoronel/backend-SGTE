package com.app.backend.dtos.sistema.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroUsuarioDTO {

    // === Datos del Usuario ===
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
    private Boolean estadoUsuario;
    private String rol;

    // === Datos de Carrera (búsqueda por código) ===
    private Integer idCarrera;
    private String codigoCarrera;
    private String nombreCarrera;
    private String tituloOtorga;
    private Integer duracionSemestres;

    // === Datos de Facultad ===
    private Integer idFacultad;
    private String codigoFacultad;
    private String nombreFacultad;
    private String emailFacultad;
    private String ubicacionOficinaFacultad;

    // === Datos del Semestre / Periodo ===
    private Integer idSemestre;
    private String codigoPeriodo;
    private String nombrePeriodo;
    private LocalDate fechaInicioPeriodo;
    private LocalDate fechaFinPeriodo;
    private Boolean esPeriodoActual;

    // === Datos del Estudiante ===
    private Integer idEstudiante;
    private String paralelo;
    private String estadoAcademico;
    private LocalDateTime fechaIngreso;
    private Boolean matriculado;
    private String numeroMatricula;
    private Double promedioGeneral;
    private Integer creditosAprobados;
    private Integer creditosTotales;
    private Boolean esBecado;
    private String tipoBeca;
    private String jornada;
    private String modalidad;
    private LocalDate fechaEgreso;

    // === Datos de Coordinador (opcionales) ===
    private Integer idCoordinador;
    private String horarioAtencion;
    private String oficinaAtencion;
    private LocalDate fechaNombramientoCoordinador;
    private LocalDate fechaFinPeriodoCoordinador;
    private String resolucionNombramiento;

    // === Datos de Decano (opcionales) ===
    private Integer idDecano;
    private LocalDate fechaNombramientoDecano;
    private LocalDate fechaFinPeriodoDecano;
    private String extensionTelefonica;
}