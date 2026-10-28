package com.app.backend.entities.sistema;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "tipos_notificacion", schema = "sistema")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TipoNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(name = "codigo_tipo", nullable = false, unique = true, length = 50)
    private String codigoTipo;

    @Column(name = "nombre_tipo", nullable = false, length = 100)
    private String nombreTipo;

    @Column(name = "plantilla_defecto", columnDefinition = "text")
    private String plantillaDefecto;

    @Column(name = "esta_activo")
    @Builder.Default
    private Boolean estaActivo = true;

    @OneToMany(mappedBy = "tipoNotificacion")
    private List<Notificacion> notificaciones;
}
