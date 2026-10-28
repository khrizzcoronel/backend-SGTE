package com.app.backend.dtos.tramites.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Definicion minima de etapa para crearla en linea desde gestion de flujo.
public class EtapaRequestDTO {
    private String nombreEtapa;
    private String descripcionEtapa;
    private String codigoEtapa;
}