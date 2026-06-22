package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getBryanCranstonAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBSerieDtoFactory.getBryanCranstonAtorTMDBSerieDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProcessamentoSerieService")
class ProcessamentoSerieServiceTest {

    private static final Long ID_TMDB_BRYAN = 17419L;

    @Mock
    private SerieAtorRepository serieAtorRepository;

    @Mock
    private ObterOuCriarAtoresService obterOuCriarAtoresService;

    @Captor
    private ArgumentCaptor<List<SerieAtor>> elencoCaptor;

    @InjectMocks
    private ProcessamentoSerieService service;

    @Test
    @DisplayName("Deve rejeitar série sem elenco")
    void deveRejeitarSerieSemElenco() {
        Serie serie = getBreakingBadSerieEntityComId();

        assertThatThrownBy(() -> service.processarElenco(serie, List.of()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode").isEqualTo(BAD_REQUEST);

        verifyNoInteractions(serieAtorRepository, obterOuCriarAtoresService);
    }

    @Test
    @DisplayName("Deve salvar personagem e episódios do ator ainda não relacionado")
    void deveSalvarPersonagemEEpisodiosDoAtorAindaNaoRelacionado() {
        Serie serie = getBreakingBadSerieEntityComId();
        AtorTMDBSerieDto atorDto = getBryanCranstonAtorTMDBSerieDto();
        Ator ator = getBryanCranstonAtorEntityComId();

        when(obterOuCriarAtoresService.obter(List.of(atorDto))).thenReturn(List.of(ator));
        when(serieAtorRepository.findAtorIdsTmdbByProducaoId(serie.getId())).thenReturn(List.of());

        service.processarElenco(serie, List.of(atorDto));

        verify(serieAtorRepository).saveAll(elencoCaptor.capture());
        SerieAtor vinculo = elencoCaptor.getValue().get(0);
        assertThat(vinculo.getSerie()).isSameAs(serie);
        assertThat(vinculo.getAtor()).isSameAs(ator);
        assertThat(vinculo.getPersonagem()).isEqualTo("Walter White");
        assertThat(vinculo.getNumeroEpisodios()).isEqualTo(62);
    }

    @Test
    @DisplayName("Não deve salvar ator já relacionado à série")
    void naoDeveSalvarAtorJaRelacionadoASerie() {
        Serie serie = getBreakingBadSerieEntityComId();
        AtorTMDBSerieDto atorDto = getBryanCranstonAtorTMDBSerieDto();
        Ator ator = getBryanCranstonAtorEntityComId();

        when(obterOuCriarAtoresService.obter(List.of(atorDto))).thenReturn(List.of(ator));
        when(serieAtorRepository.findAtorIdsTmdbByProducaoId(serie.getId())).thenReturn(List.of(ID_TMDB_BRYAN));

        service.processarElenco(serie, List.of(atorDto));

        verify(serieAtorRepository).findAtorIdsTmdbByProducaoId(serie.getId());
        verifyNoMoreInteractions(serieAtorRepository);
    }
}
