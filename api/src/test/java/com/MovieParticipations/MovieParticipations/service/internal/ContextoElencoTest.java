package com.MovieParticipations.MovieParticipations.service.internal;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getBryanCranstonAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ContextoElenco")
class ContextoElencoTest {

    private static final Long ID_TMDB_KEANU_REEVES = 6384L;
    private static final Long ID_TMDB_BRYAN_CRANSTON = 17419L;
    private static final Long ID_TMDB_INEXISTENTE = 999L;

    @Test
    @DisplayName("Deve indexar atores por ID TMDB e identificar vínculos existentes")
    void deveIndexarAtoresPorIdTmdbEIdentificarVinculosExistentes() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        Ator bryan = getBryanCranstonAtorEntityComId();

        ContextoElenco contexto = ContextoElenco.criar(
                of(keanu, bryan),
                of(ID_TMDB_KEANU_REEVES)
        );

        assertThat(contexto.atorPorIdTmdb())
                .containsEntry(ID_TMDB_KEANU_REEVES, keanu)
                .containsEntry(ID_TMDB_BRYAN_CRANSTON, bryan);
        assertThat(contexto.buscarAtor(ID_TMDB_KEANU_REEVES)).isSameAs(keanu);
        assertThat(contexto.atorJaRelacionado(ID_TMDB_KEANU_REEVES)).isTrue();
        assertThat(contexto.atorJaRelacionado(ID_TMDB_BRYAN_CRANSTON)).isFalse();
    }

    @Test
    @DisplayName("Deve retornar nulo para ator que não fizer parte do contexto")
    void deveRetornarNuloParaAtorQueNaoFizerParteDoContexto() {
        ContextoElenco contexto = ContextoElenco.criar(of(), of());

        assertThat(contexto.buscarAtor(ID_TMDB_INEXISTENTE)).isNull();
        assertThat(contexto.atorJaRelacionado(ID_TMDB_INEXISTENTE)).isFalse();
    }
}
