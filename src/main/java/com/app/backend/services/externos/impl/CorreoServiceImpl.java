package com.app.backend.services.externos.impl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.backend.services.externos.ICorreoService;

import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CorreoServiceImpl implements ICorreoService{

    @Value("${SMTP_PASSWORD:}")
    private String smtpPassword;

    @Value("${SMTP_USERNAME:}")
    private String smtpUsername;

    @Value("${SMTP_FROM:}")
    private String smtpFrom;

    @PersistenceContext
    private EntityManager entityManager;


    @Async
    @Override
    public void sendEmailAsync(String to, String subject, String body) {
        try {
            configurandoCorreo();
            enviarCorreo(to, subject, body);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }

    private JavaMailSenderImpl configurandoCorreo() {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();

        emailSender.setHost("smtp.gmail.com");
        emailSender.setPort(587);
        if (smtpUsername == null || smtpUsername.isBlank()) {
            throw new IllegalStateException("Configure SMTP_USERNAME para enviar correos");
        }
        emailSender.setUsername(smtpUsername);
        if (smtpPassword == null || smtpPassword.isBlank()) {
            throw new IllegalStateException("Configure SMTP_PASSWORD para enviar correos");
        }
        emailSender.setPassword(smtpPassword);

        Properties props = emailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return emailSender;
    }

    @SuppressWarnings("null")
    private void enviarCorreo(String to, String subject, String body) {
        try {
            JavaMailSenderImpl emailSender = configurandoCorreo();

            MimeMessage messageMime = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(messageMime, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            if (smtpFrom == null || smtpFrom.isBlank()) {
                throw new IllegalStateException("Configure SMTP_FROM para enviar correos");
            }
            helper.setFrom(smtpFrom, "Tramites estudiantiles");

            emailSender.send(messageMime);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}
