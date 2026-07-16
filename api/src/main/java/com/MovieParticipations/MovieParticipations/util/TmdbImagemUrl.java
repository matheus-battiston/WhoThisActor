package com.MovieParticipations.MovieParticipations.util;

public final class TmdbImagemUrl {

    private static final String URL_BASE_IMAGEM = "https://image.tmdb.org/t/p";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String BARRA = "/";
    private static final String CAMINHO_TAMANHO_IMAGEM = "/t/p/";
    private static final String TAMANHO_IMAGEM_PADRAO = "/w400";
    public static final String TAMANHO_LOGO_PROVIDER = "/w500";

    private TmdbImagemUrl() {
    }

    public static String normalizar(String imagem) {
        return normalizar(imagem, TAMANHO_IMAGEM_PADRAO);
    }

    public static String normalizar(String imagem, String tamanho) {
        if (imagem == null || imagem.isBlank()) return null;
        if (ehUrlCompleta(imagem)) return imagem;
        if (imagem.startsWith(BARRA)) return URL_BASE_IMAGEM + tamanho + imagem;
        return URL_BASE_IMAGEM + tamanho + BARRA + imagem;
    }

    public static String caminhoRelativo(String imagem) {
        if (imagem == null || imagem.isBlank()) return null;
        if (!ehUrlCompleta(imagem)) {
            return imagem.startsWith(BARRA) ? imagem : BARRA + imagem;
        }

        int tamanhoImagemIndex = imagem.indexOf(CAMINHO_TAMANHO_IMAGEM);
        if (tamanhoImagemIndex == -1) return imagem;

        String caminhoComTamanho = imagem.substring(tamanhoImagemIndex + CAMINHO_TAMANHO_IMAGEM.length());
        int primeiraBarra = caminhoComTamanho.indexOf(BARRA);
        if (primeiraBarra == -1) return null;

        return caminhoComTamanho.substring(primeiraBarra);
    }

    private static boolean ehUrlCompleta(String imagem) {
        return imagem.startsWith(HTTP) || imagem.startsWith(HTTPS);
    }
}
