package com.app.backend.entities.tramites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transiciones_flujo", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Reglas de transicion entre pasos de un flujo con banderas de negocio.
public class TransicionFlujo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transicion")
    private Integer idTransicion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_flujo", nullable = false)
    private FlujoTrabajo flujoTrabajo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paso_origen")
    private PasoFlujo pasoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paso_destino", nullable = false)
    private PasoFlujo pasoDestino;

    @Column(name = "observacion")
    @Builder.Default
    private Boolean observacion = false;

    @Column(name = "documento_generado")
    @Builder.Default
    private Boolean documentoGenerado = false;
}
