package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.AtorFactory.getKeanuReevesComId;
import static com.MovieParticipations.MovieParticipations.factories.UsuarioFactory.get;

@ExtendWith(MockitoExtension.class)
public class FavoritaAtorMapperTest {

    @Test
    @DisplayName("Deve transformar ator e usuario em entidade")
    void transformarEmEntidade() {
        Ator ator = getKeanuReevesComId();
        Usuario usuario = get(1L);

        FavoritaAtor response = FavoritaAtorMapper.toEntity(ator, usuario);

        Assertions.assertEquals(ator, response.getAtor());
        Assertions.assertEquals(usuario, response.getUsuario());
    }

    @Test
    @DisplayName("Deve transformar ator em response")
    void transformarEmResponse() {
        Ator ator = getKeanuReevesComId();

        InfoAtorResponse response = FavoritaAtorMapper.toResponse(ator);

        Assertions.assertEquals(ator.getId(), response.getId());
        Assertions.assertEquals(ator.getNome(), response.getNome());
        Assertions.assertEquals("https://image.tmdb.org/t/p/w200" + ator.getImagem(), response.getUrlImagem());
    }
}
