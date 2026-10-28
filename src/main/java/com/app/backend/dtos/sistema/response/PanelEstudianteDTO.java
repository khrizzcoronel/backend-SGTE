package com.app.backend.dtos.sistema.response;

import com.app.backend.dtos.tramites.response.SolicitudResponseDTO;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PanelEstudianteDTO {
    private String nombreEstudiante;
    private Integer solicitudesActivas;
    private Integer horasAcumuladas;
    private List<SolicitudResponseDTO> solicitudesRecientes;
    private List<NotificacionResponseDTO> notificaciones;
}