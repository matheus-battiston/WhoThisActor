-- =========================================================
-- SEQUENCES
-- =========================================================

CREATE SEQUENCE usuario_seq START 1 INCREMENT BY 50;
CREATE SEQUENCE permissao_seq START 1 INCREMENT BY 50;
CREATE SEQUENCE ator_seq START 1 INCREMENT BY 50;

CREATE SEQUENCE serie_seq START 1 INCREMENT BY 50;
CREATE SEQUENCE serie_ator_seq START 1 INCREMENT BY 50;
CREATE SEQUENCE favorita_serie_seq START 1 INCREMENT BY 50;

CREATE SEQUENCE filme_seq START 1 INCREMENT BY 50;
CREATE SEQUENCE filme_ator_seq START 1 INCREMENT BY 50;
CREATE SEQUENCE favorita_filme_seq START 1 INCREMENT BY 50;

CREATE SEQUENCE favorita_ator_seq START 1 INCREMENT BY 50;

-- =========================================================
-- TABLES
-- =========================================================

CREATE TABLE usuario (
                         id BIGINT PRIMARY KEY DEFAULT nextval('usuario_seq'),
                         auth_user_id BIGINT NOT NULL UNIQUE,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         ativo BOOLEAN NOT NULL
);

CREATE TABLE permissao (
                           id BIGINT PRIMARY KEY DEFAULT nextval('permissao_seq'),
                           funcao VARCHAR(100) NOT NULL,
                           usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,

                           CONSTRAINT uk_permissao UNIQUE (funcao, usuario_id)
);

CREATE TABLE ator (
                      id BIGINT PRIMARY KEY DEFAULT nextval('ator_seq'),
                      nome VARCHAR(255) NOT NULL,
                      nome_normalizado VARCHAR(255) NOT NULL,
                      imagem VARCHAR(255),
                      popularity DOUBLE PRECISION,
                      id_tmdb BIGINT,
                      inicializado BOOLEAN NOT NULL DEFAULT FALSE,
                      ultima_atualizacao DATE NOT NULL,

                      CONSTRAINT ux_ator_id_tmdb UNIQUE (id_tmdb)
);

CREATE TABLE serie (
                       id BIGINT PRIMARY KEY DEFAULT nextval('serie_seq'),
                       titulo VARCHAR(255) NOT NULL,
                       titulo_normalizado VARCHAR(255) NOT NULL,
                       id_tmdb BIGINT NOT NULL,
                       ultima_atualizacao DATE NOT NULL,
                       imagem VARCHAR(255),
                       popularidade DOUBLE PRECISION,
                       inicializado BOOLEAN NOT NULL DEFAULT FALSE,

                       CONSTRAINT uk_serie_tmdb UNIQUE (id_tmdb)
);

CREATE TABLE serie_ator (
                            id BIGINT PRIMARY KEY DEFAULT nextval('serie_ator_seq'),
                            serie BIGINT NOT NULL REFERENCES serie(id) ON DELETE CASCADE,
                            ator BIGINT NOT NULL REFERENCES ator(id) ON DELETE CASCADE,
                            personagem VARCHAR(255),
                            numero_episodios INTEGER NOT NULL,

                            CONSTRAINT uk_serie_ator UNIQUE (serie, ator, personagem)
);

CREATE TABLE filme (
                       id BIGINT PRIMARY KEY DEFAULT nextval('filme_seq'),
                       titulo VARCHAR(255) NOT NULL,
                       titulo_normalizado VARCHAR(255) NOT NULL,
                       id_tmdb BIGINT NOT NULL,
                       ultima_atualizacao DATE NOT NULL,
                       imagem VARCHAR(255),
                       popularidade DOUBLE PRECISION,
                       inicializado BOOLEAN NOT NULL DEFAULT FALSE,

                       CONSTRAINT uk_filme_tmdb UNIQUE (id_tmdb)
);

CREATE TABLE filme_ator (
                            id BIGINT PRIMARY KEY DEFAULT nextval('filme_ator_seq'),
                            filme BIGINT NOT NULL REFERENCES filme(id) ON DELETE CASCADE,
                            ator BIGINT NOT NULL REFERENCES ator(id) ON DELETE CASCADE,
                            personagem VARCHAR(255),
                            credit_order BIGINT,

                            CONSTRAINT uk_filme_ator UNIQUE (filme, ator, personagem)
);

CREATE TABLE favorita_ator (
                               id BIGINT PRIMARY KEY DEFAULT nextval('favorita_ator_seq'),
                               ator BIGINT NOT NULL REFERENCES ator(id) ON DELETE CASCADE,
                               usuario BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,

                               CONSTRAINT uk_favorita_ator UNIQUE (ator, usuario)
);

CREATE TABLE favorita_serie (
                                id BIGINT PRIMARY KEY DEFAULT nextval('favorita_serie_seq'),
                                serie BIGINT NOT NULL REFERENCES serie(id) ON DELETE CASCADE,
                                usuario BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,

                                CONSTRAINT uk_favorita_serie UNIQUE (serie, usuario)
);

CREATE TABLE favorita_filme (
                                id BIGINT PRIMARY KEY DEFAULT nextval('favorita_filme_seq'),
                                filme BIGINT NOT NULL REFERENCES filme(id) ON DELETE CASCADE,
                                usuario BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,

                                CONSTRAINT uk_favorita_filme UNIQUE (filme, usuario)
);

-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX ix_ator_nome_normalizado
    ON ator (nome_normalizado);

CREATE INDEX ix_serie_titulo_normalizado
    ON serie (titulo_normalizado);

CREATE INDEX ix_filme_titulo_normalizado
    ON filme (titulo_normalizado);

CREATE INDEX ix_serie_ator_serie
    ON serie_ator (serie);

CREATE INDEX ix_serie_ator_ator
    ON serie_ator (ator);

CREATE INDEX ix_filme_ator_filme
    ON filme_ator (filme);

CREATE INDEX ix_filme_ator_ator
    ON filme_ator (ator);

CREATE INDEX ix_favorita_ator_usuario
    ON favorita_ator (usuario);

CREATE INDEX ix_favorita_serie_usuario
    ON favorita_serie (usuario);

CREATE INDEX ix_favorita_filme_usuario
    ON favorita_filme (usuario);