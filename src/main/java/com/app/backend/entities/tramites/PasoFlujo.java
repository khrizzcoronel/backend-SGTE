package com.app.backend.entities.tramites;

import com.app.backend.entities.sistema.Rol;
import com.app.backend.entities.sistema.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pasos_flujo", schema = "tramites",
    uniqueConstraints = @UniqueConstraint(
        name = "pasos_flujo_id_flujo_orden_paso_key",
        columnNames = {"id_flujo", "orden_paso"}
    ))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Paso individual dentro de un flujo con su orden, rol requerido y SLA.
public class PasoFlujo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paso")
    private Integer idPaso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_flujo", nullable = false)
    private FlujoTrabajo flujoTrabajo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etapa", nullable = false)
    private Etapa etapa;

    @Column(name = "orden_paso", nullable = false)
    private Integer ordenPaso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_requerido")
    private Rol rolRequerido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_encargado")
    private Usuario usuarioEncargado;

    @Column(name = "horas_sla")
    private Integer horasSla;
}
