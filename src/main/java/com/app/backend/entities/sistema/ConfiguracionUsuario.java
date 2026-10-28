package com.app.backend.entities.sistema;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuraciones_usuario", schema = "sistema")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ConfiguracionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion")
    private Integer idConfiguracion;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "ruta_foto_perfil", length = 500)
    private String rutaFotoPerfil;

    @Column(name = "ruta_firma_escaneada", length = 500)
    private String rutaFirmaEscaneada;

    @Column(name = "notificar_sms")
    @Builder.Default
    private Boolean notificarSms = false;

    @Column(name = "notificar_email")
    @Builder.Default
    private Boolean notificarEmail = true;

    @Column(name = "notificar_whatsapp")
    @Builder.Default
    private Boolean notificarWhatsapp = false;

    @Column(name = "notificar_push")
    @Builder.Default
    private Boolean notificarPush = true;

    @Column(name = "idioma", length = 10)
    @Builder.Default
    private String idioma = "es";

    @Column(name = "zona_horaria", length = 50)
    @Builder.Default
    private String zonaHoraria = "America/Guayaquil";
}
