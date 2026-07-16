package com.MovieParticipations.MovieParticipations.util;

public final class TmdbImagemUrl {

    private static final String URL_BASE_IMAGEM = "https://image.tmdb.org/t/p";
    private static final String TAMANHO_IMAGEM_PADRAO = "/w400";
    public static final String TAMANHO_LOGO_PROVIDER = "/w500";

    private TmdbImagemUrl() {
    }

    public static String normalizar(String imagem) {
        return normalizar(imagem, TAMANHO_IMAGEM_PADRAO);
    }

    public static String normalizar(String imagem, String tamanho) {
        if (imagem == null || imagem.isBlank()) return null;
        if (imagem.startsWith("http://") || imagem.startsWith("https://")) return imagem;
        if (imagem.startsWith("/")) return URL_BASE_IMAGEM + tamanho + imagem;
        return URL_BASE_IMAGEM + tamanho + "/" + imagem;
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
