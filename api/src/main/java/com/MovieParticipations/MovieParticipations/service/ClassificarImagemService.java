package com.MovieParticipations.MovieParticipations.service;


import com.MovieParticipations.MovieParticipations.controller.request.ClassificarImgemRequest;
import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaPorNomeResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.MovieParticipations.MovieParticipations.mapper.OpcoesAtoresMapper.toResponse;

@Service
public class ClassificarImagemService {
    private static final String URL_BASE_IMAGEM = "https://image.tmdb.org/t/p";
    private static final String TAMANHO_IMAGEM = "/w200";

    @Autowired
    private RequisicaoApiClassificacaoService requisicaoApiClassificacaoService;

    @Autowired
    private PesquisarAtorService pesquisarAtorService;

    public List<OpcoesAtoresParecidosResponse> classificarImagem(ClassificarImgemRequest request) {
        List<OpcoesAtoresParecidosResponse> response = new ArrayList<>();

        JsonArray responseClassificacao = requisicaoApiClassificacaoService.classificarImagem(request.getUrl(), request.isFast());

        for (JsonElement element : responseClassificacao) {
            response.add(toResponse(element));
        }

        for (OpcoesAtoresParecidosResponse ator : response){
            PesquisaPorNomeResponse pesquisaPorNomeResponse = pesquisarAtorService.pesquisarIdPorNome(ator.getIdentity());
            ator.setImagem(configurarUrl(pesquisaPorNomeResponse.getUrlImagem()));
            ator.setId(pesquisaPorNomeResponse.getId());
        }
        return response;
    }

    public String configurarUrl(String urlRelativo){
        return URL_BASE_IMAGEM + TAMANHO_IMAGEM + urlRelativo;
    }
}

