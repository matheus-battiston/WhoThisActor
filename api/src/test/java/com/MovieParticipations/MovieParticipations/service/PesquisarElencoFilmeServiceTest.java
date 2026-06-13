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
import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.OpcaoPesquisaElencoResponseFactory.getNeo;
import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getNetflix;
import static com.MovieParticipations.MovieParticipations.factories.UsuarioAutenticadoFactory.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisarElencoFilmeService")
class PesquisarElencoFilmeServiceTest {

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
        OpcaoPesquisaElencoResponse opcao = getNeo();
        when(filmeAtorRepository.findElencoPorIdComPersonagem(10L, "", Pageable.ofSize(20)))
                .thenReturn(new PageImpl<>(List.of(opcao)));

        List<OpcaoPesquisaElencoResponse> resultado = pesquisarElencoFilmeService.pesquisarElenco(10L, null);

        assertThat(resultado).containsExactly(opcao);
        verify(filmeAtorRepository).findElencoPorIdComPersonagem(
                10L,
                "",
                PageRequest.of(0, 20)
        );
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum elenco for encontrado")
    void deveRetornarListaVaziaQuandoNenhumElencoForEncontrado() {
        when(filmeAtorRepository.findElencoPorIdComPersonagem(10L, "Inexistente", Pageable.ofSize(20)))
                .thenReturn(new PageImpl<>(List.of()));

        List<OpcaoPesquisaElencoResponse> resultado = pesquisarElencoFilmeService.pesquisarElenco(10L, "Inexistente");

        assertThat(resultado).isEmpty();
        verify(filmeAtorRepository).findElencoPorIdComPersonagem(
                10L,
                "Inexistente",
                PageRequest.of(0, 20)
        );
    }

    @Test
    @DisplayName("Deve montar detalhes do filme com elenco, providers e favorito quando usuario estiver autenticado")
    void deveMontarDetalhesDoFilmeComElencoProvidersEFavoritoQuandoUsuarioAutenticado() {
        UsuarioAutenticado usuarioAutenticado = get();
        Filme filme = getMatrixComId();
        OpcaoPesquisaElencoResponse opcao = getNeo();
        ProviderDto provider = getNetflix();

        when(obterFilmeInicializadoService.obter(1L)).thenReturn(filme);
        when(filmeAtorRepository.findElencoPorIdComPersonagem(1L, "Neo", Pageable.ofSize(20)))
                .thenReturn(new PageImpl<>(List.of(opcao)));
        when(providersService.buscarProviders(123L, MOVIE)).thenReturn(List.of(provider));
        when(favoritarFilmeService.estaFavoritadoComAuthId(usuarioAutenticado, 1L)).thenReturn(true);

        DetalhesProducaoComElenco resultado = pesquisarElencoFilmeService.pesquisar(1L, "Neo", usuarioAutenticado);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Matrix");
        assertThat(resultado.getImagem()).isEqualTo("/matrix.jpg");
        assertThat(resultado.getElenco()).containsExactly(opcao);
        assertThat(resultado.getProviders()).containsExactly(provider);
        assertThat(resultado.getEstaFavoritado()).isTrue();
        verify(providersService).buscarProviders(123L, MOVIE);
    }

    @Test
    @DisplayName("Deve manter favorito nulo quando usuario nao estiver autenticado")
    void deveManterFavoritoNuloQuandoUsuarioNaoAutenticado() {
        Filme filme = getMatrixComId();

        when(obterFilmeInicializadoService.obter(1L)).thenReturn(filme);
        when(filmeAtorRepository.findElencoPorIdComPersonagem(1L, "", Pageable.ofSize(20)))
                .thenReturn(new PageImpl<>(List.of()));
        when(providersService.buscarProviders(123L, MOVIE)).thenReturn(List.of());

        DetalhesProducaoComElenco resultado = pesquisarElencoFilmeService.pesquisar(1L, "", null);

        assertThat(resultado.getEstaFavoritado()).isNull();
        verifyNoInteractions(favoritarFilmeService);
    }
}
