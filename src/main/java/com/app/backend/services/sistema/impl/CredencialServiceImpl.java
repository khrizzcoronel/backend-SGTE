package com.app.backend.services.sistema.impl;

import com.app.backend.dtos.sistema.request.CredencialRequestDTO;
import com.app.backend.dtos.sistema.response.CredencialResponseDTO;
import com.app.backend.entities.sistema.Credencial;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.CredencialRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.sistema.CredencialService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class CredencialServiceImpl implements CredencialService {

    private final CredencialRepository credencialRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public CredencialResponseDTO obtenerPorUsuario(@NonNull Integer idUsuario) {
        return toDTO(credencialRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Credencial no encontrada para usuario: " + idUsuario)));
    }

    @Override
    public CredencialResponseDTO guardar(CredencialRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        Credencial credencial = credencialRepository.findByUsuarioIdUsuario(dto.getIdUsuario())
                .orElse(new Credencial());

        credencial.setUsuario(usuario);
        credencial.setHashContrasena(dto.getHashContrasena());
        credencial.setEstado(dto.getEstado() != null ? dto.getEstado() : true);

        return toDTO(credencialRepository.save(credencial));
    }

    private CredencialResponseDTO toDTO(Credencial c) {
        return CredencialResponseDTO.builder()
                .idCredencial(c.getIdCredencial())
                .idUsuario(c.getUsuario().getIdUsuario())
                .estado(c.getEstado())
                .build();
    }
}
