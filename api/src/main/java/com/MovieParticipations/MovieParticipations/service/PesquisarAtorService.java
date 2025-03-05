package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaPorNomeResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.dto.ListaProducoesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.PesquisaIdPorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.MovieParticipations.MovieParticipations.domain.TipoProducao.TV;
import static com.MovieParticipations.MovieParticipations.mapper.PesquisaMapper.toResponse;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

@Service
public class PesquisarAtorService {
    private static final String URL_BASE_IMAGEM = "https://image.tmdb.org/t/p/w200";
    private static final String ATOR_NAO_ENCONTRADO = "Ator nao foi encontrado";
    private static final String CONHECIDO_DEPARTAMENTO = "Acting";
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

        PesquisaIdPorNomeDTO response = requisicaoApiService.persquisarIdPorNome(nomeAtor);
        if (response.getResults() != null && !response.getResults().isEmpty()) {
            AtorTMDBDtoPesquisaId ator = getAtor(response.getResults());
            if (ator != null) return toResponse(ator);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ATOR_NAO_ENCONTRADO);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ATOR_NAO_ENCONTRADO);
    }

    public List<ProducaoResponse> pesquisarFilmesDeAtorPorId(int id, String nome) {
        ListaProducoesTMDBDto response = requisicaoApiService.pesquisarFilmesDoAtorPorId(id);

        return Optional.ofNullable(response.getCast())
                .orElse(emptyList())
                .stream()
                .map(FilmeMapper::toResponse)
                .filter(producao -> producao.getNomeProducao() != null &&
                        !checarNomePersonagem.checarSeAutoAtuacao(producao.getNomePersonagem(), nome))
                .collect(partitioningBy(producao -> producao.getTipoProducao().equals(TV)))
                .values()
                .stream()
                .flatMap(lista -> ordenarELimitar(lista).stream())
                .collect(toList());
    }

    private List<ProducaoResponse> ordenarELimitar(List<ProducaoResponse> listaDeProducoes) {
        return listaDeProducoes.stream()
                .sorted(Comparator.comparing(ProducaoResponse::getPopularidade).reversed())
                .limit(TAMANHO_LISTA)
                .collect(toList());
    }

    private AtorTMDBDtoPesquisaId getAtor(List<AtorTMDBDtoPesquisaId> response) {
        return response == null ? null :
                response.stream()
                        .filter(ator -> CONHECIDO_DEPARTAMENTO.equals(ator.getKnown_for_department()))
                        .max(Comparator.comparingDouble(AtorTMDBDtoPesquisaId::getPopularity))
                        .orElse(null);
    }
}
