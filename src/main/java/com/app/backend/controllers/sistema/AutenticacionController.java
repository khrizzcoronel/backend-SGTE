package com.app.backend.controllers.sistema;

import com.app.backend.dtos.sistema.request.AutenticacionRequestDTO;
import com.app.backend.dtos.sistema.response.AutenticacionRespuestaDTO;
import com.app.backend.services.sistema.AutenticacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sistema/autenticacion")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
// Expone endpoints de login y delega la logica de autenticacion al servicio.
public class AutenticacionController {

    private final AutenticacionService autenticacionService;
    
    @PostMapping("/iniciar-sesion")
    public ResponseEntity<AutenticacionRespuestaDTO> iniciarSesion(@RequestBody AutenticacionRequestDTO peticion) {
        // El servicio valida usuario/contrasena y construye el JWT de sesion.
        AutenticacionRespuestaDTO respuesta = autenticacionService.iniciarSesion(peticion);
        return ResponseEntity.ok(respuesta);
    }
}
