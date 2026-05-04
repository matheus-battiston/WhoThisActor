package com.MovieParticipations.MovieParticipations.validator;

import com.MovieParticipations.MovieParticipations.repository.FavoritaAtorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AtorNaoFavoritadoValidator {

    private final FavoritaAtorRepository favoritaAtorRepository;

    public boolean atorEstaFavoritado(Long usuarioId, Long atorId){
        return favoritaAtorRepository.existsByUsuarioIdAndAtorId(usuarioId, atorId);

    }
}
