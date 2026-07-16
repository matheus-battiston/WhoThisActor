package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.SerieDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieDetalhesTMDBDtoFactory.getBreakingBadSerieDetalhesTMDBDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarSerieInfoService")
class AtualizarSerieInfoServiceTest {
    private static final Integer ANO_PRIMEIRA_TEMPORADA_BREAKING_BAD = 2008;
    private static final Integer ANO_ULTIMA_TEMPORADA_BREAKING_BAD = 2013;
    private static final int INDICE_PRIMEIRO_GENERO = 0;

    @Mock
    private BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;

    @Mock
    private SerieRepository serieRepository;

    @InjectMocks
    private AtualizarSerieInfoService service;

    @Test
    @DisplayName("Deve buscar detalhes por id TMDB, atualizar serie e salvar")
    void deveBuscarDetalhesPorIdTmdbAtualizarSerieESalvar() {
        Serie serie = getBreakingBadSerieEntityComId();
        serie.setInfoAtualizado(false);
        SerieDetalhesTMDBDto detalhes = getBreakingBadSerieDetalhesTMDBDto();

        when(buscarProducaoPorNomeTMBDService.buscarDetalhesSeriePorId(serie.getIdTmdb())).thenReturn(detalhes);

        service.atualizar(serie);

        assertThat(serie.getBackdropPath()).isEqualTo(detalhes.getBackdropPath());
        assertThat(serie.getAnoPrimeiraTemporada()).isEqualTo(ANO_PRIMEIRA_TEMPORADA_BREAKING_BAD);
        assertThat(serie.getAnoUltimaTemporada()).isEqualTo(ANO_ULTIMA_TEMPORADA_BREAKING_BAD);
        assertThat(serie.getQuantidadeTemporadas()).isEqualTo(detalhes.getQuantidadeTemporadas());
        assertThat(serie.getGenero()).isEqualTo(detalhes.getGenres().get(INDICE_PRIMEIRO_GENERO).getName());
        assertThat(serie.getOverview()).isEqualTo(detalhes.getOverview());
        assertThat(serie.getInfoAtualizado()).isTrue();
        verify(buscarProducaoPorNomeTMBDService).buscarDetalhesSeriePorId(serie.getIdTmdb());
        verify(serieRepository).save(serie);
    }
}
