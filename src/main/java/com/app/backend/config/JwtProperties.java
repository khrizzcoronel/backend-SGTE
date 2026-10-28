package com.app.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * The secret key used to sign and verify JWT tokens.
     */
    private String secret;

    /**
     * The expiration time of JWT tokens in milliseconds.
     */
    private long expiration;
}
