package com.app.backend.entities.academico;

import com.app.backend.entities.sistema.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes", schema = "academico")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Carrera carrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;

    @Column(name = "numero_semestre")
    private Integer numeroSemestre;

    @Column(name = "numero_matricula", length = 50)
    private String numeroMatricula;

    @Column(name = "paralelo", length = 1)
    private String paralelo;

    @Column(name = "jornada", length = 20)
    @Builder.Default
    private String jornada = "Matutina";

    @Column(name = "estado_academico", length = 50)
    @Builder.Default
    private String estadoAcademico = "Regular";

    @Column(name = "promedio_general", precision = 4, scale = 2)
    private BigDecimal promedioGeneral;

    @Column(name = "creditos_aprobados")
    @Builder.Default
    private Integer creditosAprobados = 0;

    @Column(name = "es_becado")
    @Builder.Default
    private Boolean esBecado = false;

    @Column(name = "fecha_matricula")
    private LocalDate fechaMatricula;

    @Column(name = "es_externo")
    @Builder.Default
    private Boolean esExterno = false;

    @Column(name = "id_estudiante_sga")
    private Integer idEstudianteSga;

    @Column(name = "ultima_sincronizacion")
    private LocalDateTime ultimaSincronizacion;
}
