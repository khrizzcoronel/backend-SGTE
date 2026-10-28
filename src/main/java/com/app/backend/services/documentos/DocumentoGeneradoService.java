package com.app.backend.services.documentos;

import com.app.backend.dtos.documentos.response.DocumentoGeneradoDTO;
import lombok.NonNull;
import java.util.List;

// Contrato de documentos generados por el sistema a partir de solicitudes.
public interface DocumentoGeneradoService {
    List<DocumentoGeneradoDTO> listarPorSolicitud(@NonNull Integer idSolicitud);
    DocumentoGeneradoDTO obtenerPorId(@NonNull Integer id);
    DocumentoGeneradoDTO crear(DocumentoGeneradoDTO dto);
    void eliminar(@NonNull Integer id);
}
