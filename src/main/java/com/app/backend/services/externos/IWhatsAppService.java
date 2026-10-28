package com.app.backend.services.externos;

public interface IWhatsAppService {
    void enviarMensajeAsync(String numero, String mensaje);
    boolean estaConectado();
    String solicitarCodigoVinculacion();
}
