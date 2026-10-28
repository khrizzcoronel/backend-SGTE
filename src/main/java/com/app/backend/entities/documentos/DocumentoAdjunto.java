package com.app.backend.entities.documentos;

import com.app.backend.entities.sistema.Usuario;
import com.app.backend.entities.tramites.RequisitoPlantilla;
import com.app.backend.entities.tramites.Solicitud;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_adjuntos", schema = "documentos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Archivo cargado por el usuario como respaldo de un requisito de solicitud.
public class DocumentoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer idDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private Solicitud solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_requisito")
    private RequisitoPlantilla requisitoPlantilla;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "nombre_original", length = 255)
    private String nombreOriginal;

    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "tipo_mime", length = 100)
    private String tipoMime;

    @Column(name = "checksum", length = 64)
    private String checksum;

    @Column(name = "es_valido")
    private Boolean esValido;

    @Column(name = "mensaje_validacion", columnDefinition = "text")
    private String mensajeValidacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subido_por")
    private Usuario subidoPor;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;
}
