package com.MovieParticipations.MovieParticipations.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.TAMANHO_LOGO_PROVIDER;
import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.caminhoRelativo;
import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("TmdbImagemUrl")
class TmdbImagemUrlTest {
    private static final String URL_IMAGEM_PADRAO = "https://image.tmdb.org/t/p/w400/matrix.jpg";
    private static final String URL_LOGO_PROVIDER = "https://image.tmdb.org/t/p/w500/netflix.jpg";
    private static final String URL_HTTP_COMPLETA = "http://cdn.exemplo.com/imagem.jpg";
    private static final String URL_HTTPS_COMPLETA = "https://cdn.exemplo.com/imagem.jpg";
    private static final String CAMINHO_COM_BARRA = "/matrix.jpg";
    private static final String CAMINHO_SEM_BARRA = "matrix.jpg";
    private static final String CAMINHO_LOGO_PROVIDER = "/netflix.jpg";
    private static final String URL_TMDB_ORIGINAL = "https://image.tmdb.org/t/p/original/backdrop.jpg";
    private static final String URL_TMDB_SEM_CAMINHO = "https://image.tmdb.org/t/p/w400";
    private static final String URL_FORA_DO_PADRAO_TMDB = "https://image.tmdb.org/imagem.jpg";

    @Test
    @DisplayName("Deve retornar nulo ao normalizar imagem nula ou em branco")
    void deveRetornarNuloAoNormalizarImagemNulaOuEmBranco() {
        assertNull(normalizar(null));
        assertNull(normalizar(" "));
    }

    @Test
    @DisplayName("Deve normalizar caminho relativo com barra usando tamanho padrão")
    void deveNormalizarCaminhoRelativoComBarraUsandoTamanhoPadrao() {
        assertEquals(URL_IMAGEM_PADRAO, normalizar(CAMINHO_COM_BARRA));
    }

    @Test
    @DisplayName("Deve normalizar caminho relativo sem barra usando tamanho padrão")
    void deveNormalizarCaminhoRelativoSemBarraUsandoTamanhoPadrao() {
        assertEquals(URL_IMAGEM_PADRAO, normalizar(CAMINHO_SEM_BARRA));
    }

    @Test
    @DisplayName("Deve normalizar imagem com tamanho customizado")
    void deveNormalizarImagemComTamanhoCustomizado() {
        assertEquals(URL_LOGO_PROVIDER, normalizar(CAMINHO_LOGO_PROVIDER, TAMANHO_LOGO_PROVIDER));
    }

    @Test
    @DisplayName("Deve manter URL completa ao normalizar")
    void deveManterUrlCompletaAoNormalizar() {
        assertEquals(URL_HTTP_COMPLETA, normalizar(URL_HTTP_COMPLETA));
        assertEquals(URL_HTTPS_COMPLETA, normalizar(URL_HTTPS_COMPLETA));
    }

    @Test
    @DisplayName("Deve retornar nulo ao extrair caminho de imagem nula ou em branco")
    void deveRetornarNuloAoExtrairCaminhoDeImagemNulaOuEmBranco() {
        assertNull(caminhoRelativo(null));
        assertNull(caminhoRelativo(" "));
    }

    @Test
    @DisplayName("Deve manter caminho relativo com barra")
    void deveManterCaminhoRelativoComBarra() {
        assertEquals(CAMINHO_COM_BARRA, caminhoRelativo(CAMINHO_COM_BARRA));
    }

    @Test
    @DisplayName("Deve adicionar barra em caminho relativo sem barra")
    void deveAdicionarBarraEmCaminhoRelativoSemBarra() {
        assertEquals(CAMINHO_COM_BARRA, caminhoRelativo(CAMINHO_SEM_BARRA));
    }

    @Test
    @DisplayName("Deve extrair caminho relativo de URL TMDB completa")
    void deveExtrairCaminhoRelativoDeUrlTmdbCompleta() {
        assertEquals("/backdrop.jpg", caminhoRelativo(URL_TMDB_ORIGINAL));
    }

    @Test
    @DisplayName("Deve manter URL completa fora do padrão TMDB")
    void deveManterUrlCompletaForaDoPadraoTmdb() {
        assertEquals(URL_FORA_DO_PADRAO_TMDB, caminhoRelativo(URL_FORA_DO_PADRAO_TMDB));
    }

    @Test
    @DisplayName("Deve retornar nulo quando URL TMDB não tiver caminho após tamanho")
    void deveRetornarNuloQuandoUrlTmdbNaoTiverCaminhoAposTamanho() {
        assertNull(caminhoRelativo(URL_TMDB_SEM_CAMINHO));
    }
}
