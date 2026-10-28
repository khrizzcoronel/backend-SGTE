package com.app.backend.repositories.tramites;

import com.app.backend.dtos.tramites.response.PlantillaDTO;
import com.app.backend.dtos.tramites.response.DetallesPlantillaBaseResponseDTO;
import com.app.backend.entities.tramites.PlantillaTramite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// Repositorio central de plantillas.
// Incluye proyecciones DTO para vistas de detalle y filtros avanzados.
public interface PlantillaTramiteRepository extends JpaRepository<PlantillaTramite, Integer> {
    List<PlantillaTramite> findByEstaActivoTrue();
    List<PlantillaTramite> findByCategoriaIdCategoria(Integer idCategoria);

    @Query("""
        SELECT new com.app.backend.dtos.tramites.response.DetallesPlantillaBaseResponseDTO(
            p.idPlantilla,
            p.nombrePlantilla,
            p.descripcionPlantilla,
            c.idCategoria,
            c.nombreCategoria,
            car.idCarrera,
            car.nombreCarrera,
            fct.idFacultad,
            fct.nombreFacultad,
            f.idFlujo,
            f.nombreFlujo,
            f.descripcion,
            u.idUsuario,
            COALESCE(cr.nombreUsuario, CONCAT(COALESCE(u.nombres, ''), ' ', COALESCE(u.apellidos, ''))),
            f.version,
            v.idVentana,
            v.fechaApertura,
            v.fechaCierre,
            v.permiteExtension,
            p.diasResolucionEstimados,
            p.estaActivo,
            p.disponibleExternos
        )
        FROM PlantillaTramite p
        LEFT JOIN p.categoria c
        LEFT JOIN p.carrera car
        LEFT JOIN car.facultad fct
        LEFT JOIN p.flujoTrabajo f
        LEFT JOIN f.creadoPor u
        LEFT JOIN u.credencial cr
        LEFT JOIN p.ventanas v
               ON v.idVentana = (
                   SELECT MAX(v2.idVentana)
                   FROM VentanaRecepcion v2
                   WHERE v2.plantilla.idPlantilla = p.idPlantilla
               )
        ORDER BY p.idPlantilla
        """)
    // Base de detalle para dashboard general de plantillas.
    List<DetallesPlantillaBaseResponseDTO> findAllDetallesBase();

    @Query("""
        SELECT new com.app.backend.dtos.tramites.response.DetallesPlantillaBaseResponseDTO(
            p.idPlantilla,
            p.nombrePlantilla,
            p.descripcionPlantilla,
            c.idCategoria,
            c.nombreCategoria,
            car.idCarrera,
            car.nombreCarrera,
            fct.idFacultad,
            fct.nombreFacultad,
            f.idFlujo,
            f.nombreFlujo,
            f.descripcion,
            u.idUsuario,
            COALESCE(cr.nombreUsuario, CONCAT(COALESCE(u.nombres, ''), ' ', COALESCE(u.apellidos, ''))),
            f.version,
            v.idVentana,
            v.fechaApertura,
            v.fechaCierre,
            v.permiteExtension,
            p.diasResolucionEstimados,
            p.estaActivo,
            p.disponibleExternos
        )
        FROM PlantillaTramite p
        LEFT JOIN p.categoria c
        LEFT JOIN p.carrera car
        LEFT JOIN car.facultad fct
        LEFT JOIN p.flujoTrabajo f
        LEFT JOIN f.creadoPor u
        LEFT JOIN u.credencial cr
        LEFT JOIN p.ventanas v
               ON v.idVentana = (
                   SELECT MAX(v2.idVentana)
                   FROM VentanaRecepcion v2
                   WHERE v2.plantilla.idPlantilla = p.idPlantilla
               )
        WHERE p.idPlantilla = :idPlantilla
        """)
    Optional<DetallesPlantillaBaseResponseDTO> findDetalleBaseByIdPlantilla(@Param("idPlantilla") Integer idPlantilla);

