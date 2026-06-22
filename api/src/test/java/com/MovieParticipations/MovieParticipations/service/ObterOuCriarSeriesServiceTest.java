package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getBreakingBadProducaoTMDBDto;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterOuCriarSeriesService")
class ObterOuCriarSeriesServiceTest {

    private static final Long ID_SERIE_NOVA = 3L;
    private static final Long ID_SERIE_DUPLICADA = 4L;
    private static final Long ID_TMDB_SERIE_NOVA = 789L;
    private static final Long ID_TMDB_SERIE_INCONSISTENTE = 999L;
    private static final String NOME_SERIE_NOVA = "Série Nova";
    private static final String NOME_SERIE_ALTERNATIVO = "Série Alternativa";
    private static final String NOME_EM_BRANCO = "   ";
    private static final int PRIMEIRA_SERIE = 0;
    private static final int QUANTIDADE_UMA_SERIE = 1;
    private static final int QUANTIDADE_DUAS_SERIES = 2;

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private AdicionarSerieService adicionarSerieService;

    @Captor
    private ArgumentCaptor<List<ProducaoTMDBDto>> creditosCaptor;

    @InjectMocks
    private ObterOuCriarSeriesService service;

    @Test
    @DisplayName("Deve retornar séries existentes e criadas na ordem dos créditos")
    void deveRetornarSeriesExistentesECriadasNaOrdemDosCreditos() {
        ProducaoTMDBDto creditoSerieNova = criarCredito(ID_TMDB_SERIE_NOVA, NOME_SERIE_NOVA);
        ProducaoTMDBDto creditoSerieExistente = getBreakingBadProducaoTMDBDto();
        Serie serieExistente = getBreakingBadSerieEntityComId();
        Serie serieCriada = getSerieEntityComId(ID_SERIE_NOVA);
        serieCriada.setIdTmdb(ID_TMDB_SERIE_NOVA);

        when(serieRepository.findByIdTmdbIn(of(ID_TMDB_SERIE_NOVA, creditoSerieExistente.getId())))
                .thenReturn(of(serieExistente));
        when(adicionarSerieService.adicionarSeries(of(creditoSerieNova))).thenReturn(of(serieCriada));

        List<Serie> resultado = service.obterOuCriar(of(creditoSerieNova, creditoSerieExistente));

        assertEquals(QUANTIDADE_DUAS_SERIES, resultado.size());
        assertEquals(serieCriada, resultado.get(PRIMEIRA_SERIE));
        assertEquals(serieExistente, resultado.get(QUANTIDADE_UMA_SERIE));
    }

    @Test
    @DisplayName("Deve escolher crédito com nome válido quando primeiro duplicado não tiver nome")
    void deveEscolherCreditoComNomeValidoQuandoPrimeiroDuplicadoNaoTiverNome() {
        ProducaoTMDBDto creditoSemNome = criarCredito(ID_TMDB_SERIE_NOVA, NOME_EM_BRANCO);
        ProducaoTMDBDto creditoComNome = criarCredito(ID_TMDB_SERIE_NOVA, NOME_SERIE_NOVA);
        Serie serieCriada = getSerieEntityComId(ID_SERIE_NOVA);
        serieCriada.setIdTmdb(ID_TMDB_SERIE_NOVA);

        when(serieRepository.findByIdTmdbIn(of(ID_TMDB_SERIE_NOVA))).thenReturn(of());
        when(adicionarSerieService.adicionarSeries(of(creditoComNome))).thenReturn(of(serieCriada));

        List<Serie> resultado = service.obterOuCriar(of(creditoSemNome, creditoComNome));

        verify(adicionarSerieService).adicionarSeries(creditosCaptor.capture());
        assertEquals(QUANTIDADE_UMA_SERIE, creditosCaptor.getValue().size());
        assertEquals(creditoComNome, creditosCaptor.getValue().get(PRIMEIRA_SERIE));
        assertEquals(of(serieCriada), resultado);
    }

