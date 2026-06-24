package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDto;
import com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl;

import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;
import static java.time.LocalDate.now;

public final class AtorMapper {

    private AtorMapper() {
    }

    public static Ator toEntity(AtorTMDBDto ator) {
        return criarAtor(
                ator.getNome(),
                TmdbImagemUrl.caminhoRelativo(ator.getFotoDePerfil()),
                ator.getPopularidade(),
                ator.getId()
        );
    }

    public static Ator toEntityFromOpcoesParecidos(OpcoesAtoresParecidosResponse ator) {
        return criarAtor(
                ator.getIdentidade(),
                TmdbImagemUrl.caminhoRelativo(ator.getImagem()),
                ator.getPopularidade(),
                ator.getIdTmdb()
        );
    }

    public static InfoAtorResponse toInfo(Ator ator) {
        return InfoAtorResponse.builder()
                .nome(ator.getNome())
                .urlImagem(TmdbImagemUrl.normalizar(ator.getImagem()))
                .id(ator.getId())
                .popularity(ator.getPopularity())
                .build();
    }

    private static Ator criarAtor(String nome, String imagem, Double popularity, Long idTmdb) {
        return Ator.builder()
                .nome(nome)
                .nomeNormalizado(normalizar(nome))
                .imagem(imagem)
                .popularity(popularity)
                .idTmdb(idTmdb)
                .inicializado(false)
                .ultimaAtualizacao(now())
                .build();
    }
}
