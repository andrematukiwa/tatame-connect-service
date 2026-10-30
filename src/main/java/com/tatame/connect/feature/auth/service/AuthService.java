package com.tatame.connect.feature.auth.service;

import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.auth.model.dto.LoginRequestDto;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.repository.entity.UserEntity;

public interface AuthService {

    UserEntity registerUser(String email, String rawPassword, RoleEnum role);

    AuthResponseDto login(LoginRequestDto request);

    AuthResponseDto buildAuthResponse(UserEntity user);
}
