package com.tatame.connect.feature.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.auth.model.dto.LoginRequestDto;
import com.tatame.connect.feature.auth.service.AuthService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @Test
    void login_credenciaisValidas_retorna200ComToken() throws Exception {
        AuthResponseDto response = AuthResponseDto.builder()
            .token("jwt-gerado")
            .userId(UUID.randomUUID())
            .email("joao@email.com")
            .role(RoleEnum.PROFESSOR)
            .build();
        when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequestDto("joao@email.com", "senha12345"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-gerado"))
            .andExpect(jsonPath("$.role").value("PROFESSOR"));
    }

    @Test
    void login_emailEmBranco_retorna400() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequestDto("", "senha12345"))))
            .andExpect(status().isBadRequest());
    }
}
