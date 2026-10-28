package com.app.backend.entities.tramites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "requisitos_plantilla", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Requisito documental que una solicitud debe adjuntar segun su plantilla.
public class RequisitoPlantilla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_requisito")
    private Integer idRequisito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantilla", nullable = false)
    private PlantillaTramite plantilla;

    @Column(name = "nombre_requisito", nullable = false, length = 255)
    private String nombreRequisito;

    @Column(name = "descripcion_requisito", columnDefinition = "text")
    private String descripcionRequisito;

    @Column(name = "es_obligatorio")
    @Builder.Default
    private Boolean esObligatorio = true;

    @Column(name = "tipo_documento", length = 100)
    private String tipoDocumento;

    @Column(name = "tamano_max_mb")
    @Builder.Default
    private Integer tamanoMaxMb = 10;

    @Column(name = "extensiones_permitidas", length = 255)
    @Builder.Default
    private String extensionesPermitidas = "pdf,jpg,png";

    @Column(name = "numero_orden")
    @Builder.Default
    private Integer numeroOrden = 0;
}
