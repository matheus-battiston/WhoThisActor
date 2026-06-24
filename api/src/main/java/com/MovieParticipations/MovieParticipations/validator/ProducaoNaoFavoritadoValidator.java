package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.FavoritaFilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.FavoritaSerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProducaoNaoFavoritadoValidator {
    private final FavoritaSerieRepository favoritaSerieRepository;
    private final FavoritaFilmeRepository favoritaFilmeRepository;

    public boolean serieEstaFavoritada(Long usuarioId, Long serieId){
        return favoritaSerieRepository.existsByUsuarioIdAndSerieId(usuarioId, serieId);
    }

    public boolean filmeEstaFavoritado(Long usuarioId, Long filmeId) {
        return favoritaFilmeRepository.existsByUsuarioIdAndFilmeId(usuarioId, filmeId);
    }
}
