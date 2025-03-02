package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencResponseo;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDto;
import lombok.*;

@Builder
@Getter
@Setter
public class AtorMapper {
    public static Ator toEntity(AtorTMDBDto atorTMDBDto) {
        return Ator.builder()
                .nome(atorTMDBDto.getName())
                .imagem(atorTMDBDto.getProfile_path())
                .build();
    }

    public static OpcaoPesquisaElencResponseo toResponse(Ator ator, String personagem){
        return OpcaoPesquisaElencResponseo.builder()
                .nome(ator.getNome())
                .urlImagem(ator.getImagem())
                .nomePersonagem(personagem)
                .build();
    }
}
