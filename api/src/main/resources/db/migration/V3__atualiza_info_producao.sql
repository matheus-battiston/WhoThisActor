ALTER TABLE filme
    RENAME COLUMN inicializado TO elenco_inicializado;

ALTER TABLE serie
    RENAME COLUMN inicializado TO elenco_inicializado;

ALTER TABLE filme
    ADD COLUMN info_atualizado BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN data_lancamento DATE,
    ADD COLUMN backdrop_path VARCHAR(255),
    ADD COLUMN genero VARCHAR(255),
    ADD COLUMN overview VARCHAR(4000);

ALTER TABLE serie
    ADD COLUMN info_atualizado BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN backdrop_path VARCHAR(255),
    ADD COLUMN ano_primeira_temporada INTEGER,
    ADD COLUMN ano_ultima_temporada INTEGER,
    ADD COLUMN quantidade_temporadas INTEGER,
    ADD COLUMN genero VARCHAR(255),
    ADD COLUMN overview VARCHAR(4000);
