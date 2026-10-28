package com.app.backend.entities.academico;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "carreras", schema = "academico")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrera")
    private Integer idCarrera;

    @Column(name = "nombre_carrera", nullable = false, length = 255)
    private String nombreCarrera;

    @Column(name = "codigo_carrera", unique = true, length = 50)
    private String codigoCarrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facultad", nullable = false)
    private Facultad facultad;

    @Column(name = "duracion_semestres")
    @Builder.Default
    private Integer duracionSemestres = 10;

    @Column(name = "modalidad", length = 50)
    @Builder.Default
    private String modalidad = "Presencial";

    @OneToMany(mappedBy = "carrera")
    private List<Estudiante> estudiantes;

    @OneToMany(mappedBy = "carrera")
    private List<Coordinador> coordinadores;
}
