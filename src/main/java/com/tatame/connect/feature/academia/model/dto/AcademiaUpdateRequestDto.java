package com.tatame.connect.feature.academia.model.dto;

import com.tatame.connect.feature.academia.model.domain.TipoEspacoEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AcademiaUpdateRequestDto {

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
}
