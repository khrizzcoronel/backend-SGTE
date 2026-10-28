package com.app.backend.entities.tramites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "etapas", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Etapa reutilizable del flujo, identificada tambien por un codigo unico.
public class Etapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etapa")
    private Integer idEtapa;

    @Column(name = "nombre_etapa", nullable = false, length = 255)
    private String nombreEtapa;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "codigo", unique = true, length = 50)
    private String codigo;
}
