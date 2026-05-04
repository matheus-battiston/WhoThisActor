package com.MovieParticipations.MovieParticipations.util;

public final class TmdbImagemUrl {

    private static final String URL_BASE_IMAGEM = "https://image.tmdb.org/t/p";
    private static final String TAMANHO_IMAGEM_PADRAO = "/w200";

    private TmdbImagemUrl() {
    }

    public static String normalizar(String imagem) {
        if (imagem == null || imagem.isBlank()) return null;
        if (imagem.startsWith("http://") || imagem.startsWith("https://")) return imagem;
        if (imagem.startsWith("/")) return URL_BASE_IMAGEM + TAMANHO_IMAGEM_PADRAO + imagem;
        return URL_BASE_IMAGEM + TAMANHO_IMAGEM_PADRAO + "/" + imagem;
    }

    public static String caminhoRelativo(String imagem) {
        if (imagem == null || imagem.isBlank()) return null;
        if (!imagem.startsWith("http://") && !imagem.startsWith("https://")) {
            return imagem.startsWith("/") ? imagem : "/" + imagem;
        }

        int tamanhoImagemIndex = imagem.indexOf("/t/p/");
        if (tamanhoImagemIndex == -1) return imagem;

        String caminhoComTamanho = imagem.substring(tamanhoImagemIndex + "/t/p/".length());
        int primeiraBarra = caminhoComTamanho.indexOf("/");
        if (primeiraBarra == -1) return null;

        return caminhoComTamanho.substring(primeiraBarra);
    }
}
