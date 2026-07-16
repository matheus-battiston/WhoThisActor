package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioFactory.getUsuarioEntityComId;
import static com.MovieParticipations.MovieParticipations.mapper.FavoritaAtorMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FavoritaAtorMapperTest {
    private static final Long ID_USUARIO = 1L;
    private static final String LINK_IMAGEM = "https://image.tmdb.org/t/p/w400";

    @Test
    @DisplayName("Deve transformar ator e usuario em entidade")
    void transformarEmEntidade() {
        Ator ator = getKeanuReevesAtorEntityComId();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        FavoritaAtor response = toEntity(ator, usuario);

        assertEquals(ator, response.getAtor());
        assertEquals(usuario, response.getUsuario());
    }

    @Test
    @DisplayName("Deve transformar ator em response")
    void transformarEmResponse() {
        Ator ator = getKeanuReevesAtorEntityComId();

        InfoAtorResponse response = toResponse(ator);

        assertEquals(ator.getId(), response.getId());
        assertEquals(ator.getNome(), response.getNome());
        assertEquals(LINK_IMAGEM + ator.getImagem(), response.getUrlImagem());
    }
}
