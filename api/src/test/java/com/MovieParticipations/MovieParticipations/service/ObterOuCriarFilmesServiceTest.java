package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterOuCriarFilmesService")
class ObterOuCriarFilmesServiceTest {

    private static final Long ID_FILME_NOVO = 2L;
    private static final Long ID_FILME_DUPLICADO = 3L;
    private static final Long ID_TMDB_FILME_NOVO = 456L;
    private static final Long ID_TMDB_FILME_INCONSISTENTE = 789L;
    private static final String TITULO_FILME_NOVO = "Filme Novo";
    private static final String TITULO_FILME_ALTERNATIVO = "Filme Alternativo";
    private static final String TITULO_EM_BRANCO = "   ";
    private static final int PRIMEIRO_FILME = 0;
    private static final int QUANTIDADE_UM_FILME = 1;
    private static final int QUANTIDADE_DOIS_FILMES = 2;

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private AdicionarFilmeService adicionarFilmeService;

    @Captor
    private ArgumentCaptor<List<ProducaoTMDBDto>> creditosCaptor;

    @InjectMocks
    private ObterOuCriarFilmesService service;

    @Test
    @DisplayName("Deve retornar filmes existentes e criados na ordem dos créditos")
    void deveRetornarFilmesExistentesECriadosNaOrdemDosCreditos() {
        ProducaoTMDBDto creditoFilmeNovo = criarCredito(ID_TMDB_FILME_NOVO, TITULO_FILME_NOVO);
        ProducaoTMDBDto creditoFilmeExistente = getMatrixProducaoTMDBDto();
        Filme filmeExistente = getMatrixFilmeEntityComId();
        Filme filmeCriado = getFilmeEntityComId(ID_FILME_NOVO);
        filmeCriado.setIdTmdb(ID_TMDB_FILME_NOVO);

        when(filmeRepository.findByIdTmdbIn(of(ID_TMDB_FILME_NOVO, creditoFilmeExistente.getId())))
                .thenReturn(of(filmeExistente));
        when(adicionarFilmeService.adicionarFilmes(of(creditoFilmeNovo))).thenReturn(of(filmeCriado));

        List<Filme> resultado = service.obterOuCriar(of(creditoFilmeNovo, creditoFilmeExistente));

        assertEquals(QUANTIDADE_DOIS_FILMES, resultado.size());
        assertEquals(filmeCriado, resultado.get(PRIMEIRO_FILME));
        assertEquals(filmeExistente, resultado.get(QUANTIDADE_UM_FILME));
    }

    @Test
    @DisplayName("Deve escolher crédito com título válido quando primeiro duplicado não tiver título")
    void deveEscolherCreditoComTituloValidoQuandoPrimeiroDuplicadoNaoTiverTitulo() {
        ProducaoTMDBDto creditoSemTitulo = criarCredito(ID_TMDB_FILME_NOVO, TITULO_EM_BRANCO);
        ProducaoTMDBDto creditoComTitulo = criarCredito(ID_TMDB_FILME_NOVO, TITULO_FILME_NOVO);
        Filme filmeCriado = getFilmeEntityComId(ID_FILME_NOVO);
        filmeCriado.setIdTmdb(ID_TMDB_FILME_NOVO);

        when(filmeRepository.findByIdTmdbIn(of(ID_TMDB_FILME_NOVO))).thenReturn(of());
        when(adicionarFilmeService.adicionarFilmes(of(creditoComTitulo))).thenReturn(of(filmeCriado));

        List<Filme> resultado = service.obterOuCriar(of(creditoSemTitulo, creditoComTitulo));

        verify(adicionarFilmeService).adicionarFilmes(creditosCaptor.capture());
        assertEquals(QUANTIDADE_UM_FILME, creditosCaptor.getValue().size());
        assertEquals(creditoComTitulo, creditosCaptor.getValue().get(PRIMEIRO_FILME));
        assertEquals(of(filmeCriado), resultado);
    }

