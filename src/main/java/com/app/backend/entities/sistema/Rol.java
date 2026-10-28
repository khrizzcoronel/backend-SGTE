package com.app.backend.entities.sistema;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "roles", schema = "sistema")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre_rol", nullable = false, unique = true, length = 100)
    private String nombreRol;

    @Column(name = "descripcion_rol", columnDefinition = "text")
    private String descripcionRol;


    @Column(name = "nivel_jerarquico")
    @Builder.Default
    private Integer nivelJerarquico = 0;

    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;
}
