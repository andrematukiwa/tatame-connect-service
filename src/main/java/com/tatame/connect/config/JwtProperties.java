package com.tatame.connect.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** Segredo usado para assinar o token (HMAC-SHA). Nunca versionar o valor real. */
    private String secret;

    private long expirationMinutes = 60 * 24 * 7;
}
