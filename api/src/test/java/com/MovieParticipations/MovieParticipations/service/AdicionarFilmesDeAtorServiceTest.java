package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdicionarFilmesDeAtorService")
class AdicionarFilmesDeAtorServiceTest {

    @Mock
    private ObterOuCriarFilmesService obterOuCriarFilmesService;

    @Mock
    private CriarVinculosFilmeAtorService criarVinculosFilmeAtorService;

    @InjectMocks
    private AdicionarFilmesDeAtorService adicionarFilmesDeAtorService;

    @Test
    @DisplayName("Deve resolver filmes e vincular ao ator")
    void deveResolverFilmesEVincularAoAtor() {
        Ator ator = getKeanuReevesAtorEntityComId();
        List<ProducaoTMDBDto> producoesDto = List.of(getMatrixProducaoTMDBDto());
        List<Filme> filmes = List.of(getMatrixFilmeEntityComId());

        when(obterOuCriarFilmesService.obterOuCriar(producoesDto)).thenReturn(filmes);

        adicionarFilmesDeAtorService.adicionar(ator, producoesDto);

        verify(obterOuCriarFilmesService).obterOuCriar(producoesDto);
        verify(criarVinculosFilmeAtorService).criarVinculos(ator, filmes, producoesDto);
    }

    @Test
    @DisplayName("Deve resolver filmes antes de vincular ao ator")
    void deveResolverFilmesAntesDeVincularAoAtor() {
        Ator ator = getKeanuReevesAtorEntityComId();
        List<ProducaoTMDBDto> producoesDto = List.of(getMatrixProducaoTMDBDto());
        List<Filme> filmes = List.of(getMatrixFilmeEntityComId());

        when(obterOuCriarFilmesService.obterOuCriar(producoesDto)).thenReturn(filmes);

        adicionarFilmesDeAtorService.adicionar(ator, producoesDto);

        InOrder inOrder = inOrder(obterOuCriarFilmesService, criarVinculosFilmeAtorService);
        inOrder.verify(obterOuCriarFilmesService).obterOuCriar(producoesDto);
        inOrder.verify(criarVinculosFilmeAtorService).criarVinculos(ator, filmes, producoesDto);
    }
}
