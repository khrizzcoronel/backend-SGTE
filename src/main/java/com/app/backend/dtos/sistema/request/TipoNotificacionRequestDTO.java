package com.app.backend.dtos.sistema.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoNotificacionRequestDTO {
    private String codigoTipo;
    private String nombreTipo;
    private String plantillaDefecto;
    private Boolean estaActivo;
}