package com.app.backend.dtos.documentos.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO de salida de adjuntos con metadata de validacion y subida.
public class DocumentoAdjuntoDTO {
    private Integer idDocumento;
    private Integer idSolicitud;
    private Integer idRequisito;
    private String nombreArchivo;
    private String nombreOriginal;
    private String rutaArchivo;
    private Long tamanoBytes;
    private String tipoMime;
    private String checksum;
    private Boolean esValido;
    private String mensajeValidacion;
    private Integer subidoPorId;
    private LocalDateTime fechaSubida;
}