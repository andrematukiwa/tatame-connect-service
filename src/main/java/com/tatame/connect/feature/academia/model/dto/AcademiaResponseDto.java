package com.tatame.connect.feature.academia.model.dto;

import com.tatame.connect.feature.academia.model.domain.TipoEspacoEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademiaResponseDto {

    private UUID id;
    private String nomeAcademia;
    private String responsavel;
    private String telefoneWhatsapp;
    private String cidade;
    private String bairro;
    private TipoEspacoEnum tipoEspaco;
    private String fotoCapaUrl;
    private LocalDateTime criadoEm;
}
