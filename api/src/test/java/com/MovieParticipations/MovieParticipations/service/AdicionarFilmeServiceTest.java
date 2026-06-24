package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBMovieDtoFactory.getKeanuReevesAtorTMDBMovieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeTMDBDtoFactory.*;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static java.time.LocalDate.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdicionarFilmeService")
class AdicionarFilmeServiceTest {
    private static final Long ID_TMDB_MATRIX = 123L;
    private static final String TITULO_MATRIX = "Matrix";
    private static final String TITULO_NORMALIZADO_MATRIX = "matrix";
    private static final String IMAGEM_MATRIX = "/matrix.jpg";
    private static final Double POPULARIDADE_MATRIX = 10.0;
    private static final String NAO_TEM_ELENCO = "Nao tem elenco";
    private static final String TITULO_ATUALIZADO = "Matrix Revolutions";
    private static final String TITULO_ATUALIZADO_NORMALIZADO = "matrix revolutions";
    private static final String IMAGEM_ATUALIZADA = "/matrix-revolutions.jpg";
    private static final Double POPULARIDADE_ATUALIZADA = 20.0;
    private static final Long ID_TMDB_KEANU_REEVES = 6384L;
    private static final String PERSONAGEM_NEO_MAIS_COMPLETO = "Neo / The One";
    private static final String PERSONAGEM_NEO_MENOR = "N";

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private BuscarElencoService buscarElencoService;

    @Mock
    private ProcessamentoFilmeService processamentoFilmeService;

    @Mock
    private BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;

    @Mock
    private RecarregarCacheClassificacaoService recarregarCacheClassificacaoService;

    @Captor
    private ArgumentCaptor<List<Filme>> filmesCaptor;

    @Captor
    private ArgumentCaptor<List<Long>> idsCaptor;

    @Captor
    private ArgumentCaptor<List<AtorTMDBMovieDto>> atoresCaptor;

    @InjectMocks
    private AdicionarFilmeService adicionarFilmeService;

