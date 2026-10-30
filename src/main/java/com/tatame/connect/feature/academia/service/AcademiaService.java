package com.tatame.connect.feature.academia.service;

import com.tatame.connect.feature.academia.model.dto.AcademiaResponseDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaSignupRequestDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaUpdateRequestDto;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface AcademiaService {

    AuthResponseDto signup(AcademiaSignupRequestDto request);

    AcademiaResponseDto getMyProfile(UUID userId);

    AcademiaResponseDto updateMyProfile(UUID userId, AcademiaUpdateRequestDto request);

    AcademiaResponseDto updateMyPhoto(UUID userId, MultipartFile fotoCapa);
}
