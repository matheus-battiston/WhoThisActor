package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBSerieDtoFactory.getAaronPaulAtorTMDBSerieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBSerieDtoFactory.getBryanCranstonAtorTMDBSerieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBSerieDtoFactory.getBryanCranstonComPersonagemMaiorAtorTMDBSerieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getBreakingBadProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieTMDBDtoFactory.getBreakingBadAtualizadoSerieTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieTMDBDtoFactory.getBreakingBadSerieTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieTMDBDtoFactory.getSerieTMDBDtoSemId;
import static java.util.List.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdicionarSerieService")
class AdicionarSerieServiceTest {
    private static final int INDICE_PRIMEIRA_SERIE = 0;
    private static final int INDICE_PRIMEIRO_ATOR = 0;
    private static final int INDICE_PRIMEIRO_PAPEL = 0;
    private static final int QUANTIDADE_ATORES_PROCESSADOS = 2;
    private static final Long ID_TMDB_BREAKING_BAD = 456L;
    private static final String TITULO_BREAKING_BAD = "Breaking Bad";
    private static final String TITULO_NORMALIZADO_BREAKING_BAD = "breaking bad";
    private static final String IMAGEM_BREAKING_BAD = "/breaking-bad.jpg";
    private static final Double POPULARIDADE_BREAKING_BAD = 9.5;
    private static final String TITULO_ATUALIZADO = "Breaking Bad Atualizado";
    private static final String TITULO_ATUALIZADO_NORMALIZADO = "breaking bad atualizado";
    private static final String IMAGEM_ATUALIZADA = "/breaking-bad-atualizado.jpg";
    private static final Double POPULARIDADE_ATUALIZADA = 20.0;
    private static final Long ID_TMDB_BRYAN_CRANSTON = 17419L;
    private static final Long ID_TMDB_AARON_PAUL = 84497L;
    private static final String PERSONAGEM_WALTER_WHITE = "Walter White";
    private static final String PERSONAGEM_WALTER_WHITE_HEISENBERG = "Walter White / Heisenberg";
    private static final String ERRO_AO_PROCESSAR = "Erro ao processar";

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private ProcessamentoSerieService processamentoSerieService;

    @Mock
    private BuscarElencoService buscarElencoService;

    @Mock
    private BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;

    @Mock
    private RecarregarCacheClassificacaoService recarregarCacheClassificacaoService;

    @Captor
    private ArgumentCaptor<List<Serie>> seriesCaptor;

    @Captor
    private ArgumentCaptor<List<Long>> idsCaptor;

    @Captor
    private ArgumentCaptor<List<AtorTMDBSerieDto>> atoresCaptor;

    @InjectMocks
    private AdicionarSerieService adicionarSerieService;

