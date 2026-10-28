package com.app.backend.dtos.sistema.request;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionRequestDTO {
    private Integer idTipo;
    private Integer idUsuario;
    private Integer idSolicitud;
    private String titulo;
    private String mensaje;
    private String canal;
    private LocalDateTime programadaPara;
}