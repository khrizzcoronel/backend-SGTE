package com.app.backend.dtos.tramites.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Estructura de un requisito documental dentro de la configuracion de plantilla.
public class RequisitoPlantillaRequestDTO {
    private String nombreRequisito;
    private String descripcionRequisito;
    private Boolean esObligatorio;
    private String tipoDocumento;
    private Integer tamanoMaxMb;
    private String extensionesPermitidas;
    private Integer numeroOrden;
}