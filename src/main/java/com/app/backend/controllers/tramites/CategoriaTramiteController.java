package com.app.backend.controllers.tramites;

import com.app.backend.dtos.tramites.response.CategoriaPlantillaResponseDTO;
import com.app.backend.services.tramites.CategoriaTramiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tramites/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
// Catalogo de categorias para clasificar plantillas de tramite.
public class CategoriaTramiteController {

    private final CategoriaTramiteService categoriaTramiteService;

    @GetMapping
    public ResponseEntity<List<CategoriaPlantillaResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaTramiteService.listarTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaPlantillaResponseDTO>> listarCategoriasActivas() {
        // Endpoint comun para formularios que solo deben mostrar opciones habilitadas.
        return ResponseEntity.ok(categoriaTramiteService.listarActivas());
    }
}