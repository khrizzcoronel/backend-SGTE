package com.app.backend.dtos.tramites.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
// Detalle expandido de plantilla para vistas de configuracion y consulta.
public class DetallesPlantillaResponseDTO {
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
    private List<PasoFlujoPlantillaResponseDTO> pasosPlantilla;
    private List<RequisitoPlantillaResponseDTO> requisitosPlantilla;
    private Integer idVentana;
    private LocalDate fechaApertura;
    private LocalDate fechaCierre;
    private Boolean permiteExtension;
    private Integer diasResolucionEstimados;
    private Boolean estaActivo;
    private Boolean disponibleExternos;
}