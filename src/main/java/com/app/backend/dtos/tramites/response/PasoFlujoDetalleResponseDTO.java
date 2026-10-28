package com.app.backend.dtos.tramites.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Detalle de paso dentro de un flujo, incluyendo etapa y asignacion.
public class PasoFlujoDetalleResponseDTO {
    private Integer idPaso;
    private Integer ordenPaso;
    private Integer horasSla;
    private Integer rolRequeridoId;
    private String rolRequerido;
    private Integer idUsuarioEncargado;
    private String usuarioEncargado;
    private EtapaProcesamientoResponseDTO etapa;
}