    @Test
    @DisplayName("Deve adicionar filmes mapeados a partir de producoes do TMDB")
    void deveAdicionarFilmesMapeadosAPartirDeProducoesDoTmdb() {
        ProducaoTMDBDto producao = getMatrixProducaoTMDBDto();
        Filme filmeSalvo = getMatrixFilmeEntityComId();
        List<Filme> filmesSalvos = of(filmeSalvo);

        when(filmeRepository.saveAll(anyList())).thenReturn(filmesSalvos);

        List<Filme> resultado = adicionarFilmeService.adicionarFilmes(of(producao));

        assertThat(resultado).containsExactly(filmeSalvo);
        verify(filmeRepository).saveAll(filmesCaptor.capture());

        Filme filmeMapeado = filmesCaptor.getValue().get(0);
        assertThat(filmeMapeado.getIdTmdb()).isEqualTo(ID_TMDB_MATRIX);
        assertThat(filmeMapeado.getTitulo()).isEqualTo(TITULO_MATRIX);
        assertThat(filmeMapeado.getTituloNormalizado()).isEqualTo(TITULO_NORMALIZADO_MATRIX);
        assertThat(filmeMapeado.getImagem()).isEqualTo(IMAGEM_MATRIX);
        assertThat(filmeMapeado.getPopularidade()).isEqualTo(POPULARIDADE_MATRIX);
        assertThat(filmeMapeado.getInicializado()).isFalse();
        assertThat(filmeMapeado.getUltimaAtualizacao()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar, processar e salvar elenco do filme")
    void deveBuscarProcessarESalvarElencoDoFilme() {
        Filme filme = getMatrixFilmeEntityComId();
        List<AtorTMDBMovieDto> atores = of(getKeanuReevesAtorTMDBMovieDto());

        when(buscarElencoService.pesquisaElencoFilme(ID_TMDB_MATRIX)).thenReturn(atores);

        adicionarFilmeService.adicionarElenco(filme);

        assertThat(filme.getInicializado()).isTrue();

        InOrder inOrder = inOrder(
                buscarElencoService,
                processamentoFilmeService,
                filmeRepository,
                recarregarCacheClassificacaoService
        );
        inOrder.verify(buscarElencoService).pesquisaElencoFilme(ID_TMDB_MATRIX);
        inOrder.verify(processamentoFilmeService).processarElenco(filme, atores);
        inOrder.verify(filmeRepository).save(filme);
        inOrder.verify(recarregarCacheClassificacaoService).recarregarCacheAtoresPorProducao();
    }

    @Test
    @DisplayName("Deve manter apenas um crédito por ator e escolher personagem mais completo")
    void deveManterApenasUmCreditoPorAtorEEscolherPersonagemMaisCompleto() {
        Filme filme = getMatrixFilmeEntityComId();
        AtorTMDBMovieDto keanu = getKeanuReevesAtorTMDBMovieDto();
        AtorTMDBMovieDto keanuComPersonagemMaior = AtorTMDBMovieDto.builder()
                .id(ID_TMDB_KEANU_REEVES)
                .nome(keanu.getNome())
                .personagem(PERSONAGEM_NEO_MAIS_COMPLETO)
                .build();

        when(buscarElencoService.pesquisaElencoFilme(ID_TMDB_MATRIX))
                .thenReturn(of(keanu, keanuComPersonagemMaior));

        adicionarFilmeService.adicionarElenco(filme);

        verify(processamentoFilmeService).processarElenco(eq(filme), atoresCaptor.capture());
        assertThat(atoresCaptor.getValue()).containsExactly(keanuComPersonagemMaior);
    }

    @Test
    @DisplayName("Deve manter primeiro crédito quando personagem duplicado for menor")
    void deveManterPrimeiroCreditoQuandoPersonagemDuplicadoForMenor() {
        Filme filme = getMatrixFilmeEntityComId();
        AtorTMDBMovieDto keanu = getKeanuReevesAtorTMDBMovieDto();
        AtorTMDBMovieDto keanuComPersonagemMenor = AtorTMDBMovieDto.builder()
                .id(ID_TMDB_KEANU_REEVES)
                .nome(keanu.getNome())
                .personagem(PERSONAGEM_NEO_MENOR)
                .build();

        when(buscarElencoService.pesquisaElencoFilme(ID_TMDB_MATRIX))
                .thenReturn(of(keanu, keanuComPersonagemMenor));

        adicionarFilmeService.adicionarElenco(filme);

        verify(processamentoFilmeService).processarElenco(eq(filme), atoresCaptor.capture());
        assertThat(atoresCaptor.getValue()).containsExactly(keanu);
    }

    @Test
    @DisplayName("Deve lancar erro quando filme nao tiver elenco")
    void deveLancarErroQuandoFilmeNaoTiverElenco() {
        Filme filme = getMatrixFilmeEntityComId();
        filme.setInicializado(false);

        when(buscarElencoService.pesquisaElencoFilme(ID_TMDB_MATRIX)).thenReturn(of());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> adicionarFilmeService.adicionarElenco(filme)
        );

        assertThat(erro.getReason()).isEqualTo(NAO_TEM_ELENCO);
        assertThat(filme.getInicializado()).isFalse();
        verifyNoInteractions(processamentoFilmeService, recarregarCacheClassificacaoService);
        verify(filmeRepository, never()).save(filme);
    }

    @Test
    @DisplayName("Deve propagar erro e nao salvar quando processamento do elenco falhar")
    void devePropagarErroENaoSalvarQuandoProcessamentoDoElencoFalhar() {
        Filme filme = getMatrixFilmeEntityComId();
        filme.setInicializado(false);
        List<AtorTMDBMovieDto> atores = of(getKeanuReevesAtorTMDBMovieDto());
        ResponseStatusException erro = new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Erro ao processar");

        when(buscarElencoService.pesquisaElencoFilme(ID_TMDB_MATRIX)).thenReturn(atores);
        doThrow(erro).when(processamentoFilmeService).processarElenco(filme, atores);

        ResponseStatusException erroLancado = assertThrows(
                ResponseStatusException.class,
                () -> adicionarFilmeService.adicionarElenco(filme)
        );

        assertThat(erroLancado).isSameAs(erro);
        assertThat(filme.getInicializado()).isFalse();
        verify(filmeRepository, never()).save(filme);
        verifyNoInteractions(recarregarCacheClassificacaoService);
    }

    @Test
    @DisplayName("Deve salvar filme basico quando busca por nome retornar filme novo")
    void deveSalvarFilmeBasicoQuandoBuscaPorNomeRetornarFilmeNovo() {
        FilmeTMDBDto filmeDto = getMatrixFilmeTMDBDto();

        when(buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(TITULO_MATRIX)).thenReturn(of(filmeDto));
        when(filmeRepository.findByIdTmdbIn(anyList())).thenReturn(of());
        when(filmeRepository.saveAll(anyList())).thenReturn(of(getMatrixFilmeEntityComId()));

        adicionarFilmeService.adicionarFilmeComNome(TITULO_MATRIX);

        verify(filmeRepository).findByIdTmdbIn(idsCaptor.capture());
        verify(filmeRepository).saveAll(filmesCaptor.capture());

        assertThat(idsCaptor.getValue()).containsExactly(ID_TMDB_MATRIX);

        Filme filmeSalvo = filmesCaptor.getValue().get(0);
        assertThat(filmeSalvo.getIdTmdb()).isEqualTo(ID_TMDB_MATRIX);
        assertThat(filmeSalvo.getTitulo()).isEqualTo(TITULO_MATRIX);
        assertThat(filmeSalvo.getTituloNormalizado()).isEqualTo(TITULO_NORMALIZADO_MATRIX);
        assertThat(filmeSalvo.getImagem()).isEqualTo(IMAGEM_MATRIX);
        assertThat(filmeSalvo.getPopularidade()).isEqualTo(POPULARIDADE_MATRIX);
        assertThat(filmeSalvo.getInicializado()).isFalse();
        assertThat(filmeSalvo.getUltimaAtualizacao()).isNotNull();
    }

    @Test
    @DisplayName("Deve atualizar filme existente quando busca por nome retornar id ja salvo")
    void deveAtualizarFilmeExistenteQuandoBuscaPorNomeRetornarIdJaSalvo() {
        Filme filmeExistente = getMatrixFilmeEntityComId();
        FilmeTMDBDto filmeDto = getMatrixRevolutionsFilmeTMDBDto();

        when(buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(TITULO_MATRIX)).thenReturn(of(filmeDto));
        when(filmeRepository.findByIdTmdbIn(of(ID_TMDB_MATRIX))).thenReturn(of(filmeExistente));
        when(filmeRepository.saveAll(anyList())).thenReturn(of(filmeExistente));

        adicionarFilmeService.adicionarFilmeComNome(TITULO_MATRIX);

        verify(filmeRepository).saveAll(filmesCaptor.capture());

        Filme filmeAtualizado = filmesCaptor.getValue().get(0);
        assertThat(filmeAtualizado).isSameAs(filmeExistente);
        assertThat(filmeAtualizado.getTitulo()).isEqualTo(TITULO_ATUALIZADO);
        assertThat(filmeAtualizado.getTituloNormalizado()).isEqualTo(TITULO_ATUALIZADO_NORMALIZADO);
        assertThat(filmeAtualizado.getImagem()).isEqualTo(IMAGEM_ATUALIZADA);
        assertThat(filmeAtualizado.getPopularidade()).isEqualTo(POPULARIDADE_ATUALIZADA);
        assertThat(filmeAtualizado.getUltimaAtualizacao()).isEqualTo(now());
        assertThat(filmeAtualizado.getInicializado()).isTrue();
    }

    @Test
    @DisplayName("Deve consultar ids distintos quando busca por nome retornar duplicados")
    void deveConsultarIdsDistintosQuandoBuscaPorNomeRetornarDuplicados() {
        FilmeTMDBDto primeiroRetorno = getMatrixFilmeTMDBDto();
        FilmeTMDBDto segundoRetorno = getMatrixFilmeTMDBDto();

        when(buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(TITULO_MATRIX))
                .thenReturn(of(primeiroRetorno, segundoRetorno));
        when(filmeRepository.findByIdTmdbIn(anyList())).thenReturn(of());
        when(filmeRepository.saveAll(anyList())).thenReturn(of(getMatrixFilmeEntityComId()));

        adicionarFilmeService.adicionarFilmeComNome(TITULO_MATRIX);

        verify(filmeRepository).findByIdTmdbIn(idsCaptor.capture());
        assertThat(idsCaptor.getValue()).containsExactly(ID_TMDB_MATRIX);
    }

    @Test
    @DisplayName("Deve ignorar filmes sem id TMDB ao salvar por nome")
    void deveIgnorarFilmesSemIdTmdbAoSalvarPorNome() {
        FilmeTMDBDto filmeSemId = getFilmeTMDBDtoSemId();

        when(buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(TITULO_MATRIX)).thenReturn(of(filmeSemId));

        adicionarFilmeService.adicionarFilmeComNome(TITULO_MATRIX);

        verify(filmeRepository, never()).findByIdTmdbIn(anyList());
        verify(filmeRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve ignorar filme sem id quando busca por nome retornar filmes validos")
    void deveIgnorarFilmeSemIdQuandoBuscaPorNomeRetornarFilmesValidos() {
        FilmeTMDBDto filmeValido = getMatrixFilmeTMDBDto();
        FilmeTMDBDto filmeSemId = getFilmeTMDBDtoSemId();

        when(buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(TITULO_MATRIX))
                .thenReturn(of(filmeValido, filmeSemId));
        when(filmeRepository.findByIdTmdbIn(of(ID_TMDB_MATRIX))).thenReturn(of());
        when(filmeRepository.saveAll(anyList())).thenReturn(of(getMatrixFilmeEntityComId()));

        adicionarFilmeService.adicionarFilmeComNome(TITULO_MATRIX);

        verify(filmeRepository).saveAll(filmesCaptor.capture());
        assertThat(filmesCaptor.getValue())
                .extracting(Filme::getIdTmdb)
                .containsExactly(ID_TMDB_MATRIX);
    }

    @Test
    @DisplayName("Deve nao acessar repositorio quando busca por nome nao retornar filmes")
    void deveNaoAcessarRepositorioQuandoBuscaPorNomeNaoRetornarFilmes() {
        when(buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(TITULO_MATRIX)).thenReturn(of());

        adicionarFilmeService.adicionarFilmeComNome(TITULO_MATRIX);

        verify(filmeRepository, never()).findByIdTmdbIn(anyList());
        verify(filmeRepository, never()).saveAll(anyList());
    }

}
