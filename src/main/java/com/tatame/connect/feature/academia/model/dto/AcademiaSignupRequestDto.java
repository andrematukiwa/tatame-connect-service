package com.tatame.connect.feature.academia.model.dto;

import com.tatame.connect.feature.academia.model.domain.TipoEspacoEnum;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AcademiaSignupRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "A senha deve ter no minimo 8 caracteres")
    private String senha;

    @NotBlank
    private String nomeAcademia;

    @NotBlank
    private String responsavel;

    @NotBlank
    private String telefoneWhatsapp;

    @NotBlank
    private String cidade;

    @NotBlank
    private String bairro;

    private TipoEspacoEnum tipoEspaco;

    @AssertTrue(message = "E necessario aceitar os Termos de Uso e a Politica de Privacidade")
    private boolean aceiteTermos;
}
