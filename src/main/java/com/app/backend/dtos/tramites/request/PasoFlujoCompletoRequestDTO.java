package com.app.backend.dtos.tramites.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Paso enviado dentro del alta de flujo, con rol y responsable opcionales.
public class PasoFlujoCompletoRequestDTO {
    private Integer idEtapa;
    private EtapaRequestDTO etapa;
    private Integer ordenPaso;
    private Integer rolRequeridoId;
    private Integer idUsuarioEncargado;
    private Integer horasSla;
}