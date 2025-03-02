package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.domain.TipoProducao;
import com.google.gson.JsonObject;

import static com.MovieParticipations.MovieParticipations.domain.TipoProducao.*;

public class FilmeMapper {
    private static final String VALOR_TV = "tv";
    private static final String VALOR_FILME = "movie";
    private static final String PARAMETRO_TITULO_FILME = "title";
    private static final String PARAMETRO_PERSONAGEM = "character";
    private static final String PARAMETRO_POSTER = "poster_path";
    private static final String PARAMETRO_POPULARIDADE = "popularity";
    private static final String PARAMETRO_TIPO_PRODUCAO = "media_type";
    private static final String PARAMETRO_NOME_TV = "original_name";
    public static ProducaoResponse toResponse(JsonObject producao){
        String posterLink = getValorDaString(producao, PARAMETRO_POSTER);
        String nomePesonagem = getValorDaString(producao, PARAMETRO_PERSONAGEM);
        Float popularidade = getValorFloat(producao, PARAMETRO_POPULARIDADE);
        String stringTipoProducao = getValorDaString(producao, PARAMETRO_TIPO_PRODUCAO);
        assert stringTipoProducao != null;
        TipoProducao tipoProducao = definirTipoProducao(stringTipoProducao);
        String nomeProducao = tipoProducao.equals(TV) ?
                getValorDaString(producao, PARAMETRO_NOME_TV)
                : getValorDaString(producao, PARAMETRO_TITULO_FILME) ;
        int id = producao.get("id").getAsInt();


        return ProducaoResponse.builder()
                .nomeProducao(nomeProducao)
                .nomePersonagem(nomePesonagem)
                .posterLink(posterLink)
                .popularidade(popularidade)
                .tipoProducao(tipoProducao)
                .id(id)
                .build();
    }

    private static String getValorDaString(JsonObject objeto, String parametro){
        return (objeto.has(parametro) && !objeto.get(parametro).isJsonNull())
                ? objeto.get(parametro).getAsString()
                : null;
    }

    private static Float getValorFloat(JsonObject objeto, String parametro){
        return (objeto.has(parametro) && !objeto.get(parametro).isJsonNull())
                ? objeto.get(parametro).getAsFloat()
                : null;
    }
    private static TipoProducao definirTipoProducao(String tipoProducao){
        if (tipoProducao.equals(VALOR_FILME)) {
            return FILME;
        }
        else return TV;
    }
}