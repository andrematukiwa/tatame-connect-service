package com.tatame.connect.feature.academia.converter;

import com.tatame.connect.feature.academia.model.dto.AcademiaResponseDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaSignupRequestDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaUpdateRequestDto;
import com.tatame.connect.feature.academia.repository.entity.AcademiaProfileEntity;
import java.util.UUID;

public final class AcademiaConverter {

    private AcademiaConverter() {}

    public static AcademiaProfileEntity toEntity(UUID userId, AcademiaSignupRequestDto dto) {
        return AcademiaProfileEntity.builder()
            .userId(userId)
            .nomeAcademia(dto.getNomeAcademia())
            .responsavel(dto.getResponsavel())
            .telefoneWhatsapp(dto.getTelefoneWhatsapp())
            .cidade(dto.getCidade())
            .bairro(dto.getBairro())
            .tipoEspaco(dto.getTipoEspaco())
            .build();
    }

    public static void updateEntity(AcademiaProfileEntity entity, AcademiaUpdateRequestDto dto) {
        entity.setNomeAcademia(dto.getNomeAcademia());
        entity.setResponsavel(dto.getResponsavel());
        entity.setTelefoneWhatsapp(dto.getTelefoneWhatsapp());
        entity.setCidade(dto.getCidade());
        entity.setBairro(dto.getBairro());
        entity.setTipoEspaco(dto.getTipoEspaco());
    }

    public static AcademiaResponseDto toResponseDto(AcademiaProfileEntity entity) {
        return AcademiaResponseDto.builder()
            .id(entity.getId())
            .nomeAcademia(entity.getNomeAcademia())
            .responsavel(entity.getResponsavel())
            .telefoneWhatsapp(entity.getTelefoneWhatsapp())
            .cidade(entity.getCidade())
            .bairro(entity.getBairro())
            .tipoEspaco(entity.getTipoEspaco())
            .fotoCapaUrl(entity.getFotoCapaUrl())
            .criadoEm(entity.getCriadoEm())
            .build();
    }
}
