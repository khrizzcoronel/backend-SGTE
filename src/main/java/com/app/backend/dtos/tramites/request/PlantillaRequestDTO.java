package com.app.backend.dtos.tramites.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Datos de alta de una plantilla de tramite y sus requisitos iniciales.
public class PlantillaRequestDTO {
    private String nombrePlantilla;
    private String descripcionPlantilla;
    private Integer idCategoria;
    private Integer idCarrera;
    private Integer idFlujo;
    private Integer diasEstimados;
    private Boolean estaActivo;
    private Boolean disponibleExternos;
    private List<RequisitoPlantillaRequestDTO> requisitos;
}