package com.app.backend.controllers.tramites;

import com.app.backend.dtos.tramites.request.AsignarPasoUsuarioRequestDTO;
import com.app.backend.dtos.tramites.request.FlujoTrabajoCompletoRequestDTO;
import com.app.backend.dtos.tramites.response.FlujoTrabajoDetalleResponseDTO;
import com.app.backend.dtos.tramites.response.PasoFlujoDetalleResponseDTO;
import com.app.backend.services.tramites.GestionFlujoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tramites/flujos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
// API para definir y consultar flujos de trabajo de tramites.
public class FlujoTrabajoController {

    private final GestionFlujoService gestionFlujoService;

    @GetMapping
    public ResponseEntity<List<FlujoTrabajoDetalleResponseDTO>> listarFlujos() {
        return ResponseEntity.ok(gestionFlujoService.listarFlujosCompletos());
    }

    @GetMapping("/{idFlujo}")
    public ResponseEntity<FlujoTrabajoDetalleResponseDTO> obtenerFlujo(@PathVariable Integer idFlujo) {
        return ResponseEntity.ok(gestionFlujoService.obtenerFlujoCompleto(idFlujo));
    }

    @PostMapping
    public ResponseEntity<FlujoTrabajoDetalleResponseDTO> crearFlujo(@RequestBody FlujoTrabajoCompletoRequestDTO request) {
        // Crea flujo + pasos en una sola operacion.
        return ResponseEntity.ok(gestionFlujoService.crearFlujoCompleto(request));
    }

    @PutMapping("/pasos/{idPaso}/asignar-usuario/{idUsuario}")
    public ResponseEntity<PasoFlujoDetalleResponseDTO> asignarUsuarioPaso(
            @PathVariable Integer idPaso,
            @PathVariable Integer idUsuario) {
        // Vincula un usuario gestor al paso indicado del flujo.
        AsignarPasoUsuarioRequestDTO request = AsignarPasoUsuarioRequestDTO.builder()
                .idUsuarioEncargado(idUsuario)
                .build();
        return ResponseEntity.ok(gestionFlujoService.asignarUsuarioPaso(idPaso, request));
    }
}