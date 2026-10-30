package com.tatame.connect.feature.auth.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tatame.connect.feature.auth.exception.EmailAlreadyUsedException;
import com.tatame.connect.feature.auth.exception.InvalidCredentialsException;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.auth.model.dto.LoginRequestDto;
import com.tatame.connect.feature.auth.repository.UserRepository;
import com.tatame.connect.feature.auth.repository.entity.UserEntity;
import com.tatame.connect.feature.auth.security.JwtService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthServiceImpl authService;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void registerUser_emailNovo_criaUsuarioComSenhaCriptografadaERoleInformada() {
        when(userRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(passwordEncoder.encode("senha12345")).thenReturn("hash-da-senha");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = authService.registerUser(" Joao@Email.com ", "senha12345", RoleEnum.PROFESSOR);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        assertThat(captor.getValue().getEmail()).isEqualTo("joao@email.com");
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("hash-da-senha");
        assertThat(captor.getValue().getRole()).isEqualTo(RoleEnum.PROFESSOR);
        assertThat(result.getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    void registerUser_emailJaCadastrado_lancaEmailAlreadyUsedException() {
        when(userRepository.existsByEmail("joao@email.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.registerUser("joao@email.com", "senha12345", RoleEnum.PROFESSOR))
            .isInstanceOf(EmailAlreadyUsedException.class);
    }

    @Test
    void login_credenciaisValidas_retornaTokenDoUsuario() {
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder()
            .id(userId)
            .email("joao@email.com")
            .passwordHash("hash-da-senha")
            .role(RoleEnum.PROFESSOR)
            .ativo(true)
            .build();

        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha12345", "hash-da-senha")).thenReturn(true);
        when(jwtService.generateToken(userId, RoleEnum.PROFESSOR)).thenReturn("jwt-gerado");

        AuthResponseDto response = authService.login(new LoginRequestDto("joao@email.com", "senha12345"));

        assertThat(response.getToken()).isEqualTo("jwt-gerado");
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getRole()).isEqualTo(RoleEnum.PROFESSOR);
    }

    @Test
    void login_senhaIncorreta_lancaInvalidCredentialsException() {
        UserEntity user = UserEntity.builder()
            .id(UUID.randomUUID())
            .email("joao@email.com")
            .passwordHash("hash-da-senha")
            .role(RoleEnum.PROFESSOR)
            .ativo(true)
            .build();

        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq("hash-da-senha"))).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequestDto("joao@email.com", "senha-errada")))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_emailNaoCadastrado_lancaInvalidCredentialsException() {
        when(userRepository.findByEmail("desconhecido@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequestDto("desconhecido@email.com", "qualquer")))
            .isInstanceOf(InvalidCredentialsException.class);
    }
}
