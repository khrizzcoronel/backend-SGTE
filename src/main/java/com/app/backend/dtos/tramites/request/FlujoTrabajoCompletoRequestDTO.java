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
// Define un flujo completo con su cabecera y coleccion de pasos.
public class FlujoTrabajoCompletoRequestDTO {
    private String nombreFlujo;
    private String descripcionFlujo;
    private Boolean estaActivo;
    private Integer version;
    private Integer creadoPorId;
    private List<PasoFlujoCompletoRequestDTO> pasos;
}