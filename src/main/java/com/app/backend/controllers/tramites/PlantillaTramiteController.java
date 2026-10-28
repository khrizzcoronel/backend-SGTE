package com.app.backend.controllers.tramites;

import com.app.backend.dtos.tramites.request.PlantillaRequestDTO;
import com.app.backend.dtos.tramites.request.PlantillaEditarRequestDTO;
import com.app.backend.dtos.tramites.request.ActualizarRequisitosPlantillaRequestDTO;
import com.app.backend.dtos.tramites.response.DetallesPlantillaResponseDTO;
import com.app.backend.dtos.tramites.response.PlantillaResponseDTO;
import com.app.backend.dtos.tramites.response.TipoPlantillaResponseDTO;
import com.app.backend.services.tramites.DetallesTramiteService;
import com.app.backend.services.tramites.TipoTramiteService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tramites")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
// Endpoints de administracion de plantillas y sus vistas de detalle.
public class PlantillaTramiteController {

    private final DetallesTramiteService detallesTramiteService;
    private final TipoTramiteService plantillaTramiteService;

    @GetMapping("/detalles/carrera/{idCarrera}")
    public ResponseEntity<List<DetallesPlantillaResponseDTO>> listarPorCarrera(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok(detallesTramiteService.listarPorCarrera(idCarrera));
    }

    @GetMapping("/detalles/facultad/{idFacultad}")
    public ResponseEntity<List<DetallesPlantillaResponseDTO>> listarPorFacultad(@PathVariable Integer idFacultad) {
        return ResponseEntity.ok(detallesTramiteService.listarPorFacultad(idFacultad));
    }

    @GetMapping("/plantillas")
    public ResponseEntity<List<PlantillaResponseDTO>> listarPlantillas(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String busqueda) {
        // Devuelve catalogo filtrable de plantillas para panel de tramites.
        return ResponseEntity.ok(plantillaTramiteService.listarPlantillas(categoria, activo, busqueda));
    }

    @PostMapping("/plantillas/guardar")
    public ResponseEntity<TipoPlantillaResponseDTO> guardarPlantilla(@RequestBody PlantillaRequestDTO dto) {
        return ResponseEntity.ok(plantillaTramiteService.crear(dto));
    }

    @PutMapping("/plantillas/editar/{id}")
    public ResponseEntity<TipoPlantillaResponseDTO> editarPlantilla(@PathVariable Integer id,
            @RequestBody PlantillaEditarRequestDTO dto) {
        return ResponseEntity.ok(plantillaTramiteService.actualizar(id, dto));
    }

    @DeleteMapping("/plantillas/eliminar/{id}")
    public ResponseEntity<Void> eliminarPlantilla(@PathVariable Integer id) {
        // Eliminacion en cascada de plantilla y dependencias de negocio.
        plantillaTramiteService.eliminarCompleto(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/plantillas/{id}/requisitos")
    public ResponseEntity<TipoPlantillaResponseDTO> actualizarRequisitos(
            @PathVariable Integer id,
            @RequestBody ActualizarRequisitosPlantillaRequestDTO dto) {
        return ResponseEntity.ok(plantillaTramiteService.actualizarRequisitos(id, dto));
    }

    @GetMapping("/detalles/plantilla")
    public ResponseEntity<DetallesPlantillaResponseDTO> obtenerPorPlantilla(@RequestParam Integer idPlantilla) {
        return ResponseEntity.ok(detallesTramiteService.obtenerPorPlantilla(idPlantilla));
    }
}