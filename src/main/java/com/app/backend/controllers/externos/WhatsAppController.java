package com.app.backend.controllers.externos;

import com.app.backend.services.externos.IWhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class WhatsAppController {

    private final IWhatsAppService whatsAppService;

    @GetMapping("/estado")
    public ResponseEntity<Map<String, Boolean>> obtenerEstado() {
        return ResponseEntity.ok(Map.of("conectado", whatsAppService.estaConectado()));
    }

    @PostMapping("/vinculacion")
    public ResponseEntity<Map<String, String>> solicitarCodigo() {
        try {
            String codigo = whatsAppService.solicitarCodigoVinculacion();
            return ResponseEntity.ok(Map.of("codigo", codigo));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
