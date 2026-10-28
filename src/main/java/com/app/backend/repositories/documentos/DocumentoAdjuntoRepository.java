package com.app.backend.repositories.documentos;

import com.app.backend.entities.documentos.DocumentoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Persistencia de archivos adjuntos subidos por el usuario durante el tramite.
public interface DocumentoAdjuntoRepository extends JpaRepository<DocumentoAdjunto, Integer> {
    List<DocumentoAdjunto> findBySolicitudIdSolicitud(Integer idSolicitud);
}
