package com.app.backend.entities.tramites;

import com.app.backend.entities.academico.Carrera;
import com.app.backend.entities.sistema.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "solicitudes", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Entidad central del dominio: representa una solicitud iniciada por un usuario
// y su estado dentro del flujo de tramite.
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @Column(name = "codigo_solicitud", unique = true, length = 50)
    private String codigoSolicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantilla", nullable = false)
    private PlantillaTramite plantilla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    @Column(name = "detalles_solicitud", columnDefinition = "text")
    private String detallesSolicitud;

    @Column(name = "prioridad", length = 20)
    @Builder.Default
    private String prioridad = "normal";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paso_actual")
    private PasoFlujo pasoActual;

    @Column(name = "estado_actual", length = 50)
    @Builder.Default
    private String estadoActual = "pendiente";

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_estimada_fin")
    private LocalDate fechaEstimadaFin;

    @Column(name = "fecha_real_fin")
    private LocalDateTime fechaRealFin;

    @Column(name = "resolucion", columnDefinition = "text")
    private String resolucion;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialSolicitud> historiales;

    @OneToMany(mappedBy = "solicitud")
    private List<Rechazo> rechazos;
}
