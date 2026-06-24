package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.repository.FavoritaAtorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ChecarAtorFavoritadoService {

    private final FavoritaAtorRepository favoritaAtorRepository;

    public Boolean estaFavoritadoPorAuthId(Long idAtor, Long idUsuario) {
        return favoritaAtorRepository.existsByAuthUserIdAndAtorId(idUsuario, idAtor);
    }

}
