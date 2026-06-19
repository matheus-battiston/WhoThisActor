package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.domain.FavoritaFilme;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.UsuarioFactory.getUsuarioEntityComId;
import static com.MovieParticipations.MovieParticipations.mapper.FavoritaFilmeMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FavoritaFilmeMapperTest {
    private static final Long ID_USUARIO = 1L;

    @Test
    @DisplayName("Deve transformar filme e usuario em entidade")
    void transformarEmEntidade() {
        Filme filme = getMatrixFilmeEntityComId();
        Usuario usuario = getUsuarioEntityComId(ID_USUARIO);

        FavoritaFilme response = toEntity(filme, usuario);

        assertEquals(filme, response.getFilme());
        assertEquals(usuario, response.getUsuario());
    }
}