    @Query("""
        SELECT new com.app.backend.dtos.tramites.response.DetallesPlantillaBaseResponseDTO(
            p.idPlantilla,
            p.nombrePlantilla,
            p.descripcionPlantilla,
            c.idCategoria,
            c.nombreCategoria,
            car.idCarrera,
            car.nombreCarrera,
            fct.idFacultad,
            fct.nombreFacultad,
            f.idFlujo,
            f.nombreFlujo,
            f.descripcion,
            u.idUsuario,
            COALESCE(cr.nombreUsuario, CONCAT(COALESCE(u.nombres, ''), ' ', COALESCE(u.apellidos, ''))),
            f.version,
            v.idVentana,
            v.fechaApertura,
            v.fechaCierre,
            v.permiteExtension,
            p.diasResolucionEstimados,
            p.estaActivo,
            p.disponibleExternos
        )
        FROM PlantillaTramite p
        LEFT JOIN p.categoria c
        JOIN p.carrera car
        LEFT JOIN car.facultad fct
        LEFT JOIN p.flujoTrabajo f
        LEFT JOIN f.creadoPor u
        LEFT JOIN u.credencial cr
        LEFT JOIN p.ventanas v
               ON v.idVentana = (
                   SELECT MAX(v2.idVentana)
                   FROM VentanaRecepcion v2
                   WHERE v2.plantilla.idPlantilla = p.idPlantilla
               )
        WHERE car.idCarrera = :idCarrera
        ORDER BY p.idPlantilla
        """)
    List<DetallesPlantillaBaseResponseDTO> findAllDetallesBaseByCarrera(@Param("idCarrera") Integer idCarrera);

    @Query("""
        SELECT new com.app.backend.dtos.tramites.response.DetallesPlantillaBaseResponseDTO(
            p.idPlantilla,
            p.nombrePlantilla,
            p.descripcionPlantilla,
            c.idCategoria,
            c.nombreCategoria,
            car.idCarrera,
            car.nombreCarrera,
            fct.idFacultad,
            fct.nombreFacultad,
            f.idFlujo,
            f.nombreFlujo,
            f.descripcion,
            u.idUsuario,
            COALESCE(cr.nombreUsuario, CONCAT(COALESCE(u.nombres, ''), ' ', COALESCE(u.apellidos, ''))),
            f.version,
            v.idVentana,
            v.fechaApertura,
            v.fechaCierre,
            v.permiteExtension,
            p.diasResolucionEstimados,
            p.estaActivo,
            p.disponibleExternos
        )
        FROM PlantillaTramite p
        LEFT JOIN p.categoria c
        LEFT JOIN p.carrera car
        LEFT JOIN car.facultad fct
        LEFT JOIN p.flujoTrabajo f
        LEFT JOIN f.creadoPor u
        LEFT JOIN u.credencial cr
        LEFT JOIN p.ventanas v
               ON v.idVentana = (
                   SELECT MAX(v2.idVentana)
                   FROM VentanaRecepcion v2
                   WHERE v2.plantilla.idPlantilla = p.idPlantilla
               )
        WHERE fct.idFacultad = :idFacultad
        ORDER BY p.idPlantilla
        """)
    List<DetallesPlantillaBaseResponseDTO> findAllDetallesBaseByFacultad(@Param("idFacultad") Integer idFacultad);

    @Query(value = "Select * from tramites.fn_sl_plantillastramites(:categoria, :activo, :busqueda, :esExterno, :idCarrera)", nativeQuery = true)
    // Funcion SQL legacy usada para listado filtrado de plantillas en catalogo.
    List<PlantillaDTO> listarPlantillas(
            @Param("categoria") String categoria,
            @Param("activo") Boolean activo,
            @Param("busqueda") String busqueda,
            @Param("esExterno") Boolean esExterno,
            @Param("idCarrera") Integer idCarrera
    );
}

