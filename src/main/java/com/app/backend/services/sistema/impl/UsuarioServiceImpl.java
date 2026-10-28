package com.app.backend.services.sistema.impl;

import com.app.backend.dtos.sistema.request.UsuarioRequestDTO;
import com.app.backend.dtos.sistema.response.UsuarioFiltroResponseDTO;
import com.app.backend.dtos.sistema.response.UsuarioResponseDTO;
import com.app.backend.entities.academico.Coordinador;
import com.app.backend.entities.academico.Decano;
import com.app.backend.entities.academico.Estudiante;
import com.app.backend.entities.sistema.Rol;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.CoordinadorRepository;
import com.app.backend.repositories.academico.DecanoRepository;
import com.app.backend.repositories.academico.EstudianteRepository;
import com.app.backend.repositories.sistema.RolRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.sistema.UsuarioService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstudianteRepository estudianteRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final DecanoRepository decanoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(@NonNull Integer id) {
        return toDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorCedula(String cedula) {
        return toDTO(usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con cédula: " + cedula)));
    }

    @Override
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        Usuario usuario = Usuario.builder()
                .cedula(dto.getCedula())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .correoPersonal(dto.getCorreoPersonal())
                .correoInstitucional(dto.getCorreoInstitucional())
                .telefono(dto.getTelefono())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .direccion(dto.getDireccion())
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .build();

        if (dto.getRolesIds() != null && !dto.getRolesIds().isEmpty()) {
            List<Rol> roles = rolRepository.findAllById(dto.getRolesIds());
            usuario.setRoles(roles);
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponseDTO actualizar(@NonNull Integer id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreoPersonal(dto.getCorreoPersonal());
        usuario.setCorreoInstitucional(dto.getCorreoInstitucional());
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setGenero(dto.getGenero());
        usuario.setDireccion(dto.getDireccion());
        usuario.setEstado(dto.getEstado());

        if (dto.getRolesIds() != null) {
            List<Rol> roles = rolRepository.findAllById(dto.getRolesIds());
            usuario.setRoles(roles);
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(@NonNull Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioFiltroResponseDTO> listarFiltrados(Integer idUsuario, Integer idFacultad, String rol, String nombres, String apellidos) {
        String rolNormalizado = normalizarTexto(rol);
        String nombresNormalizado = normalizarTexto(nombres);
        String apellidosNormalizado = normalizarTexto(apellidos);

        return usuarioRepository.findAll().stream()
                .map(this::toFiltroDTO)
                .filter(dto -> dto.getRol() != null && esRolAcademicoPermitido(dto.getRol()))
                .filter(dto -> idUsuario == null || idUsuario.equals(dto.getIdUsuario()))
                .filter(dto -> idFacultad == null || idFacultad.equals(dto.getIdFacultad()))
                .filter(dto -> rolNormalizado == null || contieneNormalizado(dto.getRol(), rolNormalizado))
                .filter(dto -> nombresNormalizado == null || contieneNormalizado(dto.getNombres(), nombresNormalizado))
                .filter(dto -> apellidosNormalizado == null || contieneNormalizado(dto.getApellidos(), apellidosNormalizado))
                .toList();
    }

    private UsuarioResponseDTO toDTO(Usuario u) {
        return UsuarioResponseDTO.builder()
                .idUsuario(u.getIdUsuario())
                .cedula(u.getCedula())
                .nombres(u.getNombres())
                .apellidos(u.getApellidos())
                .correoPersonal(u.getCorreoPersonal())
                .correoInstitucional(u.getCorreoInstitucional())
                .telefono(u.getTelefono())
                .fechaNacimiento(u.getFechaNacimiento())
                .genero(u.getGenero())
                .direccion(u.getDireccion())
                .estado(u.getEstado())
                .rolesIds(u.getRoles() != null ? u.getRoles().stream().map(Rol::getIdRol).toList() : null)
                .build();
    }

    private UsuarioFiltroResponseDTO toFiltroDTO(Usuario usuario) {
        PerfilAcademico perfil = resolverPerfilAcademico(usuario);
        Integer idRol = usuario.getRoles() != null ? usuario.getRoles().stream()
            .filter(rol -> rol.getNombreRol() != null && perfil != null && rol.getNombreRol().equalsIgnoreCase(perfil.rol))
            .map(Rol::getIdRol)
            .findFirst()
            .orElse(null) : null;

        return UsuarioFiltroResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
            .idRol(idRol)
                .rol(perfil != null ? perfil.rol : null)
                .idFacultad(perfil != null ? perfil.idFacultad : null)
                .facultad(perfil != null ? perfil.facultad : null)
                .idCarrera(perfil != null ? perfil.idCarrera : null)
                .carrera(perfil != null ? perfil.carrera : null)
                .build();
    }

    private PerfilAcademico resolverPerfilAcademico(Usuario usuario) {
        Integer idUsuario = usuario.getIdUsuario();

        Decano decano = decanoRepository.findByUsuarioIdUsuario(idUsuario).orElse(null);
        if (decano != null) {
            Integer idFacultad = decano.getFacultad() != null ? decano.getFacultad().getIdFacultad() : null;
            String facultad = decano.getFacultad() != null ? decano.getFacultad().getNombreFacultad() : null;
            return new PerfilAcademico(idFacultad, facultad, null, null, "Decano");
        }

        Coordinador coordinador = coordinadorRepository.findByUsuarioIdUsuario(idUsuario).orElse(null);
        if (coordinador != null) {
            Integer idCarrera = coordinador.getCarrera() != null ? coordinador.getCarrera().getIdCarrera() : null;
            String carrera = coordinador.getCarrera() != null ? coordinador.getCarrera().getNombreCarrera() : null;
            Integer idFacultad = coordinador.getCarrera() != null && coordinador.getCarrera().getFacultad() != null
                    ? coordinador.getCarrera().getFacultad().getIdFacultad()
                    : null;
            String facultad = coordinador.getCarrera() != null && coordinador.getCarrera().getFacultad() != null
                    ? coordinador.getCarrera().getFacultad().getNombreFacultad()
                    : null;
            return new PerfilAcademico(idFacultad, facultad, idCarrera, carrera, "Coordinador");
        }

        Estudiante estudiante = estudianteRepository.findByUsuarioIdUsuario(idUsuario).orElse(null);
        if (estudiante != null) {
            Integer idCarrera = estudiante.getCarrera() != null ? estudiante.getCarrera().getIdCarrera() : null;
            String carrera = estudiante.getCarrera() != null ? estudiante.getCarrera().getNombreCarrera() : null;
            Integer idFacultad = estudiante.getCarrera() != null && estudiante.getCarrera().getFacultad() != null
                    ? estudiante.getCarrera().getFacultad().getIdFacultad()
                    : null;
            String facultad = estudiante.getCarrera() != null && estudiante.getCarrera().getFacultad() != null
                    ? estudiante.getCarrera().getFacultad().getNombreFacultad()
                    : null;
            return new PerfilAcademico(idFacultad, facultad, idCarrera, carrera, "Estudiante");
        }

        return null;
    }

    private String normalizarTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        return texto.trim().toLowerCase(Locale.ROOT);
    }

    private boolean contieneNormalizado(String valor, String filtro) {
        if (valor == null || filtro == null) {
            return false;
        }
        return valor.toLowerCase(Locale.ROOT).contains(filtro);
    }

    private boolean esRolAcademicoPermitido(String rol) {
        String rolNormalizado = normalizarTexto(rol);
        return "decano".equals(rolNormalizado) || "coordinador".equals(rolNormalizado);
    }

    private static final class PerfilAcademico {
        private final Integer idFacultad;
        private final String facultad;
        private final Integer idCarrera;
        private final String carrera;
        private final String rol;

        private PerfilAcademico(Integer idFacultad, String facultad, Integer idCarrera, String carrera, String rol) {
            this.idFacultad = idFacultad;
            this.facultad = facultad;
            this.idCarrera = idCarrera;
            this.carrera = carrera;
            this.rol = rol;
        }
    }
}
