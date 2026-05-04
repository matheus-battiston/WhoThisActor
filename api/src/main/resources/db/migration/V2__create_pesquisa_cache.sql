CREATE SEQUENCE pesquisa_serie_cache_seq START 1 INCREMENT BY 50;
CREATE SEQUENCE pesquisa_filme_cache_seq START 1 INCREMENT BY 50;

CREATE TABLE pesquisa_serie_cache (
                                      id BIGINT PRIMARY KEY DEFAULT nextval('pesquisa_serie_cache_seq'),
                                      termo_normalizado VARCHAR(255) NOT NULL,
                                      ultima_sincronizacao DATE NOT NULL,

                                      CONSTRAINT uk_pesquisa_serie_cache_termo UNIQUE (termo_normalizado)
);

CREATE TABLE pesquisa_filme_cache (
                                      id BIGINT PRIMARY KEY DEFAULT nextval('pesquisa_filme_cache_seq'),
                                      termo_normalizado VARCHAR(255) NOT NULL,
                                      ultima_sincronizacao DATE NOT NULL,

                                      CONSTRAINT uk_pesquisa_filme_cache_termo UNIQUE (termo_normalizado)
);

CREATE INDEX ix_pesquisa_serie_cache_termo_normalizado
    ON pesquisa_serie_cache (termo_normalizado);

CREATE INDEX ix_pesquisa_filme_cache_termo_normalizado
    ON pesquisa_filme_cache (termo_normalizado);
