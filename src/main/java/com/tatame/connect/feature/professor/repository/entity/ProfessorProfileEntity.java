package com.tatame.connect.feature.professor.repository.entity;

import com.tatame.connect.feature.professor.model.domain.DiaSemanaEnum;
import com.tatame.connect.feature.professor.model.domain.EspecialidadeEnum;
import com.tatame.connect.feature.professor.model.domain.FaixaEnum;
import com.tatame.connect.feature.professor.model.domain.PeriodoEnum;
import com.tatame.connect.feature.professor.model.domain.TipoCobrancaEnum;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professor_profiles", schema = "tatame")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorProfileEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "telefone_whatsapp", nullable = false)
    private String telefoneWhatsapp;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String bairro;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "certificado_url")
    private String certificadoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FaixaEnum faixa;

    @Column(name = "equipe_linhagem")
    private String equipeLinhagem;

    @Column(name = "tempo_experiencia_anos", nullable = false)
    private Integer tempoExperienciaAnos;

    @Column(name = "valor_pretendido", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPretendido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobranca", nullable = false, length = 20)
    private TipoCobrancaEnum tipoCobranca;

    @Column(length = 500)
    private String bio;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "professor_especialidade", schema = "tatame", joinColumns = @JoinColumn(name = "professor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "especialidade")
    private Set<EspecialidadeEnum> especialidades = new HashSet<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "professor_dia_disponivel", schema = "tatame", joinColumns = @JoinColumn(name = "professor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    private Set<DiaSemanaEnum> diasDisponiveis = new HashSet<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "professor_periodo", schema = "tatame", joinColumns = @JoinColumn(name = "professor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "periodo")
    private Set<PeriodoEnum> periodosDisponiveis = new HashSet<>();

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
