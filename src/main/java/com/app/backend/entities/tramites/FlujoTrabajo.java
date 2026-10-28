package com.app.backend.entities.tramites;

import com.app.backend.entities.sistema.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "flujos_trabajo", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Define el circuito de etapas por donde avanza una solicitud.
public class FlujoTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_flujo")
    private Integer idFlujo;

    @Column(name = "nombre_flujo", nullable = false, length = 255)
    private String nombreFlujo;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "esta_activo")
    @Builder.Default
    private Boolean estaActivo = true;

    @Column(name = "version")
    @Builder.Default
    private Integer version = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    @OneToMany(mappedBy = "flujoTrabajo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasoFlujo> pasos;

    @OneToMany(mappedBy = "flujoTrabajo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransicionFlujo> transiciones;

    @OneToMany(mappedBy = "flujoTrabajo")
    private List<PlantillaTramite> plantillas;
}
