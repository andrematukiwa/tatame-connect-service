package com.tatame.connect.feature.auth.security;

import com.tatame.connect.config.JwtProperties;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String CLAIM_ROLE = "role";

    private final JwtProperties jwtProperties;

    public String generateToken(UUID userId, RoleEnum role) {
        Instant now = Instant.now();
        return Jwts.builder()
            .subject(userId.toString())
            .claim(CLAIM_ROLE, role.name())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(jwtProperties.getExpirationMinutes(), ChronoUnit.MINUTES)))
            .signWith(signingKey())
            .compact();
    }

    public Optional<AuthenticatedUser> parseToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(signingKey()).build()
                .parseSignedClaims(token)
                .getPayload();
            UUID userId = UUID.fromString(claims.getSubject());
            RoleEnum role = RoleEnum.valueOf(claims.get(CLAIM_ROLE, String.class));
            return Optional.of(new AuthenticatedUser(userId, role));
        } catch (JwtException | IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public record AuthenticatedUser(UUID userId, RoleEnum role) {}
}
