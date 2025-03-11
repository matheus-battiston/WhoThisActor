package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AtorMapper {
    public static Ator toEntity(AtorTMDBDto atorTMDBDto) {
        return Ator.builder()
                .nome(atorTMDBDto.getName())
                .imagem(atorTMDBDto.getProfile_path())
                .popularity(atorTMDBDto.getPopularity())
                .build();
    }

    public static OpcaoPesquisaElencoResponse toResponse(Ator ator, String personagem){
        return OpcaoPesquisaElencoResponse.builder()
                .nome(ator.getNome())
                .urlImagem(ator.getImagem())
                .nomePersonagem(personagem)
                .popularity(ator.getPopularity())
                .build();
    }
}
