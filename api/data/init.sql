DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS permissao CASCADE;
DROP TABLE IF EXISTS serie CASCADE;
DROP TABLE IF EXISTS ator CASCADE;
DROP TABLE IF EXISTS serie_ator CASCADE;

-- Remover as sequências, se existirem
DROP SEQUENCE IF EXISTS usuario_seq CASCADE;
DROP SEQUENCE IF EXISTS permissao_seq CASCADE;
DROP SEQUENCE IF EXISTS serie_seq CASCADE;
DROP SEQUENCE IF EXISTS ator_seq CASCADE;
DROP SEQUENCE IF EXISTS serie_ator_seq CASCADE;

-- Criar a sequência para usuario com incremento 50
CREATE SEQUENCE usuario_seq START 1 INCREMENT BY 50;

CREATE TABLE usuario (
                         id BIGINT DEFAULT nextval('usuario_seq') NOT NULL,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL,
                         senha VARCHAR(100),
                         ativo BOOLEAN NOT NULL,
                         provider BOOLEAN NOT NULL
);

ALTER TABLE usuario ADD CONSTRAINT pk_usuario PRIMARY KEY (id);
ALTER TABLE usuario ADD CONSTRAINT uk_usuario_email UNIQUE (email);

-- Criar a sequência para permissao com incremento 50
CREATE SEQUENCE permissao_seq START 1 INCREMENT BY 50;

CREATE TABLE permissao (
                           id BIGINT DEFAULT nextval('permissao_seq') NOT NULL,
                           funcao VARCHAR(100) NOT NULL,
                           usuario_id BIGINT NOT NULL
);
ALTER TABLE permissao ADD CONSTRAINT pk_permissao PRIMARY KEY (id);
ALTER TABLE permissao ADD CONSTRAINT uk_permissao UNIQUE (funcao, usuario_id);
ALTER TABLE permissao ADD CONSTRAINT fk_permissao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario;

-- Criar a sequência para serie com incremento 50
CREATE SEQUENCE serie_seq START 1 INCREMENT BY 50;

CREATE TABLE serie (
                       id BIGINT DEFAULT nextval('serie_seq') NOT NULL,
                       titulo VARCHAR(255) NOT NULL,
                       tipo VARCHAR(10) NOT NULL,
                       imagem VARCHAR(255)
);

ALTER TABLE serie ADD CONSTRAINT pk_serie PRIMARY KEY (id);
ALTER TABLE serie ADD CONSTRAINT ck_serie_tipo CHECK (tipo IN ('TV','MOVIE'));

-- Criar a sequência para ator com incremento 50
CREATE SEQUENCE ator_seq START 1 INCREMENT BY 50;

CREATE TABLE ator (
                      id BIGINT DEFAULT nextval('ator_seq') NOT NULL,
                      nome VARCHAR(255) NOT NULL,
                      imagem VARCHAR(255),
                      popularity NUMERIC(5,3)
);

ALTER TABLE ator ADD CONSTRAINT pk_ator PRIMARY KEY (id);

-- Criar a sequência para serie_ator com incremento 50
CREATE SEQUENCE serie_ator_seq START 1 INCREMENT BY 50;

CREATE TABLE serie_ator (
                            id BIGINT DEFAULT nextval('serie_ator_seq') NOT NULL,
                            serie BIGINT NOT NULL,
                            ator BIGINT NOT NULL,
                            personagem VARCHAR(255) NOT NULL
);

ALTER TABLE serie_ator ADD CONSTRAINT pk_serie_ator PRIMARY KEY(id);
ALTER TABLE serie_ator ADD CONSTRAINT fk_serie FOREIGN KEY (serie) REFERENCES serie;
ALTER TABLE serie_ator ADD CONSTRAINT fk_ator FOREIGN KEY (ator) REFERENCES ator;