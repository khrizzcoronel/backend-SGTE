package com.app.backend.entities.tramites;

import com.app.backend.entities.sistema.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rechazos", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Registro formal de rechazo con motivo, responsable y estado de notificacion.
public class Rechazo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rechazo")
    private Integer idRechazo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private Solicitud solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_motivo", nullable = false)
    private MotivoRechazo motivoRechazo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rechazado_por", nullable = false)
    private Usuario rechazadoPor;

    @Column(name = "comentarios", nullable = false, columnDefinition = "text")
    private String comentarios;

    @Column(name = "fecha_rechazo")
    private LocalDateTime fechaRechazo;

    @Column(name = "notificacion_enviada")
    @Builder.Default
    private Boolean notificacionEnviada = false;

    @Column(name = "fecha_notificacion")
    private LocalDateTime fechaNotificacion;
}
