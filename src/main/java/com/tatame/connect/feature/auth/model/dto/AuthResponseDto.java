package com.tatame.connect.feature.auth.model.dto;

import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String token;
    private UUID userId;
    private String email;
    private RoleEnum role;
}
