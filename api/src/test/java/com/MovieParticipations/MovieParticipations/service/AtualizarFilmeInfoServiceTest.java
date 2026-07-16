package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.FilmeDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeDetalhesTMDBDtoFactory.getMatrixFilmeDetalhesTMDBDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarFilmeInfoService")
class AtualizarFilmeInfoServiceTest {
    private static final int INDICE_PRIMEIRO_GENERO = 0;

    @Mock
    private BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;

    @Mock
    private FilmeRepository filmeRepository;

    @InjectMocks
    private AtualizarFilmeInfoService service;

    @Test
    @DisplayName("Deve buscar detalhes por id TMDB, atualizar filme e salvar")
    void deveBuscarDetalhesPorIdTmdbAtualizarFilmeESalvar() {
        Filme filme = getMatrixFilmeEntityComId();
        filme.setInfoAtualizado(false);
        FilmeDetalhesTMDBDto detalhes = getMatrixFilmeDetalhesTMDBDto();

        when(buscarProducaoPorNomeTMBDService.buscarDetalhesFilmePorId(filme.getIdTmdb())).thenReturn(detalhes);

        service.atualizar(filme);

        assertThat(filme.getBackdropPath()).isEqualTo(detalhes.getBackdropPath());
        assertThat(filme.getDataLancamento()).isEqualTo(detalhes.getDataLancamento());
        assertThat(filme.getGenero()).isEqualTo(detalhes.getGenres().get(INDICE_PRIMEIRO_GENERO).getName());
        assertThat(filme.getOverview()).isEqualTo(detalhes.getOverview());
        assertThat(filme.getInfoAtualizado()).isTrue();
        verify(buscarProducaoPorNomeTMBDService).buscarDetalhesFilmePorId(filme.getIdTmdb());
        verify(filmeRepository).save(filme);
    }
}
