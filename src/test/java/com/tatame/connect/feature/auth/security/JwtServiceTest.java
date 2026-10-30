package com.tatame.connect.feature.auth.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.tatame.connect.config.JwtProperties;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("unit-test-secret-key-0123456789-abcdefghij");
        properties.setExpirationMinutes(60);
        jwtService = new JwtService(properties);
    }

    @Test
    void generateToken_eDepoisParseToken_retornaMesmoUsuarioERole() {
        UUID userId = UUID.randomUUID();

        String token = jwtService.generateToken(userId, RoleEnum.PROFESSOR);
        var parsed = jwtService.parseToken(token);

        assertThat(parsed).isPresent();
        assertThat(parsed.get().userId()).isEqualTo(userId);
        assertThat(parsed.get().role()).isEqualTo(RoleEnum.PROFESSOR);
    }

    @Test
    void parseToken_tokenInvalido_retornaVazio() {
        var parsed = jwtService.parseToken("token-completamente-invalido");

        assertThat(parsed).isEmpty();
    }

    @Test
    void parseToken_assinadoComOutroSegredo_retornaVazio() {
        JwtProperties outraProperties = new JwtProperties();
        outraProperties.setSecret("outro-secret-completamente-diferente-0123456789");
        outraProperties.setExpirationMinutes(60);
        JwtService outroJwtService = new JwtService(outraProperties);

        String token = outroJwtService.generateToken(UUID.randomUUID(), RoleEnum.ACADEMIA);

        assertThat(jwtService.parseToken(token)).isEmpty();
    }
}
