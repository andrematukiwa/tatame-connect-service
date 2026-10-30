package com.tatame.connect.feature.auth.service.impl;

import com.tatame.connect.feature.auth.exception.EmailAlreadyUsedException;
import com.tatame.connect.feature.auth.exception.InvalidCredentialsException;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.auth.model.dto.LoginRequestDto;
import com.tatame.connect.feature.auth.repository.UserRepository;
import com.tatame.connect.feature.auth.repository.entity.UserEntity;
import com.tatame.connect.feature.auth.security.JwtService;
import com.tatame.connect.feature.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserEntity registerUser(String email, String rawPassword, RoleEnum role) {
        String normalizedEmail = normalizeEmail(email);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }

        UserEntity user = UserEntity.builder()
            .email(normalizedEmail)
            .passwordHash(passwordEncoder.encode(rawPassword))
            .role(role)
            .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        UserEntity user = userRepository.findByEmail(normalizeEmail(request.getEmail()))
            .filter(found -> passwordEncoder.matches(request.getSenha(), found.getPasswordHash()))
            .filter(UserEntity::isAtivo)
            .orElseThrow(InvalidCredentialsException::new);

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponseDto buildAuthResponse(UserEntity user) {
        String token = jwtService.generateToken(user.getId(), user.getRole());
        return AuthResponseDto.builder()
            .token(token)
            .userId(user.getId())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
