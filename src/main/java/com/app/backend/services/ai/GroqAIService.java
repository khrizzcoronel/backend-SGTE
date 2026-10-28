package com.app.backend.services.ai;

import com.app.backend.config.GroqProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroqAIService {

    private final GroqProperties groqProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public boolean isAvailable() {
        String key = groqProperties.getApi().getKey();
        return key != null && !key.isBlank();
    }

    /**
     * Envía un prompt al modelo Groq y retorna la respuesta como texto libre.
     * No fuerza formato JSON — ideal para el chatbot conversacional.
     */
    @SuppressWarnings("null")
    public String chatFreeText(String systemPrompt, String userPrompt) {
        if (!isAvailable()) {
            log.warn("[GroqAIService] API key no configurada, saltando chatFreeText");
            return null;
        }

        try {
            Map<String, Object> payload = Map.of(
                    "model", groqProperties.getModel(),
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userPrompt)
                    ),
                    "max_tokens", groqProperties.getMaxTokens(),
                    "temperature", 0.5
            );
            String payloadJson = objectMapper.writeValueAsString(payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqProperties.getApi().getKey());

            HttpEntity<String> entity = new HttpEntity<>(payloadJson, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    groqProperties.getApi().getUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode responseNode = objectMapper.readTree(response.getBody());
            JsonNode choicesNode = responseNode.get("choices");
            if (choicesNode != null && choicesNode.isArray() && !choicesNode.isEmpty()) {
                JsonNode messageNode = choicesNode.get(0).get("message");
                if (messageNode != null) {
                    log.info("[GroqAIService] Respuesta recibida exitosamente");
                    return messageNode.get("content").asText();
                }
            }

            log.error("[GroqAIService] Respuesta sin contenido válido");
            return null;

        } catch (HttpClientErrorException e) {
            log.error("[GroqAIService] Error HTTP: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new GroqAIException("Error de API Groq: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("[GroqAIService] Error al comunicarse con Groq: {}", e.getMessage());
            throw new GroqAIException("Error de comunicación con Groq: " + e.getMessage(), e);
        }
    }

    public static class GroqAIException extends RuntimeException {
        public GroqAIException(String message) { super(message); }
        public GroqAIException(String message, Throwable cause) { super(message, cause); }
    }
}