package com.tatame.connect.feature.professor.model.dto;

import com.tatame.connect.feature.professor.model.domain.DiaSemanaEnum;
import com.tatame.connect.feature.professor.model.domain.EspecialidadeEnum;
import com.tatame.connect.feature.professor.model.domain.FaixaEnum;
import com.tatame.connect.feature.professor.model.domain.PeriodoEnum;
import java.math.BigDecimal;

public record ProfessorSearchFilter(
    String cidade,
    FaixaEnum faixa,
    EspecialidadeEnum especialidade,
    DiaSemanaEnum diaSemana,
    PeriodoEnum periodo,
    BigDecimal valorMin,
    BigDecimal valorMax
) {}
