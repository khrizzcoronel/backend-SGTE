package com.app.backend.services.sistema.impl;

import com.app.backend.dtos.sistema.request.ConfiguracionUsuarioRequestDTO;
import com.app.backend.dtos.sistema.response.ConfiguracionUsuarioResponseDTO;
import com.app.backend.entities.sistema.ConfiguracionUsuario;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.ConfiguracionUsuarioRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.sistema.ConfiguracionUsuarioService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class ConfiguracionUsuarioServiceImpl implements ConfiguracionUsuarioService {

    private final ConfiguracionUsuarioRepository configuracionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public ConfiguracionUsuarioResponseDTO obtenerPorUsuario(@NonNull Integer idUsuario) {
        return toDTO(configuracionRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Configuración no encontrada para usuario: " + idUsuario)));
    }

    @Override
    public ConfiguracionUsuarioResponseDTO guardar(ConfiguracionUsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        ConfiguracionUsuario config = configuracionRepository.findByUsuarioIdUsuario(dto.getIdUsuario())
                .orElse(new ConfiguracionUsuario());

        config.setUsuario(usuario);
        config.setRutaFotoPerfil(dto.getRutaFotoPerfil());
        config.setRutaFirmaEscaneada(dto.getRutaFirmaEscaneada());
        config.setNotificarSms(dto.getNotificarSms());
        config.setNotificarEmail(dto.getNotificarEmail());
        config.setNotificarWhatsapp(dto.getNotificarWhatsapp());
        config.setNotificarPush(dto.getNotificarPush());
        config.setIdioma(dto.getIdioma());
        config.setZonaHoraria(dto.getZonaHoraria());

        return toDTO(configuracionRepository.save(config));
    }

    private ConfiguracionUsuarioResponseDTO toDTO(ConfiguracionUsuario c) {
        return ConfiguracionUsuarioResponseDTO.builder()
                .idConfiguracion(c.getIdConfiguracion())
                .idUsuario(c.getUsuario().getIdUsuario())
                .rutaFotoPerfil(c.getRutaFotoPerfil())
                .rutaFirmaEscaneada(c.getRutaFirmaEscaneada())
                .notificarSms(c.getNotificarSms())
                .notificarEmail(c.getNotificarEmail())
                .notificarWhatsapp(c.getNotificarWhatsapp())
                .notificarPush(c.getNotificarPush())
                .idioma(c.getIdioma())
                .zonaHoraria(c.getZonaHoraria())
                .build();
    }
}
