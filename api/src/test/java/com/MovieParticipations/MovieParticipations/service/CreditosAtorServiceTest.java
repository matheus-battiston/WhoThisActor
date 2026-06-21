package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.ListaProducoesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ListaProducoesTMDBDtoFactory.getListaProducoesTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getBreakingBadProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getProducaoSemTituloENome;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreditosAtorService")
class CreditosAtorServiceTest {
    private static final Long ID_TMDB_KEANU_REEVES = 6384L;

    @Mock
    private RequisicaoApiService requisicaoApiService;

    @InjectMocks
    private CreditosAtorService creditosAtorService;

    @Test
    @DisplayName("Deve buscar creditos pelo id TMDB do ator e manter filmes e series validos")
    void deveBuscarCreditosPeloIdTmdbDoAtorEManterFilmesESeriesValidos() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ProducaoTMDBDto filme = getMatrixProducaoTMDBDto();
        ProducaoTMDBDto serie = getBreakingBadProducaoTMDBDto();
        ProducaoTMDBDto producaoSemTituloENome = getProducaoSemTituloENome();
        ListaProducoesTMDBDto response = getListaProducoesTMDBDto(
                List.of(filme, serie, producaoSemTituloENome)
        );

        when(requisicaoApiService.pesquisarFilmesDoAtorPorId(ID_TMDB_KEANU_REEVES)).thenReturn(response);

        List<ProducaoTMDBDto> resultado = creditosAtorService.buscarCreditosValidos(ator);

        assertThat(resultado).containsExactly(filme, serie);
        verify(requisicaoApiService).pesquisarFilmesDoAtorPorId(ID_TMDB_KEANU_REEVES);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum credito tiver titulo ou nome")
    void deveRetornarListaVaziaQuandoNenhumCreditoTiverTituloOuNome() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ListaProducoesTMDBDto response = getListaProducoesTMDBDto(List.of(getProducaoSemTituloENome()));

        when(requisicaoApiService.pesquisarFilmesDoAtorPorId(ID_TMDB_KEANU_REEVES)).thenReturn(response);

        List<ProducaoTMDBDto> resultado = creditosAtorService.buscarCreditosValidos(ator);

        assertThat(resultado).isEmpty();
        verify(requisicaoApiService).pesquisarFilmesDoAtorPorId(ID_TMDB_KEANU_REEVES);
    }
}
