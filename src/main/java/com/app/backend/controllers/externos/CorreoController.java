package com.app.backend.controllers.externos;

import com.app.backend.dtos.externos.request.CorreoRequestDTO;
import com.app.backend.services.externos.ICorreoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/externos/correo")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CorreoController {

    private final ICorreoService correoService;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarCorreo(@RequestBody CorreoRequestDTO request) {
        correoService.sendEmailAsync(request.getDestinatario(), request.getAsunto(), request.getMensaje());
        return ResponseEntity.ok("Solicitud de envío de correo encolada/enviada correctamente a: " + request.getDestinatario());
    }
}
