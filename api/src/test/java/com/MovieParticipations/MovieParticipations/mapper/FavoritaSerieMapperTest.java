package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;
import static com.MovieParticipations.MovieParticipations.factories.UsuarioFactory.get;
import static com.MovieParticipations.MovieParticipations.mapper.FavoritaSerieMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FavoritaSerieMapperTest {

    @Test
    @DisplayName("Deve transformar serie e usuario em entidade")
    void transformarEmEntidade() {
        Serie serie = getBreakingBadComId();
        Usuario usuario = get(1L);

        FavoritaSerie response = toEntity(serie, usuario);

        assertEquals(serie, response.getSerie());
        assertEquals(usuario, response.getUsuario());
    }
}
