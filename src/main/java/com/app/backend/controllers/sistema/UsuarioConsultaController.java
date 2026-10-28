package com.app.backend.controllers.sistema;

import com.app.backend.dtos.sistema.response.UsuarioFiltroResponseDTO;
import com.app.backend.services.sistema.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sistema/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioConsultaController {

    private final UsuarioService usuarioService;

    @GetMapping("/filtro")
    public ResponseEntity<List<UsuarioFiltroResponseDTO>> listarUsuariosFiltrados(
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) Integer idFacultad,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) String nombres,
            @RequestParam(required = false) String apellidos) {
        return ResponseEntity.ok(usuarioService.listarFiltrados(idUsuario, idFacultad, rol, nombres, apellidos));
    }
}