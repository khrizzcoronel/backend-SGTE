package com.app.backend.entities.sistema;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "credenciales", schema = "sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credencial")
    private Integer idCredencial;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "hash_contrasena", nullable = false, columnDefinition = "text")
    private String hashContrasena;

    @Column(name = "nombre_usuario", length = 25)
    private String nombreUsuario;

    @Column(name = "estado")
    @Builder.Default
    private Boolean estado = true;
}
