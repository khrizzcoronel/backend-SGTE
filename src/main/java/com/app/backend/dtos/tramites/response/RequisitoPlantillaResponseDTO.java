package com.app.backend.dtos.tramites.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO de requisito documental usado en configuracion y detalle de plantilla.
public class RequisitoPlantillaResponseDTO {
    private Integer idRequisito;
    private Integer idPlantilla;
    private String nombreRequisito;
    private String descripcionRequisito;
    private Boolean esObligatorio;
    private String tipoDocumento;
    private Integer tamanoMaxMb;
    private String extensionesPermitidas;
    private Integer numeroOrden;
}