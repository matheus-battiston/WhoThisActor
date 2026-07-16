package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.MOVIE;
import static com.MovieParticipations.MovieParticipations.mapper.DetalhesProducaoComElencoMapper.toResponse;
import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;
import static org.springframework.data.domain.PageRequest.of;

@RequiredArgsConstructor
@Service
public class PesquisarElencoFilmeService {
    private static final String STRING_VAZIA = "";
    private static final int PAGINA = 0;
    private static final int TAMANHO_PAGINA = 20;

    private final FilmeAtorRepository filmeAtorRepository;
    private final ObterFilmeInicializadoService obterFilmeInicializadoService;
    private final ProvidersService providersService;
    private final FavoritarFilmeService favoritarFilmeService;

    public List<OpcaoPesquisaElencoResponse> pesquisarElenco(Long id, String filtroNome) {
        Pageable pageable = of(PAGINA, TAMANHO_PAGINA);
        if (filtroNome == null || filtroNome.isEmpty()) filtroNome = STRING_VAZIA;

        return filmeAtorRepository
                .findElencoPorIdComPersonagem(id, filtroNome, pageable)
                .getContent()
                .stream()
                .map(PesquisarElencoFilmeService::normalizarImagem)
                .toList();
    }

    public DetalhesProducaoComElenco pesquisar(Long idFilme, String personagem, UsuarioAutenticado usuarioAutenticado) {

        Boolean estaFavoritado = null;
        Filme filme = obterFilmeInicializadoService.obter(idFilme);
        List<OpcaoPesquisaElencoResponse> elenco = pesquisarElenco(idFilme, personagem);
        List<ProviderDto> providers = providersService.buscarProviders(filme.getIdTmdb(), MOVIE);

        if (usuarioAutenticado != null)
            estaFavoritado = favoritarFilmeService.estaFavoritadoComAuthId(usuarioAutenticado, filme.getId());

        return toResponse(elenco, filme, providers, estaFavoritado);

    }

    private static OpcaoPesquisaElencoResponse normalizarImagem(OpcaoPesquisaElencoResponse item) {
        return OpcaoPesquisaElencoResponse.builder()
                .nome(item.getNome())
                .urlImagem(normalizar(item.getUrlImagem()))
                .nomePersonagem(item.getNomePersonagem())
                .popularity(item.getPopularity())
                .id(item.getId())
                .build();
    }
}
