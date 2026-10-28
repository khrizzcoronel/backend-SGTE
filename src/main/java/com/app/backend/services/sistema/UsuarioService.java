package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.request.UsuarioRequestDTO;
import com.app.backend.dtos.sistema.response.UsuarioResponseDTO;
import com.app.backend.dtos.sistema.response.UsuarioFiltroResponseDTO;
import lombok.NonNull;
import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDTO> listarTodos();
    UsuarioResponseDTO obtenerPorId(@NonNull Integer id);
    UsuarioResponseDTO obtenerPorCedula(String cedula);
    UsuarioResponseDTO crear(UsuarioRequestDTO dto);
    UsuarioResponseDTO actualizar(@NonNull Integer id, UsuarioRequestDTO dto);
    void eliminar(@NonNull Integer id);
    List<UsuarioFiltroResponseDTO> listarFiltrados(Integer idUsuario, Integer idFacultad, String rol, String nombres, String apellidos);
}
