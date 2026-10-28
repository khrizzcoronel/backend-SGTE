package com.app.backend.entities.tramites;

import com.app.backend.entities.academico.Periodo;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "ventanas_recepcion", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Define el periodo en que una plantilla puede recibir solicitudes.
public class VentanaRecepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ventana")
    private Integer idVentana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantilla")
    private PlantillaTramite plantilla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "fecha_cierre", nullable = false)
    private LocalDate fechaCierre;

    @Column(name = "permite_extension")
    @Builder.Default
    private Boolean permiteExtension = false;

    @Column(name = "dias_max_extension")
    @Builder.Default
    private Integer diasMaxExtension = 0;
}
