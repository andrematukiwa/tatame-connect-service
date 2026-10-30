package com.tatame.connect.feature.academia.repository.entity;

import com.tatame.connect.feature.academia.model.domain.TipoEspacoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "academia_profiles", schema = "tatame")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademiaProfileEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "nome_academia", nullable = false)
    private String nomeAcademia;

    @Column(nullable = false)
    private String responsavel;

    @Column(name = "telefone_whatsapp", nullable = false)
    private String telefoneWhatsapp;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String bairro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_espaco", length = 30)
    private TipoEspacoEnum tipoEspaco;

    @Column(name = "foto_capa_url")
    private String fotoCapaUrl;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}
