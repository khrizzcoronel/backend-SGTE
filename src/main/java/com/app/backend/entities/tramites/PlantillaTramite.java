package com.app.backend.entities.tramites;

import com.app.backend.entities.academico.Carrera;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "plantillas_tramite", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Plantilla base de un tipo de tramite: define categoria, flujo, requisitos y reglas.
public class PlantillaTramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plantilla")
    private Integer idPlantilla;

    @Column(name = "nombre_plantilla", nullable = false, length = 255)
    private String nombrePlantilla;

    @Column(name = "descripcion_plantilla", columnDefinition = "text")
    private String descripcionPlantilla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_flujo")
    private FlujoTrabajo flujoTrabajo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;

    @Column(name = "dias_resolucion_estimados")
    @Builder.Default
    private Integer diasResolucionEstimados = 5;

    @Column(name = "esta_activo")
    @Builder.Default
    private Boolean estaActivo = true;

    @Column(name = "disponible_externos")
    @Builder.Default
    private Boolean disponibleExternos = false;

    @OneToMany(mappedBy = "plantilla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitoPlantilla> requisitos;

    @OneToMany(mappedBy = "plantilla")
    private List<VentanaRecepcion> ventanas;

    @OneToMany(mappedBy = "plantilla")
    private List<Solicitud> solicitudes;
}
