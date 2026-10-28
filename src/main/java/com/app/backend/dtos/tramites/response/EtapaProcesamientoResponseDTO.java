package com.app.backend.dtos.tramites.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Representa una etapa del flujo en respuestas de configuracion.
public class EtapaProcesamientoResponseDTO {
    private Integer idEtapa;
    private String nombreEtapa;
    private String descripcionEtapa;
    private String codigoEtapa;
}

