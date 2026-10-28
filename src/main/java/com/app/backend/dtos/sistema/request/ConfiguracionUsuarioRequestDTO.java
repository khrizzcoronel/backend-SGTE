package com.app.backend.dtos.sistema.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionUsuarioRequestDTO {
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