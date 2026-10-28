package com.app.backend.controllers.sistema;

import com.app.backend.dtos.sistema.request.ConfiguracionUsuarioRequestDTO;
import com.app.backend.dtos.sistema.response.ConfiguracionUsuarioResponseDTO;
import com.app.backend.services.externos.IJwtService;
import com.app.backend.services.sistema.ConfiguracionUsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/configuracion-usuario")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ConfiguracionUsuarioController {

    private final ConfiguracionUsuarioService configuracionService;
    private final IJwtService jwtService;
    private final HttpServletRequest request;

    @GetMapping("/mis-preferencias")
    public ResponseEntity<ConfiguracionUsuarioResponseDTO> obtenerMisPreferencias() {
        Integer idUsuario = extraerIdUsuario();
        try {
            return ResponseEntity.ok(configuracionService.obtenerPorUsuario(idUsuario));
        } catch (Exception e) {
            // Si no existe configuración, devolver valores por defecto
            return ResponseEntity.ok(ConfiguracionUsuarioResponseDTO.builder()
                    .idUsuario(idUsuario)
                    .notificarEmail(true)
                    .notificarWhatsapp(false)
                    .notificarSms(false)
                    .notificarPush(false)
                    .idioma("es")
                    .zonaHoraria("America/Guayaquil")
                    .build());
        }
    }

    @PutMapping("/canales")
    public ResponseEntity<Map<String, String>> guardarCanales(@RequestBody Map<String, Boolean> canales) {
        Integer idUsuario = extraerIdUsuario();

        ConfiguracionUsuarioRequestDTO dto = ConfiguracionUsuarioRequestDTO.builder()
                .idUsuario(idUsuario)
                .notificarEmail(canales.getOrDefault("correo", true))
                .notificarWhatsapp(canales.getOrDefault("whatsapp", false))
                .notificarSms(false)
                .notificarPush(false)
                .build();

        // Preservar campos existentes que no estamos modificando
        try {
            ConfiguracionUsuarioResponseDTO existente = configuracionService.obtenerPorUsuario(idUsuario);
            dto.setRutaFotoPerfil(existente.getRutaFotoPerfil());
            dto.setRutaFirmaEscaneada(existente.getRutaFirmaEscaneada());
            dto.setIdioma(existente.getIdioma());
            dto.setZonaHoraria(existente.getZonaHoraria());
        } catch (Exception ignored) {
            // No hay configuración previa, se crea nueva
        }

        configuracionService.guardar(dto);
        return ResponseEntity.ok(Map.of("mensaje", "Preferencias de canales guardadas exitosamente"));
    }

    private Integer extraerIdUsuario() {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return jwtService.extraerIdUsuario(auth.substring(7));
        }
        throw new RuntimeException("No se encontró un usuario autenticado");
    }
}
