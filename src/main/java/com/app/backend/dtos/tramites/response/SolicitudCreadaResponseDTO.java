package com.app.backend.dtos.tramites.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Respuesta breve tras crear una solicitud desde formulario multipart.
public class SolicitudCreadaResponseDTO {
    private String mensaje;
    private Integer idPlantilla;
    private Integer cantidadArchivos;
}