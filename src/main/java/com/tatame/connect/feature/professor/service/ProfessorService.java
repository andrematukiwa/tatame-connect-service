package com.tatame.connect.feature.professor.service;

import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorResponseDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorSearchFilter;
import com.tatame.connect.feature.professor.model.dto.ProfessorSignupRequestDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorUpdateRequestDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProfessorService {

    AuthResponseDto signup(ProfessorSignupRequestDto request);

    ProfessorResponseDto getMyProfile(UUID userId);

    ProfessorResponseDto updateMyProfile(UUID userId, ProfessorUpdateRequestDto request);

    ProfessorResponseDto updateMyPhoto(UUID userId, MultipartFile foto);

    ProfessorResponseDto updateMyCertificado(UUID userId, MultipartFile certificado);

    ProfessorResponseDto getById(UUID professorId);

    Page<ProfessorResponseDto> search(ProfessorSearchFilter filter, Pageable pageable);
}
