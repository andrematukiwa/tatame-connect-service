package com.tatame.connect.feature.professor.model.dto;

import com.tatame.connect.feature.professor.model.domain.DiaSemanaEnum;
import com.tatame.connect.feature.professor.model.domain.EspecialidadeEnum;
import com.tatame.connect.feature.professor.model.domain.FaixaEnum;
import com.tatame.connect.feature.professor.model.domain.PeriodoEnum;
import com.tatame.connect.feature.professor.model.domain.TipoCobrancaEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class ProfessorUpdateRequestDto {

    @NotBlank
    private String nomeCompleto;

    @NotBlank
    private String telefoneWhatsapp;

    @NotBlank
    private String cidade;

    @NotBlank
    private String bairro;

    @NotNull
    private FaixaEnum faixa;

    private String equipeLinhagem;

    @NotNull
    @Min(0)
    private Integer tempoExperienciaAnos;

    @NotEmpty
    private Set<EspecialidadeEnum> especialidades;

    @NotEmpty
    private Set<DiaSemanaEnum> diasDisponiveis;

    @NotEmpty
    private Set<PeriodoEnum> periodosDisponiveis;

    @NotNull
    private TipoCobrancaEnum tipoCobranca;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal valorPretendido;

    @Size(max = 500)
    private String bio;
}