    @Test
    @DisplayName("Deve escolher primeiro crédito quando duplicados tiverem título válido")
    void deveEscolherPrimeiroCreditoQuandoDuplicadosTiveremTituloValido() {
        ProducaoTMDBDto primeiroCredito = criarCredito(ID_TMDB_FILME_NOVO, TITULO_FILME_NOVO);
        ProducaoTMDBDto creditoDuplicado = criarCredito(ID_TMDB_FILME_NOVO, TITULO_FILME_ALTERNATIVO);
        Filme filmeCriado = getFilmeEntityComId(ID_FILME_NOVO);
        filmeCriado.setIdTmdb(ID_TMDB_FILME_NOVO);

        when(filmeRepository.findByIdTmdbIn(of(ID_TMDB_FILME_NOVO))).thenReturn(of());
        when(adicionarFilmeService.adicionarFilmes(of(primeiroCredito))).thenReturn(of(filmeCriado));

        service.obterOuCriar(of(primeiroCredito, creditoDuplicado));

        verify(adicionarFilmeService).adicionarFilmes(creditosCaptor.capture());
        assertEquals(QUANTIDADE_UM_FILME, creditosCaptor.getValue().size());
        assertEquals(primeiroCredito, creditosCaptor.getValue().get(PRIMEIRO_FILME));
    }

    @Test
    @DisplayName("Deve retornar filme existente sem criar novos filmes")
    void deveRetornarFilmeExistenteSemCriarNovosFilmes() {
        ProducaoTMDBDto credito = getMatrixProducaoTMDBDto();
        Filme filmeExistente = getMatrixFilmeEntityComId();

        when(filmeRepository.findByIdTmdbIn(of(credito.getId()))).thenReturn(of(filmeExistente));

        List<Filme> resultado = service.obterOuCriar(of(credito));

        assertEquals(of(filmeExistente), resultado);
        verify(filmeRepository).findByIdTmdbIn(of(credito.getId()));
        verifyNoMoreInteractions(filmeRepository);
        verifyNoInteractions(adicionarFilmeService);
    }

    @Test
    @DisplayName("Deve retornar filme existente mesmo quando crédito não tiver título válido")
    void deveRetornarFilmeExistenteMesmoQuandoCreditoNaoTiverTituloValido() {
        ProducaoTMDBDto creditoSemTitulo = criarCredito(ID_TMDB_FILME_NOVO, TITULO_EM_BRANCO);
        Filme filmeExistente = getFilmeEntityComId(ID_FILME_NOVO);
        filmeExistente.setIdTmdb(ID_TMDB_FILME_NOVO);

        when(filmeRepository.findByIdTmdbIn(of(ID_TMDB_FILME_NOVO))).thenReturn(of(filmeExistente));

        List<Filme> resultado = service.obterOuCriar(of(creditoSemTitulo));

        assertEquals(of(filmeExistente), resultado);
        verifyNoInteractions(adicionarFilmeService);
    }

    @Test
    @DisplayName("Deve usar primeiro filme quando repositório retornar IDs TMDB duplicados")
    void deveUsarPrimeiroFilmeQuandoRepositorioRetornarIdsTmdbDuplicados() {
        ProducaoTMDBDto credito = getMatrixProducaoTMDBDto();
        Filme primeiroFilme = getMatrixFilmeEntityComId();
        Filme filmeDuplicado = getFilmeEntityComId(ID_FILME_DUPLICADO);
        filmeDuplicado.setIdTmdb(credito.getId());

        when(filmeRepository.findByIdTmdbIn(of(credito.getId()))).thenReturn(of(primeiroFilme, filmeDuplicado));

        List<Filme> resultado = service.obterOuCriar(of(credito));

        assertEquals(of(primeiroFilme), resultado);
        verifyNoInteractions(adicionarFilmeService);
    }

    @Test
    @DisplayName("Deve ignorar filme criado com ID TMDB diferente do crédito solicitado")
    void deveIgnorarFilmeCriadoComIdTmdbDiferenteDoCreditoSolicitado() {
        ProducaoTMDBDto creditoFilmeExistente = getMatrixProducaoTMDBDto();
        ProducaoTMDBDto creditoFilmeNovo = criarCredito(ID_TMDB_FILME_NOVO, TITULO_FILME_NOVO);
        Filme filmeExistente = getMatrixFilmeEntityComId();
        Filme filmeInconsistente = getFilmeEntityComId(ID_FILME_NOVO);
        filmeInconsistente.setIdTmdb(ID_TMDB_FILME_INCONSISTENTE);

        when(filmeRepository.findByIdTmdbIn(of(creditoFilmeExistente.getId(), ID_TMDB_FILME_NOVO)))
                .thenReturn(of(filmeExistente));
        when(adicionarFilmeService.adicionarFilmes(of(creditoFilmeNovo))).thenReturn(of(filmeInconsistente));

        List<Filme> resultado = service.obterOuCriar(of(creditoFilmeExistente, creditoFilmeNovo));

        assertEquals(of(filmeExistente), resultado);
        verify(adicionarFilmeService).adicionarFilmes(of(creditoFilmeNovo));
    }

