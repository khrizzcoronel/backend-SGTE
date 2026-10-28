package com.app.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "groq")
public class GroqProperties {

    private Api api = new Api();
    private String model = "llama-3.3-70b-versatile";
    private int maxTokens = 2048;
    private double temperature = 0.1;
    private int timeoutSeconds = 10;

    @Data
    public static class Api {
        private String url = "https://api.groq.com/openai/v1/chat/completions";
        private String key = "";
    }
}