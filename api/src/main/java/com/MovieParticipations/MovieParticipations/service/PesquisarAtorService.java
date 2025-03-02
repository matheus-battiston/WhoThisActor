package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaPorNomeResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.mapper.AtorEProducoesMapper;
import com.MovieParticipations.MovieParticipations.mapper.FilmeMapper;
import com.MovieParticipations.MovieParticipations.validator.ChecarNomePersonagem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.MovieParticipations.MovieParticipations.domain.TipoProducao.TV;
import static com.MovieParticipations.MovieParticipations.mapper.PesquisaMapper.toResponse;
import static java.util.stream.Collectors.toList;

@Service
public class PesquisarAtorService {
    private static final String URL_BASE_IMAGEM = "https://image.tmdb.org/t/p/w200";
    private static final String PARAMETRO_RESULTADOS = "results";
    private static final String PARAMETRO_CAST = "cast";
    private static final String PARAMETRO_ID = "id";
    private static final String ATOR_NAO_ENCONTRADO = "Ator nao foi encontrado";
    private static final String PARAMETRO_URL_IMAGE = "profile_path";
    private static final String CONHECIDO_DEPARTAMENTO = "Acting";
    private static final String DEPARTAMENTO = "known_for_department";
    private static final int TAMANHO_LISTA = 30;

    @Autowired
    private RequisicaoApiService requisicaoApiService;

    @Autowired
    private ChecarNomePersonagem checarNomePersonagem;

    public AtorEProducoesResponse pesquisarProducoesPorNome(String nomeAtor) {
        PesquisaPorNomeResponse response = pesquisarIdPorNome(nomeAtor);
        List<ProducaoResponse> listaDeProducoes = pesquisarFilmesDeAtorPorId(response.getId(), response.getNome());

        return AtorEProducoesMapper.toResponse(listaDeProducoes, nomeAtor, URL_BASE_IMAGEM + response.getUrlImagem());
    }

    public PesquisaPorNomeResponse pesquisarIdPorNome(String nomeAtor) {

        JsonArray results = requisicaoApiService.persquisarIdPorNome(nomeAtor).getAsJsonArray(PARAMETRO_RESULTADOS);
        int id;
        String urlImage;

        if (results != null && !results.isEmpty()) {
            JsonObject ator = getAtor(results);
            if (ator != null) {
                id = ator.get(PARAMETRO_ID).getAsInt();
                if (ator.has(PARAMETRO_URL_IMAGE) && !ator.get(PARAMETRO_URL_IMAGE).isJsonNull()) {
                    urlImage = ator.get(PARAMETRO_URL_IMAGE).getAsString();
                } else {
                    // Defina um valor padrão ou trate o caso de parâmetro nulo
                    urlImage = "";  // ou null, ou algum valor de fallback
                }
                return toResponse(id, nomeAtor, urlImage);
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ATOR_NAO_ENCONTRADO);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ATOR_NAO_ENCONTRADO);
    }

    public List<ProducaoResponse> pesquisarFilmesDeAtorPorId(int id, String nome) {
        JsonArray results = requisicaoApiService.pesquisarFilmesDoAtorPorId(id).getAsJsonArray(PARAMETRO_CAST);
        List<ProducaoResponse> filmes = new ArrayList<>();
        List<ProducaoResponse> tv = new ArrayList<>();

        if (results != null && !results.isEmpty()) {
            for (JsonElement elemento : results) {
                JsonObject filmeApi = elemento.getAsJsonObject();
                ProducaoResponse filme;
                filme = FilmeMapper.toResponse(filmeApi);
                if (filme.getNomeProducao() != null && !checarNomePersonagem.checarSeAutoAtuacao(filme.getNomePersonagem(), nome))
                    if (filme.getTipoProducao().equals(TV))
                        tv.add(filme);
                    else
                        filmes.add(filme);
            }
        }
        return Stream.concat(ordenarELimitar(filmes).stream(), ordenarELimitar(tv).stream())
                .collect(toList());
    }

    private List<ProducaoResponse> ordenarELimitar(List<ProducaoResponse> listaDeProducoes) {
        return listaDeProducoes.stream()
                .sorted(Comparator.comparing(ProducaoResponse::getPopularidade).reversed())  // Ordena por popularidade de forma decrescente
                .limit(TAMANHO_LISTA)
                .collect(toList());
    }

    private JsonObject getAtor(JsonArray results) {

        if (results != null && !results.isEmpty()) {
            // Lista para armazenar os atores filtrados
            ArrayList<JsonObject> filteredActors = new ArrayList<>();

            // Filtrar os atores que têm "known_for_department" igual a "Acting"
            for (int i = 0; i < results.size(); i++) {
                JsonObject ator = results.get(i).getAsJsonObject();
                if (ator.has(DEPARTAMENTO) && ator.get(DEPARTAMENTO).getAsString().equals(CONHECIDO_DEPARTAMENTO)) {
                    filteredActors.add(ator);
                }
            }
            if (!filteredActors.isEmpty()) {
                filteredActors.sort((ator1, ator2) -> Double.compare(ator2.get("popularity").getAsDouble(), ator1.get("popularity").getAsDouble()));
                return filteredActors.get(0);
            }
        }
        return null;  // Retorna null se não encontrar nenhum ator com a condição
    }
}
