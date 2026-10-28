package com.app.backend.dtos.tramites.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Proyeccion base de plantilla usada como insumo para armar el detalle extendido.
public class DetallesPlantillaBaseResponseDTO {
    private Integer idPlantilla;
    private String nombrePlantilla;
    private String descripcionPlantilla;
    private Integer idCategoria;
    private String categoria;
    private Integer idCarrera;
    private String carrera;
    private Integer idFacultad;
    private String facultad;
    private Integer idFlujo;
    private String nombreFlujo;
    private String descripcion;
    private Integer idUsuarioCreador;
    private String usuarioCreador;
    private Integer version;
    private Integer idVentana;
    private LocalDate fechaApertura;
    private LocalDate fechaCierre;
    private Boolean permiteExtension;
    private Integer diasResolucionEstimados;
    private Boolean estaActivo;
    private Boolean disponibleExternos;
}