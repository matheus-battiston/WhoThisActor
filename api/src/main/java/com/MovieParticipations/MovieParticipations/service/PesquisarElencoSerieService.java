package com.MovieParticipations.MovieParticipations.service;


import com.MovieParticipations.MovieParticipations.controller.response.DetalhesProducaoComElenco;
import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.mapper.DetalhesProducaoComElencoMapper.toResponse;
import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;
import static org.springframework.data.domain.PageRequest.of;

@RequiredArgsConstructor
@Service
public class PesquisarElencoSerieService {

    private static final String STRING_VAZIA = "";
    private static final int PAGINA = 0;
    private static final int TAMANHO_PAGINA = 20;

    private final SerieAtorRepository serieAtorRepository;
    private final FavoritarSerieService favoritarSerieService;
    private final ObterSerieInicializadaService obterSerieInicializadaService;
    private final ProvidersService providersService;

    public List<OpcaoPesquisaElencoResponse> pesquisarElenco(Long id, String filtroNome) {
        Pageable pageable = of(PAGINA, TAMANHO_PAGINA);
        if (filtroNome == null || filtroNome.isEmpty()) filtroNome = STRING_VAZIA;

        return serieAtorRepository
                .findElencoPorIdComPersonagem(id, filtroNome, pageable)
                .getContent()
                .stream()
                .map(PesquisarElencoSerieService::normalizarImagem)
                .toList();
    }

    public DetalhesProducaoComElenco pesquisar(Long idSerie, String personagem, UsuarioAutenticado usuarioAutenticado) {

        Boolean estaFavoritado = null;
        Serie serie = obterSerieInicializadaService.obter(idSerie);
        List<OpcaoPesquisaElencoResponse> elenco = pesquisarElenco(idSerie, personagem);
        List<ProviderDto> providers = providersService.buscarProviders(serie.getIdTmdb(), TipoMidia.TV);

        if (usuarioAutenticado != null)
            estaFavoritado = favoritarSerieService.estaFavoritadoComAuthId(usuarioAutenticado, serie.getId());

        return toResponse(elenco, serie, providers, estaFavoritado);

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
