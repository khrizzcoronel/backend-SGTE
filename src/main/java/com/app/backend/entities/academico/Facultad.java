package com.app.backend.entities.academico;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "facultades", schema = "academico")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Facultad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_facultad")
    private Integer idFacultad;

    @Column(name = "nombre_facultad", nullable = false, unique = true, length = 255)
    private String nombreFacultad;

    @Column(name = "codigo_facultad", length = 20)
    private String codigoFacultad;

    @Column(name = "ubicacion_oficina", length = 255)
    private String ubicacionOficina;

    @Column(name = "telefono_oficina", length = 20)
    private String telefonoOficina;

    @Column(name = "email_facultad", length = 255)
    private String emailFacultad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_universidad", nullable = false)
    private Universidad universidad;

    @OneToMany(mappedBy = "facultad")
    private List<Carrera> carreras;

    @OneToMany(mappedBy = "facultad")
    private List<Decano> decanos;
}
