package com.tatame.connect.feature.auth.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import jakarta.servlet.FilterChain;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_headerComTokenValido_autenticaNoSecurityContext() throws Exception {
        UUID userId = UUID.randomUUID();
        var authenticatedUser = new JwtService.AuthenticatedUser(userId, RoleEnum.ACADEMIA);

        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-valido");
        var response = new MockHttpServletResponse();

        when(jwtService.parseToken("token-valido")).thenReturn(Optional.of(authenticatedUser));

        new JwtAuthenticationFilter(jwtService).doFilterInternal(request, response, filterChain);

        var authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userId);
        assertThat(authentication.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_ACADEMIA");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_semHeaderDeAutorizacao_naoAutenticaESeguiAFrente() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();

        new JwtAuthenticationFilter(jwtService).doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilterInternal_tokenInvalido_naoAutenticaMasSeguiAFrente() throws Exception {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-invalido");
        var response = new MockHttpServletResponse();

        when(jwtService.parseToken("token-invalido")).thenReturn(Optional.empty());

        new JwtAuthenticationFilter(jwtService).doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}
