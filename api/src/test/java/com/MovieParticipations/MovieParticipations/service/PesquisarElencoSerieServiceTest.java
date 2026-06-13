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
import static com.MovieParticipations.MovieParticipations.factories.OpcaoPesquisaElencoResponseFactory.getWalterWhite;
import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getDisneyPlus;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;
import static com.MovieParticipations.MovieParticipations.factories.UsuarioAutenticadoFactory.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisarElencoSerieService")
class PesquisarElencoSerieServiceTest {

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
        OpcaoPesquisaElencoResponse opcao = getWalterWhite();
        when(serieAtorRepository.findElencoPorIdComPersonagem(10L, "", Pageable.ofSize(20)))
                .thenReturn(new PageImpl<>(List.of(opcao)));

        List<OpcaoPesquisaElencoResponse> resultado = pesquisarElencoSerieService.pesquisarElenco(10L, null);

        assertThat(resultado).containsExactly(opcao);
        verify(serieAtorRepository).findElencoPorIdComPersonagem(
                10L,
                "",
                PageRequest.of(0, 20)
        );
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum elenco for encontrado")
    void deveRetornarListaVaziaQuandoNenhumElencoForEncontrado() {
        when(serieAtorRepository.findElencoPorIdComPersonagem(10L, "Inexistente", Pageable.ofSize(20)))
                .thenReturn(new PageImpl<>(List.of()));

        List<OpcaoPesquisaElencoResponse> resultado = pesquisarElencoSerieService.pesquisarElenco(10L, "Inexistente");

        assertThat(resultado).isEmpty();
        verify(serieAtorRepository).findElencoPorIdComPersonagem(
                10L,
                "Inexistente",
                PageRequest.of(0, 20)
        );
    }

    @Test
    @DisplayName("Deve montar detalhes da serie com elenco, providers e favorito quando usuario estiver autenticado")
    void deveMontarDetalhesDaSerieComElencoProvidersEFavoritoQuandoUsuarioAutenticado() {
        UsuarioAutenticado usuarioAutenticado = get();
        Serie serie = getBreakingBadComId();
        OpcaoPesquisaElencoResponse opcao = getWalterWhite();
        ProviderDto provider = getDisneyPlus();

        when(obterSerieInicializadaService.obter(2L)).thenReturn(serie);
        when(serieAtorRepository.findElencoPorIdComPersonagem(2L, "Walter", Pageable.ofSize(20)))
                .thenReturn(new PageImpl<>(List.of(opcao)));
        when(providersService.buscarProviders(456L, TV)).thenReturn(List.of(provider));
        when(favoritarSerieService.estaFavoritadoComAuthId(usuarioAutenticado, 2L)).thenReturn(false);

        DetalhesProducaoComElenco resultado = pesquisarElencoSerieService.pesquisar(2L, "Walter", usuarioAutenticado);

        assertThat(resultado.getId()).isEqualTo(2L);
        assertThat(resultado.getNome()).isEqualTo("Breaking Bad");
        assertThat(resultado.getImagem()).isEqualTo("/breaking-bad.jpg");
        assertThat(resultado.getTipoMidia()).isEqualTo(TV);
        assertThat(resultado.getElenco()).containsExactly(opcao);
        assertThat(resultado.getProviders()).containsExactly(provider);
        assertThat(resultado.getEstaFavoritado()).isFalse();
    }

    @Test
    @DisplayName("Deve manter favorito nulo quando usuario nao estiver autenticado")
    void deveManterFavoritoNuloQuandoUsuarioNaoAutenticado() {
        Serie serie = getBreakingBadComId();

        when(obterSerieInicializadaService.obter(2L)).thenReturn(serie);
        when(serieAtorRepository.findElencoPorIdComPersonagem(2L, "", Pageable.ofSize(20)))
                .thenReturn(new PageImpl<>(List.of()));
        when(providersService.buscarProviders(456L, TV)).thenReturn(List.of());

        DetalhesProducaoComElenco resultado = pesquisarElencoSerieService.pesquisar(2L, "", null);

        assertThat(resultado.getEstaFavoritado()).isNull();
        verifyNoInteractions(favoritarSerieService);
    }
}
