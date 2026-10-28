package com.app.backend.dtos.sistema.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionUsuarioResponseDTO {
    private Integer idConfiguracion;
    private Integer idUsuario;
    private String rutaFotoPerfil;
    private String rutaFirmaEscaneada;
    private Boolean notificarSms;
    private Boolean notificarEmail;
    private Boolean notificarWhatsapp;
    private Boolean notificarPush;
    private String idioma;
    private String zonaHoraria;
}