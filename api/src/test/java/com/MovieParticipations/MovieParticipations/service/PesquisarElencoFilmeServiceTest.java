package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.MOVIE;
import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.OpcaoPesquisaElencoResponseFactory.getNeoOpcaoPesquisaElencoResponse;
import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getNetflixProviderDto;
import static com.MovieParticipations.MovieParticipations.factories.UsuarioAutenticadoFactory.getUsuarioAutenticadoDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisarElencoFilmeService")
class PesquisarElencoFilmeServiceTest {
    private static final Long ID_FILME_PESQUISA = 10L;
    private static final Long ID_FILME = 1L;
    private static final Long ID_TMDB_MATRIX = 123L;
    private static final String FILTRO_VAZIO = "";
    private static final String FILTRO_INEXISTENTE = "Inexistente";
    private static final String FILTRO_NEO = "Neo";
    private static final int PRIMEIRA_PAGINA = 0;
    private static final int TAMANHO_PAGINA = 20;
    private static final String NOME_MATRIX = "Matrix";
    private static final String IMAGEM_MATRIX = "/matrix.jpg";
    private static final boolean ESTA_FAVORITADO = true;

    @Mock
    private FilmeAtorRepository filmeAtorRepository;

    @Mock
    private ObterFilmeInicializadoService obterFilmeInicializadoService;

    @Mock
    private ProvidersService providersService;

    @Mock
    private FavoritarFilmeService favoritarFilmeService;

    @InjectMocks
    private PesquisarElencoFilmeService pesquisarElencoFilmeService;

    @Test
    @DisplayName("Deve pesquisar elenco com filtro vazio quando o filtro for nulo")
    void devePesquisarElencoComFiltroVazioQuandoFiltroForNulo() {
        OpcaoPesquisaElencoResponse opcao = getNeoOpcaoPesquisaElencoResponse();
        Pageable pageable = Pageable.ofSize(TAMANHO_PAGINA);
        PageRequest pageRequest = PageRequest.of(PRIMEIRA_PAGINA, TAMANHO_PAGINA);
        List<OpcaoPesquisaElencoResponse> elenco = List.of(opcao);
        PageImpl<OpcaoPesquisaElencoResponse> page = new PageImpl<>(elenco);

        when(filmeAtorRepository.findElencoPorIdComPersonagem(ID_FILME_PESQUISA, FILTRO_VAZIO, pageable))
                .thenReturn(page);

        List<OpcaoPesquisaElencoResponse> resultado = pesquisarElencoFilmeService.pesquisarElenco(ID_FILME_PESQUISA, null);

        assertThat(resultado).containsExactly(opcao);
        verify(filmeAtorRepository).findElencoPorIdComPersonagem(
                ID_FILME_PESQUISA,
                FILTRO_VAZIO,
                pageRequest
        );
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum elenco for encontrado")
    void deveRetornarListaVaziaQuandoNenhumElencoForEncontrado() {
        Pageable pageable = Pageable.ofSize(TAMANHO_PAGINA);
        PageRequest pageRequest = PageRequest.of(PRIMEIRA_PAGINA, TAMANHO_PAGINA);
        List<OpcaoPesquisaElencoResponse> elenco = List.of();
        PageImpl<OpcaoPesquisaElencoResponse> page = new PageImpl<>(elenco);

        when(filmeAtorRepository.findElencoPorIdComPersonagem(ID_FILME_PESQUISA, FILTRO_INEXISTENTE, pageable))
                .thenReturn(page);

        List<OpcaoPesquisaElencoResponse> resultado = pesquisarElencoFilmeService.pesquisarElenco(ID_FILME_PESQUISA, FILTRO_INEXISTENTE);

        assertThat(resultado).isEmpty();
        verify(filmeAtorRepository).findElencoPorIdComPersonagem(
                ID_FILME_PESQUISA,
                FILTRO_INEXISTENTE,
                pageRequest
        );
    }

    @Test
    @DisplayName("Deve montar detalhes do filme com elenco, providers e favorito quando usuario estiver autenticado")
    void deveMontarDetalhesDoFilmeComElencoProvidersEFavoritoQuandoUsuarioAutenticado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Filme filme = getMatrixFilmeEntityComId();
        OpcaoPesquisaElencoResponse opcao = getNeoOpcaoPesquisaElencoResponse();
        ProviderDto provider = getNetflixProviderDto();
        Pageable pageable = Pageable.ofSize(TAMANHO_PAGINA);
        List<OpcaoPesquisaElencoResponse> elenco = List.of(opcao);
        PageImpl<OpcaoPesquisaElencoResponse> page = new PageImpl<>(elenco);
        List<ProviderDto> providers = List.of(provider);

        when(obterFilmeInicializadoService.obter(ID_FILME)).thenReturn(filme);
        when(filmeAtorRepository.findElencoPorIdComPersonagem(ID_FILME, FILTRO_NEO, pageable))
                .thenReturn(page);
        when(providersService.buscarProviders(ID_TMDB_MATRIX, MOVIE)).thenReturn(providers);
        when(favoritarFilmeService.estaFavoritadoComAuthId(usuarioAutenticado, ID_FILME)).thenReturn(ESTA_FAVORITADO);

        DetalhesProducaoComElenco resultado = pesquisarElencoFilmeService.pesquisar(ID_FILME, FILTRO_NEO, usuarioAutenticado);

        assertThat(resultado.getId()).isEqualTo(ID_FILME);
        assertThat(resultado.getNome()).isEqualTo(NOME_MATRIX);
        assertThat(resultado.getImagem()).isEqualTo(IMAGEM_MATRIX);
        assertThat(resultado.getElenco()).containsExactly(opcao);
        assertThat(resultado.getProviders()).containsExactly(provider);
        assertThat(resultado.getEstaFavoritado()).isTrue();
        verify(providersService).buscarProviders(ID_TMDB_MATRIX, MOVIE);
    }

    @Test
    @DisplayName("Deve manter favorito nulo quando usuario nao estiver autenticado")
    void deveManterFavoritoNuloQuandoUsuarioNaoAutenticado() {
        Filme filme = getMatrixFilmeEntityComId();
        Pageable pageable = Pageable.ofSize(TAMANHO_PAGINA);
        List<OpcaoPesquisaElencoResponse> elenco = List.of();
        PageImpl<OpcaoPesquisaElencoResponse> page = new PageImpl<>(elenco);
        List<ProviderDto> providers = List.of();

        when(obterFilmeInicializadoService.obter(ID_FILME)).thenReturn(filme);
        when(filmeAtorRepository.findElencoPorIdComPersonagem(ID_FILME, FILTRO_VAZIO, pageable))
                .thenReturn(page);
        when(providersService.buscarProviders(ID_TMDB_MATRIX, MOVIE)).thenReturn(providers);

        DetalhesProducaoComElenco resultado = pesquisarElencoFilmeService.pesquisar(ID_FILME, FILTRO_VAZIO, null);

        assertThat(resultado.getEstaFavoritado()).isNull();
        verifyNoInteractions(favoritarFilmeService);
    }
}
