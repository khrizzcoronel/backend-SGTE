package com.app.backend.controllers.sistema;

import com.app.backend.dtos.sistema.response.NotificacionResponseDTO;
import com.app.backend.services.externos.IJwtService;
import com.app.backend.services.sistema.NotificacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final IJwtService jwtService;
    private final HttpServletRequest request;

    @GetMapping("/mis-notificaciones")
    public ResponseEntity<List<NotificacionResponseDTO>> misNotificaciones() {
        Integer idUsuario = extraerIdUsuario();
        return ResponseEntity.ok(notificacionService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/mis-notificaciones/no-leidas")
    public ResponseEntity<List<NotificacionResponseDTO>> misNoLeidas() {
        Integer idUsuario = extraerIdUsuario();
        return ResponseEntity.ok(notificacionService.listarNoLeidasPorUsuario(idUsuario));
    }

    @PatchMapping("/{id}/marcar-leida")
    public ResponseEntity<NotificacionResponseDTO> marcarLeida(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @PatchMapping("/marcar-todas-leidas")
    public ResponseEntity<Map<String, String>> marcarTodasLeidas() {
        Integer idUsuario = extraerIdUsuario();
        List<NotificacionResponseDTO> noLeidas = notificacionService.listarNoLeidasPorUsuario(idUsuario);
        for (NotificacionResponseDTO n : noLeidas) {
            notificacionService.marcarComoLeida(n.getIdNotificacion());
        }
        return ResponseEntity.ok(Map.of("mensaje", "Todas las notificaciones marcadas como leídas"));
    }

    private Integer extraerIdUsuario() {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return jwtService.extraerIdUsuario(auth.substring(7));
        }
        throw new RuntimeException("No se encontró un usuario autenticado");
    }
}
