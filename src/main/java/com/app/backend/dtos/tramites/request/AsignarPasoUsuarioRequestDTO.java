package com.app.backend.dtos.tramites.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Request puntual para asignar un usuario encargado a un paso del flujo.
public class AsignarPasoUsuarioRequestDTO {
    private Integer idUsuarioEncargado;
}