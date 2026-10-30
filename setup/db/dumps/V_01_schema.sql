CREATE SCHEMA IF NOT EXISTS tatame;

CREATE TABLE tatame.users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE tatame.professor_profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES tatame.users (id),
    nome_completo VARCHAR(255) NOT NULL,
    telefone_whatsapp VARCHAR(20) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    foto_url VARCHAR(500),
    certificado_url VARCHAR(500),
    faixa VARCHAR(20) NOT NULL,
    equipe_linhagem VARCHAR(255),
    tempo_experiencia_anos INTEGER NOT NULL,
    valor_pretendido NUMERIC(10, 2) NOT NULL,
    tipo_cobranca VARCHAR(20) NOT NULL,
    bio VARCHAR(500),
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_professor_profiles_cidade ON tatame.professor_profiles (cidade);
CREATE INDEX idx_professor_profiles_faixa ON tatame.professor_profiles (faixa);

CREATE TABLE tatame.professor_especialidade (
    professor_id UUID NOT NULL REFERENCES tatame.professor_profiles (id) ON DELETE CASCADE,
    especialidade VARCHAR(30) NOT NULL
);

CREATE INDEX idx_professor_especialidade_professor_id ON tatame.professor_especialidade (professor_id);

CREATE TABLE tatame.professor_dia_disponivel (
    professor_id UUID NOT NULL REFERENCES tatame.professor_profiles (id) ON DELETE CASCADE,
    dia_semana VARCHAR(20) NOT NULL
);

CREATE INDEX idx_professor_dia_disponivel_professor_id ON tatame.professor_dia_disponivel (professor_id);

CREATE TABLE tatame.professor_periodo (
    professor_id UUID NOT NULL REFERENCES tatame.professor_profiles (id) ON DELETE CASCADE,
    periodo VARCHAR(20) NOT NULL
);

CREATE INDEX idx_professor_periodo_professor_id ON tatame.professor_periodo (professor_id);

CREATE TABLE tatame.academia_profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES tatame.users (id),
    nome_academia VARCHAR(255) NOT NULL,
    responsavel VARCHAR(255) NOT NULL,
    telefone_whatsapp VARCHAR(20) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    tipo_espaco VARCHAR(30),
    foto_capa_url VARCHAR(500),
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);
