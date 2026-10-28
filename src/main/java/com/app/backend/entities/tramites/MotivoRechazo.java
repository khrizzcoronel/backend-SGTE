package com.app.backend.entities.tramites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "motivos_rechazo", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Catalogo de razones estandar para justificar rechazos de solicitudes.
public class MotivoRechazo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_motivo")
    private Integer idMotivo;

    @Column(name = "codigo_motivo", unique = true, length = 50)
    private String codigoMotivo;

    @Column(name = "nombre_motivo", nullable = false, length = 255)
    private String nombreMotivo;

    @Column(name = "descripcion_motivo", columnDefinition = "text")
    private String descripcionMotivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(name = "esta_activo")
    @Builder.Default
    private Boolean estaActivo = true;
}
