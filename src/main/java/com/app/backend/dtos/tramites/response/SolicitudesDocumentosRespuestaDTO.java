package com.app.backend.dtos.tramites.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
// DTO de documentos adjuntos asociados a una solicitud.
public class SolicitudesDocumentosRespuestaDTO {
    private Integer iddocumento;
    private String nombreoriginal;
    private String nombrearchivo;
    private String rutaarchivo;
    private Long tamanobytes;
    private String tipomime;
    private Boolean esvalido;
    private Date fechasubida;
    private String nombrerequisito;
}
