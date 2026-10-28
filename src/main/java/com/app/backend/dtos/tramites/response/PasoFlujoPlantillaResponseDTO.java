package com.app.backend.dtos.tramites.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Vista de paso embebida en el detalle completo de una plantilla.
public class PasoFlujoPlantillaResponseDTO {
    private Integer idPaso;
    private Integer idFlujo;
    private Integer ordenPaso;
    private Integer idEtapa;
    private String codigoEtapa;
    private String nombreEtapa;
    private String descripcionEtapa;
    private Integer idRolRequerido;
    private String rolRequerido;
    private Integer idUsuarioEncargado;
    private String usuarioEncargado;
    private Integer horasSla;
}