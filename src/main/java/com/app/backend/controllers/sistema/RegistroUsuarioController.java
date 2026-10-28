package com.app.backend.controllers.sistema;

import com.app.backend.dtos.sistema.request.RegistroUsuarioDTO;
import com.app.backend.dtos.sistema.response.RegistroUsuarioRespuestaDTO;
import com.app.backend.services.sistema.RegistroUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sistema/registro")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroUsuarioController {

    private final RegistroUsuarioService registroUsuarioService;

    @PostMapping
    public ResponseEntity<RegistroUsuarioRespuestaDTO> registrarUsuario(@RequestBody RegistroUsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroUsuarioService.registrarUsuario(dto));
    }
}
