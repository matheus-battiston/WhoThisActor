package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.mapper.ProducaoesAtorMapper;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
import com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.mapper.AtorEProducoesMapper.toResponse;
import static java.text.Normalizer.*;
import static java.text.Normalizer.Form.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class PesquisarProducoesAtorService {
    private static final String CONHECIDO_DEPARTAMENTO = "Acting";
    private static final int TAMANHO_LISTA = 30;
    private static final String ATOR_NAO_ENCONTRADO = "Ator nao foi encontrado";
    private static final int ZERO = 0;

    private final SerieAtorRepository serieAtorRepository;
    private final FilmeAtorRepository filmeAtorRepository;

    public ProducaoAtorResponse pesquisarProducoesDeAtorPorId(Long id) {
        Pageable pageable = of(ZERO, TAMANHO_LISTA);

        List<ProducaoComPersonagemResponse> filmes = filmeAtorRepository
                .findProducoesResponsePorAtor(id, pageable)
                .getContent()
                .stream()
                .map(PesquisarProducoesAtorService::normalizarPoster)
                .toList();

        List<ProducaoComPersonagemResponse> series = serieAtorRepository
                .findProducoesResponsePorAtor(id, pageable)
                .getContent()
                .stream()
                .map(PesquisarProducoesAtorService::normalizarPoster)
                .toList();

        return ProducaoesAtorMapper.toResponse(filmes, series);
    }

    AtorTMDBDtoPesquisaId getAtor(List<AtorTMDBDtoPesquisaId> atores, String nomeBuscado) {
        String alvo = normalizar(nomeBuscado);

        return atores.stream()
                .filter(ator -> CONHECIDO_DEPARTAMENTO.equals(ator.getConhecidoPor())).min(Comparator
                        .comparing((AtorTMDBDtoPesquisaId a) ->
                                !normalizar(a.getNome()).equals(alvo))

                        .thenComparing(a ->
                                !normalizar(a.getNome()).startsWith(alvo))

                        .thenComparing(a ->
                                !normalizar(a.getNome()).contains(alvo))

                        .thenComparing(AtorTMDBDtoPesquisaId::getPopularidade,
                                Comparator.nullsLast(Comparator.reverseOrder())))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, ATOR_NAO_ENCONTRADO));
    }

    AtorEProducoesResponse getAtorInfo(Ator ator) {
        ProducaoAtorResponse listaDeProducoes = pesquisarProducoesDeAtorPorId(ator.getId());

        return toResponse(
                listaDeProducoes,
                ator.getNome(),
                TmdbImagemUrl.normalizar(ator.getImagem()),
                ator.getId(),
                null
        );
    }

    private String normalizar(String s) {
        if (s == null) return "";
        String normalized = normalize(s, NFD);
        return normalized
                .replaceAll("\\p{M}", "") // remove acentos
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .toLowerCase()
                .trim();
    }

    private static ProducaoComPersonagemResponse normalizarPoster(ProducaoComPersonagemResponse producao) {
        return ProducaoComPersonagemResponse.builder()
                .id(producao.getId())
                .nomeProducao(producao.getNomeProducao())
                .nomePersonagem(producao.getNomePersonagem())
                .posterLink(TmdbImagemUrl.normalizar(producao.getPosterLink()))
                .build();
    }
}
