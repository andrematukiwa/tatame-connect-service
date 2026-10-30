package com.tatame.connect.feature.professor.service.impl;

import com.tatame.connect.exception.base.BadRequestException;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.auth.repository.entity.UserEntity;
import com.tatame.connect.feature.auth.service.AuthService;
import com.tatame.connect.feature.professor.converter.ProfessorConverter;
import com.tatame.connect.feature.professor.exception.ProfessorNotFoundException;
import com.tatame.connect.feature.professor.model.dto.ProfessorResponseDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorSearchFilter;
import com.tatame.connect.feature.professor.model.dto.ProfessorSignupRequestDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorUpdateRequestDto;
import com.tatame.connect.feature.professor.repository.ProfessorProfileRepository;
import com.tatame.connect.feature.professor.repository.ProfessorSpecifications;
import com.tatame.connect.feature.professor.repository.entity.ProfessorProfileEntity;
import com.tatame.connect.feature.professor.service.ProfessorService;
import com.tatame.connect.feature.storage.StorageService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private static final Set<String> ALLOWED_PHOTO_TYPES = Set.of("image/jpeg", "image/png");
    private static final Set<String> ALLOWED_CERTIFICADO_TYPES = Set.of("image/jpeg", "image/png", "application/pdf");

    private final ProfessorProfileRepository professorProfileRepository;
    private final AuthService authService;
    private final StorageService storageService;

    @Override
    @Transactional
    public AuthResponseDto signup(ProfessorSignupRequestDto request) {
        UserEntity user = authService.registerUser(request.getEmail(), request.getSenha(), RoleEnum.PROFESSOR);
        ProfessorProfileEntity profile = ProfessorConverter.toEntity(user.getId(), request);
        professorProfileRepository.save(profile);
        return authService.buildAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessorResponseDto getMyProfile(UUID userId) {
        return ProfessorConverter.toResponseDto(getEntityByUserId(userId));
    }

    @Override
    @Transactional
    public ProfessorResponseDto updateMyProfile(UUID userId, ProfessorUpdateRequestDto request) {
        ProfessorProfileEntity entity = getEntityByUserId(userId);
        ProfessorConverter.updateEntity(entity, request);
        professorProfileRepository.save(entity);
        return ProfessorConverter.toResponseDto(entity);
    }

    @Override
    @Transactional
    public ProfessorResponseDto updateMyPhoto(UUID userId, MultipartFile foto) {
        if (foto == null || foto.isEmpty()) {
            throw new BadRequestException("Nenhum arquivo enviado");
        }
        if (!ALLOWED_PHOTO_TYPES.contains(foto.getContentType())) {
            throw new BadRequestException("Formato de imagem invalido. Use JPEG ou PNG");
        }

        ProfessorProfileEntity entity = getEntityByUserId(userId);
        String path = "professor/%s/foto-perfil".formatted(entity.getId());

        try {
            String url = storageService.uploadFile(path, foto.getInputStream(), foto.getSize(), foto.getContentType());
            entity.setFotoUrl(url);
        } catch (IOException exception) {
            throw new UncheckedIOException("Falha ao ler o arquivo enviado", exception);
        }

        professorProfileRepository.save(entity);
        return ProfessorConverter.toResponseDto(entity);
    }

    @Override
    @Transactional
    public ProfessorResponseDto updateMyCertificado(UUID userId, MultipartFile certificado) {
        if (certificado == null || certificado.isEmpty()) {
            throw new BadRequestException("Nenhum arquivo enviado");
        }
        if (!ALLOWED_CERTIFICADO_TYPES.contains(certificado.getContentType())) {
            throw new BadRequestException("Formato invalido. Use JPEG, PNG ou PDF");
        }

        ProfessorProfileEntity entity = getEntityByUserId(userId);
        String path = "professor/%s/certificado".formatted(entity.getId());

        try {
            String url = storageService.uploadFile(
                path, certificado.getInputStream(), certificado.getSize(), certificado.getContentType()
            );
            entity.setCertificadoUrl(url);
        } catch (IOException exception) {
            throw new UncheckedIOException("Falha ao ler o arquivo enviado", exception);
        }

        professorProfileRepository.save(entity);
        return ProfessorConverter.toResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessorResponseDto getById(UUID professorId) {
        return ProfessorConverter.toResponseDto(
            professorProfileRepository.findById(professorId).orElseThrow(() -> new ProfessorNotFoundException(professorId))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessorResponseDto> search(ProfessorSearchFilter filter, Pageable pageable) {
        return professorProfileRepository.findAll(ProfessorSpecifications.withFilter(filter), pageable)
            .map(ProfessorConverter::toResponseDto);
    }

    private ProfessorProfileEntity getEntityByUserId(UUID userId) {
        return professorProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ProfessorNotFoundException("Perfil de professor nao encontrado para o usuario logado"));
    }
}
