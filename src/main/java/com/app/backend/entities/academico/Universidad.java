package com.app.backend.entities.academico;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "universidades", schema = "academico")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Universidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_universidad")
    private Integer idUniversidad;

    @Column(name = "nombre_universidad", nullable = false, length = 255)
    private String nombreUniversidad;

    @Column(name = "es_publica", nullable = false)
    @Builder.Default
    private Boolean esPublica = true;

    @OneToMany(mappedBy = "universidad")
    private List<Facultad> facultades;
}
