package com.app.backend.dtos.tramites.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Vista consolidada de una solicitud para pantalla de seguimiento completo.
public class SolicitudDetalleResponseDTO {
    private Integer idSolicitud;
    private String codigoSolicitud;
    private String nombrePlantilla;
    private String categoria;
    private String carrera;
    private String nombreUsuario;
    private String prioridad;
    private String estadoActual;
    private String detallesSolicitud;
    private String resolucion;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaEstimadaFin;
    private LocalDateTime fechaRealFin;
    private Integer etapaActual;
    private Integer totalEtapas;
    private Integer idPasoActual;

    private List<PasoFlujoItemDTO> pasosFlujo;
    private List<HistorialItemDTO> historial;
    private List<DocumentoItemDTO> documentos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // Representa un paso del flujo con datos de etapa y responsable.
    public static class PasoFlujoItemDTO {
        private Integer idPaso;
        private Integer ordenPaso;
        private String nombreEtapa;
        private String codigoEtapa;
        private String descripcionEtapa;
        private String rolRequerido;
        private Integer idRolRequerido;
        private String usuarioEncargado;
        private Integer horasSla;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // Item del historial cronologico de acciones realizadas.
    public static class HistorialItemDTO {
        private Integer idHistorial;
        private String nombreEtapa;
        private String codigoEtapa;
        private String estado;
        private String tipoAccion;
        private String procesadoPor;
        private String comentarios;
        private LocalDateTime fechaEntrada;
        private LocalDateTime fechaCompletado;
        private Boolean slaExcedido;
        private Integer ordenPaso;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // Metadatos de un documento adjunto asociado a la solicitud.
    public static class DocumentoItemDTO {
        private Integer idDocumento;
        private String nombreOriginal;
        private String nombreArchivo;
        private String rutaArchivo;
        private Long tamanoBytes;
        private String tipoMime;
        private Boolean esValido;
        private LocalDateTime fechaSubida;
        private String nombreRequisito;
    }
}
