package com.app.backend.entities.tramites;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "categorias", schema = "tramites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
// Categoria funcional para agrupar plantillas y motivos de rechazo.
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "nombre_categoria", nullable = false, length = 255)
    private String nombreCategoria;

    @Column(name = "descripcion_categoria", columnDefinition = "text")
    private String descripcionCategoria;

    @Column(name = "esta_activo")
    @Builder.Default
    private Boolean estaActivo = true;

    @OneToMany(mappedBy = "categoria")
    private List<PlantillaTramite> plantillas;

    @OneToMany(mappedBy = "categoria")
    private List<MotivoRechazo> motivosRechazo;
}