    @Test
    @DisplayName("Deve escolher primeiro crédito quando duplicados tiverem nome válido")
    void deveEscolherPrimeiroCreditoQuandoDuplicadosTiveremNomeValido() {
        ProducaoTMDBDto primeiroCredito = criarCredito(ID_TMDB_SERIE_NOVA, NOME_SERIE_NOVA);
        ProducaoTMDBDto creditoDuplicado = criarCredito(ID_TMDB_SERIE_NOVA, NOME_SERIE_ALTERNATIVO);
        Serie serieCriada = getSerieEntityComId(ID_SERIE_NOVA);
        serieCriada.setIdTmdb(ID_TMDB_SERIE_NOVA);

        when(serieRepository.findByIdTmdbIn(of(ID_TMDB_SERIE_NOVA))).thenReturn(of());
        when(adicionarSerieService.adicionarSeries(of(primeiroCredito))).thenReturn(of(serieCriada));

        service.obterOuCriar(of(primeiroCredito, creditoDuplicado));

        verify(adicionarSerieService).adicionarSeries(creditosCaptor.capture());
        assertEquals(QUANTIDADE_UMA_SERIE, creditosCaptor.getValue().size());
        assertEquals(primeiroCredito, creditosCaptor.getValue().get(PRIMEIRA_SERIE));
    }

    @Test
    @DisplayName("Deve retornar série existente sem criar novas séries")
    void deveRetornarSerieExistenteSemCriarNovasSeries() {
        ProducaoTMDBDto credito = getBreakingBadProducaoTMDBDto();
        Serie serieExistente = getBreakingBadSerieEntityComId();

        when(serieRepository.findByIdTmdbIn(of(credito.getId()))).thenReturn(of(serieExistente));

        List<Serie> resultado = service.obterOuCriar(of(credito));

        assertEquals(of(serieExistente), resultado);
        verify(serieRepository).findByIdTmdbIn(of(credito.getId()));
        verifyNoMoreInteractions(serieRepository);
        verifyNoInteractions(adicionarSerieService);
    }

    @Test
    @DisplayName("Deve retornar série existente mesmo quando crédito não tiver nome válido")
    void deveRetornarSerieExistenteMesmoQuandoCreditoNaoTiverNomeValido() {
        ProducaoTMDBDto creditoSemNome = criarCredito(ID_TMDB_SERIE_NOVA, NOME_EM_BRANCO);
        Serie serieExistente = getSerieEntityComId(ID_SERIE_NOVA);
        serieExistente.setIdTmdb(ID_TMDB_SERIE_NOVA);

        when(serieRepository.findByIdTmdbIn(of(ID_TMDB_SERIE_NOVA))).thenReturn(of(serieExistente));

        List<Serie> resultado = service.obterOuCriar(of(creditoSemNome));

        assertEquals(of(serieExistente), resultado);
        verifyNoInteractions(adicionarSerieService);
    }

    @Test
    @DisplayName("Deve usar primeira série quando repositório retornar IDs TMDB duplicados")
    void deveUsarPrimeiraSerieQuandoRepositorioRetornarIdsTmdbDuplicados() {
        ProducaoTMDBDto credito = getBreakingBadProducaoTMDBDto();
        Serie primeiraSerie = getBreakingBadSerieEntityComId();
        Serie serieDuplicada = getSerieEntityComId(ID_SERIE_DUPLICADA);
        serieDuplicada.setIdTmdb(credito.getId());

        when(serieRepository.findByIdTmdbIn(of(credito.getId()))).thenReturn(of(primeiraSerie, serieDuplicada));

        List<Serie> resultado = service.obterOuCriar(of(credito));

        assertEquals(of(primeiraSerie), resultado);
        verifyNoInteractions(adicionarSerieService);
    }

    @Test
    @DisplayName("Deve ignorar série criada com ID TMDB diferente do crédito solicitado")
    void deveIgnorarSerieCriadaComIdTmdbDiferenteDoCreditoSolicitado() {
        ProducaoTMDBDto creditoSerieExistente = getBreakingBadProducaoTMDBDto();
        ProducaoTMDBDto creditoSerieNova = criarCredito(ID_TMDB_SERIE_NOVA, NOME_SERIE_NOVA);
        Serie serieExistente = getBreakingBadSerieEntityComId();
        Serie serieInconsistente = getSerieEntityComId(ID_SERIE_NOVA);
        serieInconsistente.setIdTmdb(ID_TMDB_SERIE_INCONSISTENTE);

        when(serieRepository.findByIdTmdbIn(of(creditoSerieExistente.getId(), ID_TMDB_SERIE_NOVA)))
                .thenReturn(of(serieExistente));
        when(adicionarSerieService.adicionarSeries(of(creditoSerieNova))).thenReturn(of(serieInconsistente));

        List<Serie> resultado = service.obterOuCriar(of(creditoSerieExistente, creditoSerieNova));

        assertEquals(of(serieExistente), resultado);
        verify(adicionarSerieService).adicionarSeries(of(creditoSerieNova));
    }

