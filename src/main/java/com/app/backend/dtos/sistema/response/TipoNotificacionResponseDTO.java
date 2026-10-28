package com.app.backend.dtos.sistema.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoNotificacionResponseDTO {
    private Integer idTipo;
    private String codigoTipo;
    private String nombreTipo;
    private String plantillaDefecto;
    private Boolean estaActivo;
}