package com.tatame.connect.feature.professor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.professor.model.domain.DiaSemanaEnum;
import com.tatame.connect.feature.professor.model.domain.EspecialidadeEnum;
import com.tatame.connect.feature.professor.model.domain.FaixaEnum;
import com.tatame.connect.feature.professor.model.domain.PeriodoEnum;
import com.tatame.connect.feature.professor.model.domain.TipoCobrancaEnum;
import com.tatame.connect.feature.professor.model.dto.ProfessorSignupRequestDto;
import com.tatame.connect.feature.professor.service.ProfessorService;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProfessorController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProfessorService professorService;

    private ProfessorSignupRequestDto buildValidRequest() {
        ProfessorSignupRequestDto dto = new ProfessorSignupRequestDto();
        dto.setEmail("joao@email.com");
        dto.setSenha("senha12345");
        dto.setNomeCompleto("Joao Gabriel");
        dto.setTelefoneWhatsapp("(11) 99999-9999");
        dto.setCidade("Sao Paulo");
        dto.setBairro("Vila Mariana");
        dto.setFaixa(FaixaEnum.PRETA);
        dto.setTempoExperienciaAnos(8);
        dto.setEspecialidades(Set.of(EspecialidadeEnum.ADULTO));
        dto.setDiasDisponiveis(Set.of(DiaSemanaEnum.SEGUNDA));
        dto.setPeriodosDisponiveis(Set.of(PeriodoEnum.NOITE));
        dto.setTipoCobranca(TipoCobrancaEnum.MENSAL);
        dto.setValorPretendido(new BigDecimal("3000.00"));
        dto.setAceiteTermos(true);
        return dto;
    }

    @Test
    void signup_requisicaoValida_retorna201ComToken() throws Exception {
        AuthResponseDto response = AuthResponseDto.builder()
            .token("jwt-gerado")
            .userId(UUID.randomUUID())
            .role(RoleEnum.PROFESSOR)
            .build();
        when(professorService.signup(any())).thenReturn(response);

        mockMvc.perform(post("/professores/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildValidRequest())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.token").value("jwt-gerado"));
    }

    @Test
    void signup_semAceiteDeTermos_retorna400() throws Exception {
        ProfessorSignupRequestDto request = buildValidRequest();
        request.setAceiteTermos(false);

        mockMvc.perform(post("/professores/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void signup_semEspecialidades_retorna400() throws Exception {
        ProfessorSignupRequestDto request = buildValidRequest();
        request.setEspecialidades(Set.of());

        mockMvc.perform(post("/professores/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
