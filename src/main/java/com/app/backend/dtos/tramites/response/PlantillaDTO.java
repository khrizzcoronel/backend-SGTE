package com.app.backend.dtos.tramites.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
    // Proyeccion de consulta para listados filtrados de plantillas.
public class PlantillaDTO {
    private Integer idplantilla;
    private String nombreplantilla;
    private String descripcionplantilla;
    private String nombrecategoria;
    private Integer diasresolucionestimados;
    private Boolean estaactivo;
    private Boolean disponiblesexternos;
    private String nombrerequisito;
    private String descripcionrequisito;
    private Boolean esobligatorio;
    private String tipodocumento;
    private String extensionespermitidas;
    private Integer numeroorden;
    private Long pasos;
}