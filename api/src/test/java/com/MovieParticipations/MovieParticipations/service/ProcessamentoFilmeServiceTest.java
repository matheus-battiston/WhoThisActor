package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.FilmeAtor;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
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

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBMovieDtoFactory.getKeanuReevesAtorTMDBMovieDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProcessamentoFilmeService")
class ProcessamentoFilmeServiceTest {

    private static final Long ID_TMDB_KEANU = 6384L;

    @Mock
    private FilmeAtorRepository filmeAtorRepository;

    @Mock
    private ObterOuCriarAtoresService obterOuCriarAtoresService;

    @Captor
    private ArgumentCaptor<List<FilmeAtor>> elencoCaptor;

    @InjectMocks
    private ProcessamentoFilmeService service;

    @Test
    @DisplayName("Deve rejeitar filme sem elenco")
    void deveRejeitarFilmeSemElenco() {
        Filme filme = getMatrixFilmeEntityComId();

        assertThatThrownBy(() -> service.processarElenco(filme, List.of()))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode").isEqualTo(BAD_REQUEST);

        verifyNoInteractions(filmeAtorRepository, obterOuCriarAtoresService);
    }

    @Test
    @DisplayName("Deve salvar os vínculos para atores ainda não relacionados")
    void deveSalvarVinculosParaAtoresAindaNaoRelacionados() {
        Filme filme = getMatrixFilmeEntityComId();
        AtorTMDBMovieDto atorDto = getKeanuReevesAtorTMDBMovieDto();
        Ator ator = getKeanuReevesAtorEntityComId();

        when(obterOuCriarAtoresService.obter(List.of(atorDto))).thenReturn(List.of(ator));
        when(filmeAtorRepository.findAtorIdsTmdbByProducaoId(filme.getId())).thenReturn(List.of());

        service.processarElenco(filme, List.of(atorDto));

        verify(filmeAtorRepository).saveAll(elencoCaptor.capture());
        FilmeAtor vinculo = elencoCaptor.getValue().get(0);
        assertThat(vinculo.getFilme()).isSameAs(filme);
        assertThat(vinculo.getAtor()).isSameAs(ator);
        assertThat(vinculo.getPersonagem()).isEqualTo("Neo");
        assertThat(vinculo.getCreditOrder()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Não deve salvar vínculo já existente")
    void naoDeveSalvarVinculoJaExistente() {
        Filme filme = getMatrixFilmeEntityComId();
        AtorTMDBMovieDto atorDto = getKeanuReevesAtorTMDBMovieDto();
        Ator ator = getKeanuReevesAtorEntityComId();

        when(obterOuCriarAtoresService.obter(List.of(atorDto))).thenReturn(List.of(ator));
        when(filmeAtorRepository.findAtorIdsTmdbByProducaoId(filme.getId())).thenReturn(List.of(ID_TMDB_KEANU));

        service.processarElenco(filme, List.of(atorDto));

        verify(filmeAtorRepository).findAtorIdsTmdbByProducaoId(filme.getId());
        verifyNoMoreInteractions(filmeAtorRepository);
    }
}