    @Test
    @DisplayName("Deve ignorar crédito novo quando criação não retornar série correspondente")
    void deveIgnorarCreditoNovoQuandoCriacaoNaoRetornarSerieCorrespondente() {
        ProducaoTMDBDto creditoSerieExistente = getBreakingBadProducaoTMDBDto();
        ProducaoTMDBDto creditoSerieNova = criarCredito(ID_TMDB_SERIE_NOVA, NOME_SERIE_NOVA);
        Serie serieExistente = getBreakingBadSerieEntityComId();

        when(serieRepository.findByIdTmdbIn(of(creditoSerieExistente.getId(), ID_TMDB_SERIE_NOVA)))
                .thenReturn(of(serieExistente));
        when(adicionarSerieService.adicionarSeries(of(creditoSerieNova))).thenReturn(of());

        List<Serie> resultado = service.obterOuCriar(of(creditoSerieExistente, creditoSerieNova));

        assertEquals(of(serieExistente), resultado);
        verify(adicionarSerieService).adicionarSeries(of(creditoSerieNova));
    }

    @Test
    @DisplayName("Não deve consultar banco quando lista de créditos estiver vazia")
    void naoDeveConsultarBancoQuandoListaDeCreditosEstiverVazia() {
        List<Serie> resultado = service.obterOuCriar(of());

        assertEquals(of(), resultado);
        verifyNoInteractions(serieRepository, adicionarSerieService);
    }

    @Test
    @DisplayName("Não deve consultar banco para crédito sem ID TMDB")
    void naoDeveConsultarBancoParaCreditoSemIdTmdb() {
        ProducaoTMDBDto creditoSemId = criarCredito(null, NOME_SERIE_NOVA);

        List<Serie> resultado = service.obterOuCriar(of(creditoSemId));

        assertEquals(of(), resultado);
        verifyNoInteractions(serieRepository, adicionarSerieService);
    }

    @Test
    @DisplayName("Não deve criar série para crédito com nome em branco")
    void naoDeveCriarSerieParaCreditoComNomeEmBranco() {
        ProducaoTMDBDto creditoSemNome = criarCredito(ID_TMDB_SERIE_NOVA, NOME_EM_BRANCO);

        when(serieRepository.findByIdTmdbIn(of(ID_TMDB_SERIE_NOVA))).thenReturn(of());

        List<Serie> resultado = service.obterOuCriar(of(creditoSemNome));

        assertEquals(of(), resultado);
        verify(serieRepository).findByIdTmdbIn(of(ID_TMDB_SERIE_NOVA));
        verifyNoMoreInteractions(serieRepository);
        verifyNoInteractions(adicionarSerieService);
    }

    @Test
    @DisplayName("Não deve criar série para crédito sem nome")
    void naoDeveCriarSerieParaCreditoSemNome() {
        ProducaoTMDBDto creditoSemNome = criarCredito(ID_TMDB_SERIE_NOVA, null);

        when(serieRepository.findByIdTmdbIn(of(ID_TMDB_SERIE_NOVA))).thenReturn(of());

        List<Serie> resultado = service.obterOuCriar(of(creditoSemNome));

        assertEquals(of(), resultado);
        verify(serieRepository).findByIdTmdbIn(of(ID_TMDB_SERIE_NOVA));
        verifyNoMoreInteractions(serieRepository);
        verifyNoInteractions(adicionarSerieService);
    }

    private ProducaoTMDBDto criarCredito(Long idTmdb, String nome) {
        return ProducaoTMDBDto.builder()
                .id(idTmdb)
                .nome(nome)
                .build();
    }
}
