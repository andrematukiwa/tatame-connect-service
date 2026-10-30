package com.tatame.connect.feature.academia.service.impl;

import com.tatame.connect.exception.base.BadRequestException;
import com.tatame.connect.feature.academia.converter.AcademiaConverter;
import com.tatame.connect.feature.academia.exception.AcademiaNotFoundException;
import com.tatame.connect.feature.academia.model.dto.AcademiaResponseDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaSignupRequestDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaUpdateRequestDto;
import com.tatame.connect.feature.academia.repository.AcademiaProfileRepository;
import com.tatame.connect.feature.academia.repository.entity.AcademiaProfileEntity;
import com.tatame.connect.feature.academia.service.AcademiaService;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.auth.repository.entity.UserEntity;
import com.tatame.connect.feature.auth.service.AuthService;
import com.tatame.connect.feature.storage.StorageService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AcademiaServiceImpl implements AcademiaService {

    private static final Set<String> ALLOWED_PHOTO_TYPES = Set.of("image/jpeg", "image/png");

    private final AcademiaProfileRepository academiaProfileRepository;
    private final AuthService authService;
    private final StorageService storageService;

    @Override
    @Transactional
    public AuthResponseDto signup(AcademiaSignupRequestDto request) {
        UserEntity user = authService.registerUser(request.getEmail(), request.getSenha(), RoleEnum.ACADEMIA);
        AcademiaProfileEntity profile = AcademiaConverter.toEntity(user.getId(), request);
        academiaProfileRepository.save(profile);
        return authService.buildAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademiaResponseDto getMyProfile(UUID userId) {
        return AcademiaConverter.toResponseDto(getEntityByUserId(userId));
    }

    @Override
    @Transactional
    public AcademiaResponseDto updateMyProfile(UUID userId, AcademiaUpdateRequestDto request) {
        AcademiaProfileEntity entity = getEntityByUserId(userId);
        AcademiaConverter.updateEntity(entity, request);
        academiaProfileRepository.save(entity);
        return AcademiaConverter.toResponseDto(entity);
    }

    @Override
    @Transactional
    public AcademiaResponseDto updateMyPhoto(UUID userId, MultipartFile fotoCapa) {
        if (fotoCapa == null || fotoCapa.isEmpty()) {
            throw new BadRequestException("Nenhum arquivo enviado");
        }
        if (!ALLOWED_PHOTO_TYPES.contains(fotoCapa.getContentType())) {
            throw new BadRequestException("Formato de imagem invalido. Use JPEG ou PNG");
        }

        AcademiaProfileEntity entity = getEntityByUserId(userId);
        String path = "academia/%s/foto-capa".formatted(entity.getId());

        try {
            String url = storageService.uploadFile(path, fotoCapa.getInputStream(), fotoCapa.getSize(), fotoCapa.getContentType());
            entity.setFotoCapaUrl(url);
        } catch (IOException exception) {
            throw new UncheckedIOException("Falha ao ler o arquivo enviado", exception);
        }

        academiaProfileRepository.save(entity);
        return AcademiaConverter.toResponseDto(entity);
    }

    private AcademiaProfileEntity getEntityByUserId(UUID userId) {
        return academiaProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new AcademiaNotFoundException("Perfil de academia nao encontrado para o usuario logado"));
    }
}
