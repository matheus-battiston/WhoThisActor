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

import static com.MovieParticipations.MovieParticipations.factories.AtorFactory.getKeanuReevesComId;
import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoTMDBDtoFactory.getMatrix;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdicionarFilmesDeAtorService")
class AdicionarFilmesDeAtorServiceTest {

    @Mock
    private FilmeResolverService filmeResolverService;

    @Mock
    private FilmeAtorVinculoService filmeAtorVinculoService;

    @InjectMocks
    private AdicionarFilmesDeAtorService adicionarFilmesDeAtorService;

    @Test
    @DisplayName("Deve resolver filmes e vincular ao ator")
    void deveResolverFilmesEVincularAoAtor() {
        Ator ator = getKeanuReevesComId();
        List<ProducaoTMDBDto> producoesDto = List.of(getMatrix());
        List<Filme> filmes = List.of(getMatrixComId());

        when(filmeResolverService.resolverFilme(producoesDto)).thenReturn(filmes);

        adicionarFilmesDeAtorService.adicionar(ator, producoesDto);

        verify(filmeResolverService).resolverFilme(producoesDto);
        verify(filmeAtorVinculoService).vincularAtorAFilme(ator, filmes, producoesDto);
    }

    @Test
    @DisplayName("Deve resolver filmes antes de vincular ao ator")
    void deveResolverFilmesAntesDeVincularAoAtor() {
        Ator ator = getKeanuReevesComId();
        List<ProducaoTMDBDto> producoesDto = List.of(getMatrix());
        List<Filme> filmes = List.of(getMatrixComId());

        when(filmeResolverService.resolverFilme(producoesDto)).thenReturn(filmes);

        adicionarFilmesDeAtorService.adicionar(ator, producoesDto);

        InOrder inOrder = inOrder(filmeResolverService, filmeAtorVinculoService);
        inOrder.verify(filmeResolverService).resolverFilme(producoesDto);
        inOrder.verify(filmeAtorVinculoService).vincularAtorAFilme(ator, filmes, producoesDto);
    }
}
