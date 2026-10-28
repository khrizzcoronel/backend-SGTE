package com.app.backend.entities.documentos;

import com.app.backend.entities.sistema.Usuario;
import com.app.backend.entities.tramites.Solicitud;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_generados", schema = "documentos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Documento emitido por el sistema durante o al finalizar el tramite.
public class DocumentoGenerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_generado")
    private Integer idGenerado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private Solicitud solicitud;

    @Column(name = "tipo_documento", nullable = false, length = 100)
    private String tipoDocumento;

    @Column(name = "codigo_documento", unique = true, length = 100)
    private String codigoDocumento;

    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "plantilla_usada", length = 255)
    private String plantillaUsada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generado_por")
    private Usuario generadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firmado_por")
    private Usuario firmadoPor;

    @Column(name = "firma_digital", columnDefinition = "text")
    private String firmaDigital;

    @Column(name = "es_oficial")
    @Builder.Default
    private Boolean esOficial = false;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "valido_hasta")
    private LocalDate validoHasta;
}
