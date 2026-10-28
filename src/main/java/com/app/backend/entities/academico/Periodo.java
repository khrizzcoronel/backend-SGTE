package com.app.backend.entities.academico;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "periodos", schema = "academico")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo")
    private Integer idPeriodo;

    @Column(name = "codigo_periodo", nullable = false, unique = true, length = 50)
    private String codigoPeriodo;

    @Column(name = "nombre_periodo", length = 100)
    private String nombrePeriodo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "esta_activo")
    @Builder.Default
    private Boolean estaActivo = true;

    @Column(name = "es_periodo_actual")
    @Builder.Default
    private Boolean esPeriodoActual = false;
}
