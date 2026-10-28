package com.app.backend.dtos.tramites.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Reemplazo completo de la lista de requisitos de una plantilla.
public class ActualizarRequisitosPlantillaRequestDTO {
    private List<RequisitoPlantillaRequestDTO> requisitos;
}