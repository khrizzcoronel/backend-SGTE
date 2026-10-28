package com.app.backend.controllers.tramites;

import com.app.backend.dtos.tramites.response.EtapaProcesamientoResponseDTO;
import com.app.backend.services.tramites.EtapaProcesamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tramites/etapas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
// Catalogo de etapas reutilizables dentro de los flujos de tramite.
public class EtapaProcesamientoController {

    private final EtapaProcesamientoService etapaProcesamientoService;

    @GetMapping
    public ResponseEntity<List<EtapaProcesamientoResponseDTO>> listarEtapas() {
        return ResponseEntity.ok(etapaProcesamientoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtapaProcesamientoResponseDTO> obtenerEtapa(@PathVariable Integer id) {
        return ResponseEntity.ok(etapaProcesamientoService.obtenerPorId(id));
    }
}