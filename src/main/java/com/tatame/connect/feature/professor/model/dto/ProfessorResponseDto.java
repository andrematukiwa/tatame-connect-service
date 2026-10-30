package com.tatame.connect.feature.professor.model.dto;

import com.tatame.connect.feature.professor.model.domain.DiaSemanaEnum;
import com.tatame.connect.feature.professor.model.domain.EspecialidadeEnum;
import com.tatame.connect.feature.professor.model.domain.FaixaEnum;
import com.tatame.connect.feature.professor.model.domain.PeriodoEnum;
import com.tatame.connect.feature.professor.model.domain.TipoCobrancaEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorResponseDto {

    private UUID id;
    private String nomeCompleto;
    private String telefoneWhatsapp;
    private String cidade;
    private String bairro;
    private String fotoUrl;
    private String certificadoUrl;
    private FaixaEnum faixa;
    private String equipeLinhagem;
    private Integer tempoExperienciaAnos;
    private BigDecimal valorPretendido;
    private TipoCobrancaEnum tipoCobranca;
    private String bio;
    private Set<EspecialidadeEnum> especialidades;
    private Set<DiaSemanaEnum> diasDisponiveis;
    private Set<PeriodoEnum> periodosDisponiveis;
    private LocalDateTime criadoEm;
}
