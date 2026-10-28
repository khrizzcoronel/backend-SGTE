package com.app.backend.services.externos;

public interface ICorreoService {
    void sendEmailAsync(String to, String subject, String body);
}
