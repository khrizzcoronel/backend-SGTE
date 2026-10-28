package com.app.backend.dtos.documentos.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Payload minimo para registrar un adjunto asociado a un requisito.
public class DocumentoAdjuntoRequestDTO {
    private Integer idRequisito;
    private String nombreArchivo;
    private String nombreOriginal;
    private String rutaArchivo;
    private Long tamanoBytes;
}