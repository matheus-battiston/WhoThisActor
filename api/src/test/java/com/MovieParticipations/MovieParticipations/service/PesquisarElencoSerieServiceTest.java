package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
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

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.TV;
import static com.MovieParticipations.MovieParticipations.factories.response.OpcaoPesquisaElencoResponseFactory.getWalterWhiteOpcaoPesquisaElencoResponse;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProviderDtoFactory.getDisneyPlusProviderDto;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioAutenticadoFactory.getUsuarioAutenticadoDto;
import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisarElencoSerieService")
class PesquisarElencoSerieServiceTest {
    private static final Long ID_SERIE_PESQUISA = 10L;
    private static final Long ID_SERIE = 2L;
    private static final Long ID_TMDB_BREAKING_BAD = 456L;
    private static final String FILTRO_VAZIO = "";
    private static final String FILTRO_INEXISTENTE = "Inexistente";
    private static final String FILTRO_WALTER = "Walter";
    private static final int PRIMEIRA_PAGINA = 0;
    private static final int TAMANHO_PAGINA = 20;
    private static final String NOME_BREAKING_BAD = "Breaking Bad";
    private static final String IMAGEM_BREAKING_BAD = "/breaking-bad.jpg";
    private static final boolean NAO_ESTA_FAVORITADO = false;

    @Mock
    private SerieAtorRepository serieAtorRepository;

    @Mock
    private FavoritarSerieService favoritarSerieService;

    @Mock
    private ObterSerieInicializadaService obterSerieInicializadaService;

    @Mock
    private ProvidersService providersService;

    @InjectMocks
    private PesquisarElencoSerieService pesquisarElencoSerieService;

    @Test
    @DisplayName("Deve pesquisar elenco com filtro vazio quando o filtro for nulo")
    void devePesquisarElencoComFiltroVazioQuandoFiltroForNulo() {
        OpcaoPesquisaElencoResponse opcao = getWalterWhiteOpcaoPesquisaElencoResponse();
        Pageable pageable = Pageable.ofSize(TAMANHO_PAGINA);
        PageRequest pageRequest = PageRequest.of(PRIMEIRA_PAGINA, TAMANHO_PAGINA);
        List<OpcaoPesquisaElencoResponse> elenco = List.of(opcao);
        PageImpl<OpcaoPesquisaElencoResponse> page = new PageImpl<>(elenco);

        when(serieAtorRepository.findElencoPorIdComPersonagem(ID_SERIE_PESQUISA, FILTRO_VAZIO, pageable))
                .thenReturn(page);

        List<OpcaoPesquisaElencoResponse> resultado = pesquisarElencoSerieService.pesquisarElenco(ID_SERIE_PESQUISA, null);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUrlImagem()).isEqualTo(normalizar(opcao.getUrlImagem()));
        verify(serieAtorRepository).findElencoPorIdComPersonagem(
                ID_SERIE_PESQUISA,
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

        when(serieAtorRepository.findElencoPorIdComPersonagem(ID_SERIE_PESQUISA, FILTRO_INEXISTENTE, pageable))
                .thenReturn(page);

        List<OpcaoPesquisaElencoResponse> resultado = pesquisarElencoSerieService.pesquisarElenco(ID_SERIE_PESQUISA, FILTRO_INEXISTENTE);

        assertThat(resultado).isEmpty();
        verify(serieAtorRepository).findElencoPorIdComPersonagem(
                ID_SERIE_PESQUISA,
                FILTRO_INEXISTENTE,
                pageRequest
        );
    }

    @Test
    @DisplayName("Deve montar detalhes da serie com elenco, providers e favorito quando usuario estiver autenticado")
    void deveMontarDetalhesDaSerieComElencoProvidersEFavoritoQuandoUsuarioAutenticado() {
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        Serie serie = getBreakingBadSerieEntityComId();
        OpcaoPesquisaElencoResponse opcao = getWalterWhiteOpcaoPesquisaElencoResponse();
        ProviderDto provider = getDisneyPlusProviderDto();
        Pageable pageable = Pageable.ofSize(TAMANHO_PAGINA);
        List<OpcaoPesquisaElencoResponse> elenco = List.of(opcao);
        PageImpl<OpcaoPesquisaElencoResponse> page = new PageImpl<>(elenco);
        List<ProviderDto> providers = List.of(provider);

        when(obterSerieInicializadaService.obter(ID_SERIE)).thenReturn(serie);
        when(serieAtorRepository.findElencoPorIdComPersonagem(ID_SERIE, FILTRO_WALTER, pageable))
                .thenReturn(page);
        when(providersService.buscarProviders(ID_TMDB_BREAKING_BAD, TV)).thenReturn(providers);
        when(favoritarSerieService.estaFavoritadoComAuthId(usuarioAutenticado, ID_SERIE)).thenReturn(NAO_ESTA_FAVORITADO);

        DetalhesProducaoComElenco resultado = pesquisarElencoSerieService.pesquisar(ID_SERIE, FILTRO_WALTER, usuarioAutenticado);

        assertThat(resultado.getId()).isEqualTo(ID_SERIE);
        assertThat(resultado.getNome()).isEqualTo(NOME_BREAKING_BAD);
        assertThat(resultado.getImagem()).isEqualTo(normalizar(IMAGEM_BREAKING_BAD));
        assertThat(resultado.getTipoMidia()).isEqualTo(TV);
        assertThat(resultado.getElenco()).hasSize(1);
        assertThat(resultado.getElenco().get(0).getNome()).isEqualTo(opcao.getNome());
        assertThat(resultado.getElenco().get(0).getNomePersonagem()).isEqualTo(opcao.getNomePersonagem());
        assertThat(resultado.getElenco().get(0).getUrlImagem()).isEqualTo(normalizar(opcao.getUrlImagem()));
        assertThat(resultado.getProviders()).containsExactly(provider);
        assertThat(resultado.getEstaFavoritado()).isFalse();
    }

    @Test
    @DisplayName("Deve manter favorito nulo quando usuario nao estiver autenticado")
    void deveManterFavoritoNuloQuandoUsuarioNaoAutenticado() {
        Serie serie = getBreakingBadSerieEntityComId();
        Pageable pageable = Pageable.ofSize(TAMANHO_PAGINA);
        List<OpcaoPesquisaElencoResponse> elenco = List.of();
        PageImpl<OpcaoPesquisaElencoResponse> page = new PageImpl<>(elenco);
        List<ProviderDto> providers = List.of();

        when(obterSerieInicializadaService.obter(ID_SERIE)).thenReturn(serie);
        when(serieAtorRepository.findElencoPorIdComPersonagem(ID_SERIE, FILTRO_VAZIO, pageable))
                .thenReturn(page);
        when(providersService.buscarProviders(ID_TMDB_BREAKING_BAD, TV)).thenReturn(providers);

        DetalhesProducaoComElenco resultado = pesquisarElencoSerieService.pesquisar(ID_SERIE, FILTRO_VAZIO, null);

        assertThat(resultado.getEstaFavoritado()).isNull();
        verifyNoInteractions(favoritarSerieService);
    }
}
