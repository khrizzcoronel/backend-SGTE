package com.app.backend.dtos.tramites.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
// Vista de catalogo de plantilla con resumen y requisitos visibles.
public class PlantillaResponseDTO {
    private Integer idplantilla;
    private String nombreplantilla;
    private String descripcionplantilla;
    private String nombrecategoria;
    private Integer diasresolucionestimados;
    private Boolean estaactivo;
    private Boolean disponiblesexternos;
    private Long pasos;
    private List<RequisitoPlantillaResponseDTO> requisitos;
}