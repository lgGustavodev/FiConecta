-- =========================================================
-- F CONECTA - SCRIPT INICIAL DO BANCO DE DADOS
-- Banco: ficonecta_dev
-- Execução: manual (sem Flyway/Liquibase nesta primeira versão)
-- =========================================================

-- Extensão necessária para gerar UUID automaticamente
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================================================
-- TABELA: usuarios
-- Responsável pela autenticação (email + senha + tipo)
-- =========================================================
CREATE TABLE usuarios (
                          id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          email           VARCHAR(150) NOT NULL UNIQUE,
                          senha           VARCHAR(255) NOT NULL,
                          tipo_usuario    VARCHAR(20)  NOT NULL CHECK (tipo_usuario IN ('PRESTADOR', 'EMPRESA')),
                          status          VARCHAR(20)  NOT NULL DEFAULT 'ATIVO' CHECK (status IN ('ATIVO', 'INATIVO')),
                          created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =========================================================
-- TABELA: prestadores
-- Dados específicos de quem presta o serviço
-- =========================================================
CREATE TABLE prestadores (
                             id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             usuario_id      UUID NOT NULL UNIQUE REFERENCES usuarios(id),
                             nome            VARCHAR(150) NOT NULL,
                             cpf             VARCHAR(14),
                             foto            VARCHAR(255),
                             descricao       TEXT,
                             disponivel      BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                             updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =========================================================
-- TABELA: empresas
-- Dados específicos de quem contrata (ex: supermercados)
-- =========================================================
CREATE TABLE empresas (
                          id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          usuario_id      UUID NOT NULL UNIQUE REFERENCES usuarios(id),
                          nome            VARCHAR(150) NOT NULL,
                          tipo_documento  VARCHAR(10) CHECK (tipo_documento IN ('CPF', 'CNPJ')),
                          documento       VARCHAR(20),
                          foto            VARCHAR(255),
                          descricao       TEXT,
                          created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =========================================================
-- TABELA: vagas
-- Diárias/serviços publicados por uma empresa
-- Versão mínima: sem convites, favoritos ou candidaturas ainda
-- =========================================================
CREATE TABLE vagas (
                       id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       empresa_id      UUID NOT NULL REFERENCES empresas(id),
                       titulo          VARCHAR(150) NOT NULL,
                       descricao       TEXT,
                       valor           NUMERIC(10,2) NOT NULL,
                       data_vaga       DATE NOT NULL,
                       status          VARCHAR(20) NOT NULL DEFAULT 'ABERTA' CHECK (status IN ('ABERTA', 'FECHADA', 'CANCELADA')),
                       created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =========================================================
-- ÍNDICES DE APOIO
-- =========================================================
CREATE INDEX idx_vagas_empresa_id ON vagas(empresa_id);
CREATE INDEX idx_vagas_status ON vagas(status);