    @Test
    @DisplayName("Deve ignorar crédito novo quando criação não retornar filme correspondente")
    void deveIgnorarCreditoNovoQuandoCriacaoNaoRetornarFilmeCorrespondente() {
        ProducaoTMDBDto creditoFilmeExistente = getMatrixProducaoTMDBDto();
        ProducaoTMDBDto creditoFilmeNovo = criarCredito(ID_TMDB_FILME_NOVO, TITULO_FILME_NOVO);
        Filme filmeExistente = getMatrixFilmeEntityComId();

        when(filmeRepository.findByIdTmdbIn(of(creditoFilmeExistente.getId(), ID_TMDB_FILME_NOVO)))
                .thenReturn(of(filmeExistente));
        when(adicionarFilmeService.adicionarFilmes(of(creditoFilmeNovo))).thenReturn(of());

        List<Filme> resultado = service.obterOuCriar(of(creditoFilmeExistente, creditoFilmeNovo));

        assertEquals(of(filmeExistente), resultado);
        verify(adicionarFilmeService).adicionarFilmes(of(creditoFilmeNovo));
    }

    @Test
    @DisplayName("Não deve consultar banco quando lista de créditos estiver vazia")
    void naoDeveConsultarBancoQuandoListaDeCreditosEstiverVazia() {
        List<Filme> resultado = service.obterOuCriar(of());

        assertEquals(of(), resultado);
        verifyNoInteractions(filmeRepository, adicionarFilmeService);
    }

    @Test
    @DisplayName("Não deve consultar banco para crédito sem ID TMDB")
    void naoDeveConsultarBancoParaCreditoSemIdTmdb() {
        ProducaoTMDBDto creditoSemId = criarCredito(null, TITULO_FILME_NOVO);

        List<Filme> resultado = service.obterOuCriar(of(creditoSemId));

        assertEquals(of(), resultado);
        verifyNoInteractions(filmeRepository, adicionarFilmeService);
    }

    @Test
    @DisplayName("Não deve criar filme para crédito com título em branco")
    void naoDeveCriarFilmeParaCreditoComTituloEmBranco() {
        ProducaoTMDBDto creditoSemTitulo = criarCredito(ID_TMDB_FILME_NOVO, TITULO_EM_BRANCO);

        when(filmeRepository.findByIdTmdbIn(of(ID_TMDB_FILME_NOVO))).thenReturn(of());

        List<Filme> resultado = service.obterOuCriar(of(creditoSemTitulo));

        assertEquals(of(), resultado);
        verify(filmeRepository).findByIdTmdbIn(of(ID_TMDB_FILME_NOVO));
        verifyNoMoreInteractions(filmeRepository);
        verifyNoInteractions(adicionarFilmeService);
    }

    @Test
    @DisplayName("Não deve criar filme para crédito sem título")
    void naoDeveCriarFilmeParaCreditoSemTitulo() {
        ProducaoTMDBDto creditoSemTitulo = criarCredito(ID_TMDB_FILME_NOVO, null);

        when(filmeRepository.findByIdTmdbIn(of(ID_TMDB_FILME_NOVO))).thenReturn(of());

        List<Filme> resultado = service.obterOuCriar(of(creditoSemTitulo));

        assertEquals(of(), resultado);
        verify(filmeRepository).findByIdTmdbIn(of(ID_TMDB_FILME_NOVO));
        verifyNoMoreInteractions(filmeRepository);
        verifyNoInteractions(adicionarFilmeService);
    }

    private ProducaoTMDBDto criarCredito(Long idTmdb, String titulo) {
        return ProducaoTMDBDto.builder()
                .id(idTmdb)
                .titulo(titulo)
                .build();
    }
}
