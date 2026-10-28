package com.app.backend.services.externos.impl;

import com.app.backend.services.externos.IWhatsAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@SuppressWarnings("null")
public class WhatsAppServiceImpl implements IWhatsAppService {

    @Value("${whatsapp.service.url:http://localhost:7075}")
    private String whatsappServiceUrl;

    @Value("${whatsapp.service.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
            new ParameterizedTypeReference<>() {};

    @Async
    @Override
    public void enviarMensajeAsync(String numero, String mensaje) {
        if (numero == null || numero.isBlank()) {
            log.warn("WhatsApp: número vacío, se omite el envío");
            return;
        }

        // Limpiar número: quitar +, espacios, guiones
        String numeroLimpio = numero.replaceAll("[^0-9]", "");
        if (numeroLimpio.startsWith("0")) {
            // Si empieza con 0, agregar código de país Ecuador
            numeroLimpio = "593" + numeroLimpio.substring(1);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);

            Map<String, String> body = Map.of(
                    "numero", numeroLimpio,
                    "mensaje", mensaje
            );

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    whatsappServiceUrl + "/api/whatsapp/enviar",
                    HttpMethod.POST,
                    request,
                    MAP_TYPE
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("WhatsApp enviado a {}", numeroLimpio);
            } else {
                log.warn("WhatsApp respuesta inesperada: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error al enviar WhatsApp a {}: {}", numeroLimpio, e.getMessage());
        }
    }

    @Override
    public boolean estaConectado() {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    whatsappServiceUrl + "/api/whatsapp/estado",
                    HttpMethod.GET,
                    null,
                    MAP_TYPE
            );
            Map<String, Object> responseBody = response.getBody();
            if (response.getStatusCode().is2xxSuccessful() && responseBody != null) {
                return "conectado".equals(responseBody.get("estado"));
            }
        } catch (Exception e) {
            log.debug("WhatsApp service no disponible: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public String solicitarCodigoVinculacion() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of(), headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    whatsappServiceUrl + "/api/whatsapp/solicitar-codigo",
                    HttpMethod.POST,
                    request,
                    MAP_TYPE
            );

            Map<String, Object> responseBody = response.getBody();
            if (response.getStatusCode().is2xxSuccessful() && responseBody != null) {
                Boolean exito = (Boolean) responseBody.get("exito");
                if (Boolean.TRUE.equals(exito)) {
                    return (String) responseBody.get("codigo");
                } else {
                    throw new RuntimeException("Error en Node: " + responseBody.get("error"));
                }
            } else {
                throw new RuntimeException("Error HTTP del servicio de WhatsApp: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error al solicitar código de vinculación: {}", e.getMessage());
            throw new RuntimeException("No se pudo solicitar el código de vinculación: " + e.getMessage());
        }
    }
}
