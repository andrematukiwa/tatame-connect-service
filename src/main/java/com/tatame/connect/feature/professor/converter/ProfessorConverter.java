package com.tatame.connect.feature.professor.converter;

import com.tatame.connect.feature.professor.model.dto.ProfessorResponseDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorSignupRequestDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorUpdateRequestDto;
import com.tatame.connect.feature.professor.repository.entity.ProfessorProfileEntity;
import java.util.UUID;

public final class ProfessorConverter {

    private ProfessorConverter() {}

    public static ProfessorProfileEntity toEntity(UUID userId, ProfessorSignupRequestDto dto) {
        return ProfessorProfileEntity.builder()
            .userId(userId)
            .nomeCompleto(dto.getNomeCompleto())
            .telefoneWhatsapp(dto.getTelefoneWhatsapp())
            .cidade(dto.getCidade())
            .bairro(dto.getBairro())
            .faixa(dto.getFaixa())
            .equipeLinhagem(dto.getEquipeLinhagem())
            .tempoExperienciaAnos(dto.getTempoExperienciaAnos())
            .valorPretendido(dto.getValorPretendido())
            .tipoCobranca(dto.getTipoCobranca())
            .bio(dto.getBio())
            .especialidades(dto.getEspecialidades())
            .diasDisponiveis(dto.getDiasDisponiveis())
            .periodosDisponiveis(dto.getPeriodosDisponiveis())
            .build();
    }

    public static void updateEntity(ProfessorProfileEntity entity, ProfessorUpdateRequestDto dto) {
        entity.setNomeCompleto(dto.getNomeCompleto());
        entity.setTelefoneWhatsapp(dto.getTelefoneWhatsapp());
        entity.setCidade(dto.getCidade());
        entity.setBairro(dto.getBairro());
        entity.setFaixa(dto.getFaixa());
        entity.setEquipeLinhagem(dto.getEquipeLinhagem());
        entity.setTempoExperienciaAnos(dto.getTempoExperienciaAnos());
        entity.setValorPretendido(dto.getValorPretendido());
        entity.setTipoCobranca(dto.getTipoCobranca());
        entity.setBio(dto.getBio());
        entity.setEspecialidades(dto.getEspecialidades());
        entity.setDiasDisponiveis(dto.getDiasDisponiveis());
        entity.setPeriodosDisponiveis(dto.getPeriodosDisponiveis());
    }

    public static ProfessorResponseDto toResponseDto(ProfessorProfileEntity entity) {
        return ProfessorResponseDto.builder()
            .id(entity.getId())
            .nomeCompleto(entity.getNomeCompleto())
            .telefoneWhatsapp(entity.getTelefoneWhatsapp())
            .cidade(entity.getCidade())
            .bairro(entity.getBairro())
            .fotoUrl(entity.getFotoUrl())
            .certificadoUrl(entity.getCertificadoUrl())
            .faixa(entity.getFaixa())
            .equipeLinhagem(entity.getEquipeLinhagem())
            .tempoExperienciaAnos(entity.getTempoExperienciaAnos())
            .valorPretendido(entity.getValorPretendido())
            .tipoCobranca(entity.getTipoCobranca())
            .bio(entity.getBio())
            .especialidades(entity.getEspecialidades())
            .diasDisponiveis(entity.getDiasDisponiveis())
            .periodosDisponiveis(entity.getPeriodosDisponiveis())
            .criadoEm(entity.getCriadoEm())
            .build();
    }
}
