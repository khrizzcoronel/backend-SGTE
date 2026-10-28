package com.app.backend.services.sistema.impl;

import com.app.backend.dtos.sistema.request.RegistroUsuarioDTO;
import com.app.backend.dtos.sistema.response.RegistroUsuarioRespuestaDTO;
import com.app.backend.entities.academico.*;
import com.app.backend.entities.sistema.Credencial;
import com.app.backend.entities.sistema.Rol;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.*;
import com.app.backend.repositories.sistema.CredencialRepository;
import com.app.backend.repositories.sistema.RolRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.sistema.RegistroUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class RegistroUsuarioServiceImpl implements RegistroUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CarreraRepository carreraRepository;
    private final PeriodoRepository periodoRepository;
    private final EstudianteRepository estudianteRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final DecanoRepository decanoRepository;
    private final CredencialRepository credencialRepository;
    private final FacultadRepository facultadRepository;

    @Override
    public RegistroUsuarioRespuestaDTO registrarUsuario(RegistroUsuarioDTO dto) {
        log.info("Iniciando registro de usuario con cédula: {}, rol: {}", dto.getCedula(), dto.getRol());

        if (dto.getCedula() == null || dto.getCedula().isBlank()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        if (dto.getNombres() == null || dto.getNombres().isBlank()) {
            throw new IllegalArgumentException("Los nombres son obligatorios");
        }
        if (dto.getApellidos() == null || dto.getApellidos().isBlank()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        }
        if (dto.getCorreoInstitucional() == null || dto.getCorreoInstitucional().isBlank()) {
            throw new IllegalArgumentException("El correo institucional es obligatorio");
        }

        dto.setCedula(dto.getCedula().trim());
        dto.setNombres(dto.getNombres().trim());
        dto.setApellidos(dto.getApellidos().trim());
        dto.setCorreoInstitucional(dto.getCorreoInstitucional().trim());

        // 1. Validar que no exista un usuario con la misma cédula o correo
        if (usuarioRepository.existsByCedula(dto.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con la cédula: " + dto.getCedula());
        }
        if (usuarioRepository.existsByCorreoInstitucional(dto.getCorreoInstitucional())) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario registrado con el correo: " + dto.getCorreoInstitucional());
        }

        // 2. Buscar la Carrera por código
        Carrera carrera = null;
        if (dto.getCodigoCarrera() != null) {
            carrera = carreraRepository.findByCodigoCarrera(dto.getCodigoCarrera())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Carrera no encontrada con código: " + dto.getCodigoCarrera()));
        }

        // 3. Buscar el Periodo por código
        Periodo periodo = null;
        if (dto.getCodigoPeriodo() != null) {
            periodo = periodoRepository.findByCodigoPeriodo(dto.getCodigoPeriodo())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Periodo no encontrado con código: " + dto.getCodigoPeriodo()));
        }

        // 4. Buscar el Rol por nombre
        Rol rol = null;
        if (dto.getRol() != null) {
            rol = rolRepository.findByNombreRol(dto.getRol())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Rol no encontrado con nombre: " + dto.getRol()));
        }

        // 5. Crear el Usuario
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
                .estado(dto.getEstadoUsuario() != null ? dto.getEstadoUsuario() : true)
                .build();

        if (rol != null) {
            List<Rol> roles = new ArrayList<>();
            roles.add(rol);
            usuario.setRoles(roles);
        }

        usuario = usuarioRepository.save(usuario);

        // 6. Crear la Credencial con la cédula como contraseña por defecto y nombre generado
        String nombreUsuario = generarNombreUsuario(dto.getNombres(), dto.getApellidos(), dto.getCedula());

        log.info("nombreUsuario calculado para credencial: {}", nombreUsuario);

        Credencial credencial = new Credencial();
        credencial.setUsuario(usuario);
        credencial.setHashContrasena(dto.getCedula());
        credencial.setNombreUsuario(nombreUsuario);
        credencial.setEstado(true);
        credencialRepository.save(credencial);

        // 7. Crear la entidad específica según el rol
        Integer idEstudiante = null;
        Integer idCoordinador = null;
        Integer idDecano = null;

        String rolNombre = dto.getRol() != null ? dto.getRol().toLowerCase() : "";

        switch (rolNombre) {
            case "estudiante" -> {
                LocalDate fechaMatricula = null;
                if (dto.getFechaIngreso() != null) {
                    fechaMatricula = dto.getFechaIngreso().toLocalDate();
                }

                Estudiante estudiante = Estudiante.builder()
                        .usuario(usuario)
                        .carrera(carrera)
                        .periodo(periodo)
                        .paralelo(dto.getParalelo())
                        .estadoAcademico(dto.getEstadoAcademico() != null ? dto.getEstadoAcademico() : "Regular")
                        .fechaMatricula(fechaMatricula)
                        .esExterno(false)
                        .build();

                estudiante = estudianteRepository.save(estudiante);
                idEstudiante = estudiante.getIdEstudiante();
            }

            case "coordinador" -> {
                Coordinador coordinador = Coordinador.builder()
                        .usuario(usuario)
                        .carrera(carrera)
                        .horarioAtencion(dto.getHorarioAtencion())
                        .ubicacionOficina(dto.getOficinaAtencion())
                        .estaActivo(true)
                        .fechaNombramiento(dto.getFechaNombramientoCoordinador())
                        .build();

                coordinador = coordinadorRepository.save(coordinador);
                idCoordinador = coordinador.getIdCoordinador();
            }

            case "decano" -> {
                // Prioriza nombre de facultad para evitar cruces por id provenientes de otros catálogos.
                Facultad facultadDecano = null;

                if (dto.getNombreFacultad() != null && !dto.getNombreFacultad().isBlank()) {
                    String nombreFacultad = dto.getNombreFacultad().trim();
                    facultadDecano = facultadRepository.findByNombreFacultadIgnoreCase(nombreFacultad)
                            .orElseThrow(() -> new RecursoNoEncontradoException(
                                    "Facultad no encontrada con nombre: " + nombreFacultad));
                } else if (dto.getIdFacultad() != null) {
                    facultadDecano = facultadRepository.findById(dto.getIdFacultad())
                            .orElseThrow(() -> new RecursoNoEncontradoException(
                                    "Facultad no encontrada con id: " + dto.getIdFacultad()));
                }

                if (facultadDecano == null) {
                    throw new IllegalArgumentException(
                            "Para registrar un decano debe enviarse nombreFacultad o idFacultad válido.");
                }

                Decano decano = Decano.builder()
                        .usuario(usuario)
                        .facultad(facultadDecano)
                        .fechaNombramiento(dto.getFechaNombramientoDecano() != null
                                ? dto.getFechaNombramientoDecano().atStartOfDay()
                                : null)
                        .estaActivo(true)
                        .build();

                decano = decanoRepository.save(decano);
                idDecano = decano.getIdDecano();
            }

            default -> {
                // Rol genérico: solo se crea el usuario con credencial
            }
        }

        // 8. Construir la respuesta
        return RegistroUsuarioRespuestaDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .idEstudiante(idEstudiante)
                .idCoordinador(idCoordinador)
                .idDecano(idDecano)
                .cedula(usuario.getCedula())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .correoInstitucional(usuario.getCorreoInstitucional())
                .rol(dto.getRol())
                .nombreUsuario(nombreUsuario)
                .mensaje("Usuario registrado exitosamente")
                .build();
    }

    private String generarNombreUsuario(String nombres, String apellidos, String cedula) {
        if (nombres == null || apellidos == null || nombres.isBlank() || apellidos.isBlank()) {
            return cedula;
        }

        String[] nombresArray = nombres.trim().toLowerCase().split("\\s+");
        String[] apellidosArray = apellidos.trim().toLowerCase().split("\\s+");

        String inicialNombre = nombresArray.length > 0 && !nombresArray[0].isEmpty() ? nombresArray[0].substring(0, 1) : "";
        String primerApellido = apellidosArray.length > 0 ? apellidosArray[0] : "";
        String inicialSegundoApellido = apellidosArray.length > 1 && !apellidosArray[1].isEmpty() ? apellidosArray[1].substring(0, 1) : "";

        String base = inicialNombre + primerApellido + inicialSegundoApellido;

        // Quitar acentos y caracteres especiales
        base = java.text.Normalizer.normalize(base, java.text.Normalizer.Form.NFD);
        base = base.replaceAll("\\p{M}", "");
        base = base.replaceAll("[^a-z0-9]", "");

        if (base.isEmpty()) {
            return cedula;
        }

        String username = base;
        int counter = 1;
        while (credencialRepository.findByNombreUsuario(username).isPresent()) {
            username = base + counter;
            counter++;
        }
        return username;
    }
}
