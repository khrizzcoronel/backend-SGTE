package com.app.backend.dtos.sistema.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponseDTO {
    private Integer idNotificacion;
    private Integer idTipo;
    private Integer idUsuario;
    private Integer idSolicitud;
    private String titulo;
    private String mensaje;
    private String canal;
    private Boolean estaLeida;
    private LocalDateTime fechaLectura;
    private Boolean estaEnviada;
    private LocalDateTime fechaEnvio;
    private String errorEnvio;
    private LocalDateTime programadaPara;
    private LocalDateTime fechaCreacion;
}