    @Test
    @DisplayName("Deve adicionar series mapeadas a partir de producoes do TMDB")
    void deveAdicionarSeriesMapeadasAPartirDeProducoesDoTmdb() {
        ProducaoTMDBDto producao = getBreakingBadProducaoTMDBDto();
        Serie serieSalva = getBreakingBadSerieEntityComId();

        when(serieRepository.saveAll(anyList())).thenReturn(of(serieSalva));

        List<Serie> resultado = adicionarSerieService.adicionarSeries(of(producao));

        assertThat(resultado).containsExactly(serieSalva);
        verify(serieRepository).saveAll(seriesCaptor.capture());

        Serie serieMapeada = seriesCaptor.getValue().get(INDICE_PRIMEIRA_SERIE);
        assertThat(serieMapeada.getIdTmdb()).isEqualTo(ID_TMDB_BREAKING_BAD);
        assertThat(serieMapeada.getTitulo()).isEqualTo(TITULO_BREAKING_BAD);
        assertThat(serieMapeada.getTituloNormalizado()).isEqualTo(TITULO_NORMALIZADO_BREAKING_BAD);
        assertThat(serieMapeada.getImagem()).isEqualTo(IMAGEM_BREAKING_BAD);
        assertThat(serieMapeada.getPopularidade()).isEqualTo(POPULARIDADE_BREAKING_BAD);
        assertThat(serieMapeada.getElencoInicializado()).isFalse();
        assertThat(serieMapeada.getUltimaAtualizacao()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar, deduplicar, processar e salvar elenco da serie")
    void deveBuscarDeduplicarProcessarESalvarElencoDaSerie() {
        Serie serie = getBreakingBadSerieEntityComId();
        serie.setElencoInicializado(false);
        AtorTMDBSerieDto bryanCranston = getBryanCranstonAtorTMDBSerieDto();
        AtorTMDBSerieDto aaronPaul = getAaronPaulAtorTMDBSerieDto();

        when(buscarElencoService.pesquisaElencoSerie(ID_TMDB_BREAKING_BAD)).thenReturn(of(bryanCranston, aaronPaul));
        doAnswer(invocation -> {
            assertThat(serie.getElencoInicializado()).isFalse();
            return null;
        }).when(processamentoSerieService).processarElenco(eq(serie), anyList());

        adicionarSerieService.adicionarElenco(serie);

        assertThat(serie.getElencoInicializado()).isTrue();

        InOrder inOrder = inOrder(
                buscarElencoService,
                processamentoSerieService,
                serieRepository,
                recarregarCacheClassificacaoService
        );
        inOrder.verify(buscarElencoService).pesquisaElencoSerie(ID_TMDB_BREAKING_BAD);
        inOrder.verify(processamentoSerieService).processarElenco(
                eq(serie),
                atoresCaptor.capture()
        );
        inOrder.verify(serieRepository).save(serie);
        inOrder.verify(recarregarCacheClassificacaoService).recarregarCacheAtoresPorProducao();

        assertThat(atoresCaptor.getValue()).containsExactlyInAnyOrder(bryanCranston, aaronPaul);
    }

    @Test
    @DisplayName("Deve manter apenas um credito por ator e escolher personagem mais completo")
    void deveManterApenasUmCreditoPorAtorEEscolherPersonagemMaisCompleto() {
        Serie serie = getBreakingBadSerieEntityComId();
        AtorTMDBSerieDto bryanCranston = getBryanCranstonAtorTMDBSerieDto();
        AtorTMDBSerieDto bryanCranstonComPersonagemMaior = getBryanCranstonComPersonagemMaiorAtorTMDBSerieDto();
        AtorTMDBSerieDto aaronPaul = getAaronPaulAtorTMDBSerieDto();

        when(buscarElencoService.pesquisaElencoSerie(ID_TMDB_BREAKING_BAD))
                .thenReturn(of(bryanCranston, bryanCranstonComPersonagemMaior, aaronPaul));

        adicionarSerieService.adicionarElenco(serie);

        verify(processamentoSerieService).processarElenco(eq(serie), atoresCaptor.capture());

        List<AtorTMDBSerieDto> atoresProcessados = atoresCaptor.getValue();
        assertThat(atoresProcessados).hasSize(QUANTIDADE_ATORES_PROCESSADOS);
        assertThat(atoresProcessados)
                .extracting(AtorTMDBSerieDto::getId)
                .containsExactlyInAnyOrder(ID_TMDB_BRYAN_CRANSTON, ID_TMDB_AARON_PAUL);
        assertThat(atoresProcessados).contains(bryanCranstonComPersonagemMaior);
        assertThat(atoresProcessados).doesNotContain(bryanCranston);
        assertThat(bryanCranstonComPersonagemMaior.getPapeis().get(INDICE_PRIMEIRO_PAPEL).getPersonagem())
                .isEqualTo(PERSONAGEM_WALTER_WHITE_HEISENBERG);
    }

    @Test
    @DisplayName("Deve manter primeiro credito quando personagem duplicado nao for maior")
    void deveManterPrimeiroCreditoQuandoPersonagemDuplicadoNaoForMaior() {
        Serie serie = getBreakingBadSerieEntityComId();
        AtorTMDBSerieDto bryanCranston = getBryanCranstonAtorTMDBSerieDto();
        AtorTMDBSerieDto bryanCranstonDuplicado = getBryanCranstonAtorTMDBSerieDto();

        when(buscarElencoService.pesquisaElencoSerie(ID_TMDB_BREAKING_BAD))
                .thenReturn(of(bryanCranston, bryanCranstonDuplicado));

        adicionarSerieService.adicionarElenco(serie);

        verify(processamentoSerieService).processarElenco(eq(serie), atoresCaptor.capture());

        assertThat(atoresCaptor.getValue()).containsExactly(bryanCranston);
        assertThat(atoresCaptor.getValue()
                .get(INDICE_PRIMEIRO_ATOR)
                .getPapeis()
                .get(INDICE_PRIMEIRO_PAPEL)
                .getPersonagem())
                .isEqualTo(PERSONAGEM_WALTER_WHITE);
    }

    @Test
    @DisplayName("Deve nao processar nem inicializar serie quando nao houver elenco")
    void deveNaoProcessarNemInicializarSerieQuandoNaoHouverElenco() {
        Serie serie = getBreakingBadSerieEntityComId();
        serie.setElencoInicializado(false);

        when(buscarElencoService.pesquisaElencoSerie(ID_TMDB_BREAKING_BAD)).thenReturn(of());

        adicionarSerieService.adicionarElenco(serie);

        assertThat(serie.getElencoInicializado()).isFalse();
        verifyNoInteractions(processamentoSerieService, recarregarCacheClassificacaoService);
        verify(serieRepository, never()).save(serie);
    }

    @Test
    @DisplayName("Deve propagar erro e nao inicializar quando processamento do elenco falhar")
    void devePropagarErroENaoInicializarQuandoProcessamentoDoElencoFalhar() {
        Serie serie = getBreakingBadSerieEntityComId();
        serie.setElencoInicializado(false);
        AtorTMDBSerieDto ator = getBryanCranstonAtorTMDBSerieDto();
        ResponseStatusException erro = new ResponseStatusException(BAD_REQUEST, ERRO_AO_PROCESSAR);

        when(buscarElencoService.pesquisaElencoSerie(ID_TMDB_BREAKING_BAD)).thenReturn(of(ator));
        doThrow(erro).when(processamentoSerieService).processarElenco(serie, of(ator));

        ResponseStatusException erroLancado = assertThrows(
                ResponseStatusException.class,
                () -> adicionarSerieService.adicionarElenco(serie)
        );

        assertThat(erroLancado).isSameAs(erro);
        assertThat(serie.getElencoInicializado()).isFalse();
        verify(serieRepository, never()).save(serie);
        verifyNoInteractions(recarregarCacheClassificacaoService);
    }

    @Test
    @DisplayName("Deve salvar serie basica quando busca por nome retornar serie nova")
    void deveSalvarSerieBasicaQuandoBuscaPorNomeRetornarSerieNova() {
        SerieTMDBDto serieDto = getBreakingBadSerieTMDBDto();

        when(buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(TITULO_BREAKING_BAD)).thenReturn(of(serieDto));
        when(serieRepository.findByIdTmdbIn(anyList())).thenReturn(of());
        when(serieRepository.saveAll(anyList())).thenReturn(of(getBreakingBadSerieEntityComId()));

        adicionarSerieService.adicionarSerieComNome(TITULO_BREAKING_BAD);

        verify(serieRepository).findByIdTmdbIn(idsCaptor.capture());
        verify(serieRepository).saveAll(seriesCaptor.capture());

        assertThat(idsCaptor.getValue()).containsExactly(ID_TMDB_BREAKING_BAD);

        Serie serieSalva = seriesCaptor.getValue().get(INDICE_PRIMEIRA_SERIE);
        assertThat(serieSalva.getIdTmdb()).isEqualTo(ID_TMDB_BREAKING_BAD);
        assertThat(serieSalva.getTitulo()).isEqualTo(TITULO_BREAKING_BAD);
        assertThat(serieSalva.getTituloNormalizado()).isEqualTo(TITULO_NORMALIZADO_BREAKING_BAD);
        assertThat(serieSalva.getImagem()).isEqualTo(IMAGEM_BREAKING_BAD);
        assertThat(serieSalva.getPopularidade()).isEqualTo(POPULARIDADE_BREAKING_BAD);
        assertThat(serieSalva.getElencoInicializado()).isFalse();
        assertThat(serieSalva.getUltimaAtualizacao()).isNotNull();
    }

    @Test
    @DisplayName("Deve atualizar serie existente quando busca por nome retornar id ja salvo")
    void deveAtualizarSerieExistenteQuandoBuscaPorNomeRetornarIdJaSalvo() {
        Serie serieExistente = getBreakingBadSerieEntityComId();
        SerieTMDBDto serieDto = getBreakingBadAtualizadoSerieTMDBDto();

        when(buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(TITULO_BREAKING_BAD)).thenReturn(of(serieDto));
        when(serieRepository.findByIdTmdbIn(of(ID_TMDB_BREAKING_BAD))).thenReturn(of(serieExistente));
        when(serieRepository.saveAll(anyList())).thenReturn(of(serieExistente));

        adicionarSerieService.adicionarSerieComNome(TITULO_BREAKING_BAD);

        verify(serieRepository).saveAll(seriesCaptor.capture());

        Serie serieAtualizada = seriesCaptor.getValue().get(INDICE_PRIMEIRA_SERIE);
        assertThat(serieAtualizada).isSameAs(serieExistente);
        assertThat(serieAtualizada.getTitulo()).isEqualTo(TITULO_ATUALIZADO);
        assertThat(serieAtualizada.getTituloNormalizado()).isEqualTo(TITULO_ATUALIZADO_NORMALIZADO);
        assertThat(serieAtualizada.getImagem()).isEqualTo(IMAGEM_ATUALIZADA);
        assertThat(serieAtualizada.getPopularidade()).isEqualTo(POPULARIDADE_ATUALIZADA);
        assertThat(serieAtualizada.getUltimaAtualizacao()).isEqualTo(LocalDate.now());
        assertThat(serieAtualizada.getElencoInicializado()).isTrue();
    }

    @Test
    @DisplayName("Deve consultar ids distintos quando busca por nome retornar duplicados")
    void deveConsultarIdsDistintosQuandoBuscaPorNomeRetornarDuplicados() {
        SerieTMDBDto primeiroRetorno = getBreakingBadSerieTMDBDto();
        SerieTMDBDto segundoRetorno = getBreakingBadSerieTMDBDto();

        when(buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(TITULO_BREAKING_BAD))
                .thenReturn(of(primeiroRetorno, segundoRetorno));
        when(serieRepository.findByIdTmdbIn(anyList())).thenReturn(of());
        when(serieRepository.saveAll(anyList())).thenReturn(of(getBreakingBadSerieEntityComId()));

        adicionarSerieService.adicionarSerieComNome(TITULO_BREAKING_BAD);

        verify(serieRepository).findByIdTmdbIn(idsCaptor.capture());
        assertThat(idsCaptor.getValue()).containsExactly(ID_TMDB_BREAKING_BAD);
    }

    @Test
    @DisplayName("Deve ignorar series sem id TMDB ao salvar por nome")
    void deveIgnorarSeriesSemIdTmdbAoSalvarPorNome() {
        SerieTMDBDto serieSemId = getSerieTMDBDtoSemId();

        when(buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(TITULO_BREAKING_BAD)).thenReturn(of(serieSemId));

        adicionarSerieService.adicionarSerieComNome(TITULO_BREAKING_BAD);

        verify(serieRepository, never()).findByIdTmdbIn(anyList());
        verify(serieRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve ignorar serie sem id quando busca por nome retornar series validas")
    void deveIgnorarSerieSemIdQuandoBuscaPorNomeRetornarSeriesValidas() {
        SerieTMDBDto serieValida = getBreakingBadSerieTMDBDto();
        SerieTMDBDto serieSemId = getSerieTMDBDtoSemId();

        when(buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(TITULO_BREAKING_BAD))
                .thenReturn(of(serieValida, serieSemId));
        when(serieRepository.findByIdTmdbIn(of(ID_TMDB_BREAKING_BAD))).thenReturn(of());
        when(serieRepository.saveAll(anyList())).thenReturn(of(getBreakingBadSerieEntityComId()));

        adicionarSerieService.adicionarSerieComNome(TITULO_BREAKING_BAD);

        verify(serieRepository).saveAll(seriesCaptor.capture());
        assertThat(seriesCaptor.getValue())
                .extracting(Serie::getIdTmdb)
                .containsExactly(ID_TMDB_BREAKING_BAD);
    }

    @Test
    @DisplayName("Deve nao acessar repositorio quando busca por nome nao retornar series")
    void deveNaoAcessarRepositorioQuandoBuscaPorNomeNaoRetornarSeries() {
        when(buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(TITULO_BREAKING_BAD)).thenReturn(of());

        adicionarSerieService.adicionarSerieComNome(TITULO_BREAKING_BAD);

        verify(serieRepository, never()).findByIdTmdbIn(anyList());
        verify(serieRepository, never()).saveAll(anyList());
    }
}
