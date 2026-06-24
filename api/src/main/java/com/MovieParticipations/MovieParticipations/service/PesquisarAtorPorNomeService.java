package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaAtorResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.dto.ContratoRespostaDTO;
import com.MovieParticipations.MovieParticipations.dto.PesquisaIdPorNomeDTO;
import com.MovieParticipations.MovieParticipations.mapper.AtorMapper;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.mapper.AtorMapper.toInfo;
import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class PesquisarAtorPorNomeService {

    private static final String ATOR_NAO_ENCONTRADO = "Ator nao foi encontrado";

    private final AtorRepository atorRepository;
    private final AdicionarAtorService adicionarAtorService;
    private final RequisicaoApiService requisicaoApiService;
    private final PesquisarProducoesAtorService pesquisarProducoesAtorService;

    public PesquisaAtorResponse pesquisarInfosPorNome(String nomeAtor) {
        List<Ator> atoresEncontrados = atorRepository.findAtorByNomeNormalizado(normalizar(nomeAtor));

        if (atoresEncontrados.isEmpty()) return respostaUnica(adicionarNovoAtorComoInfo(nomeAtor));
        if (!atoresEncontrados.get(0).getInicializado()) adicionarAtorService.processarAtor(atoresEncontrados.get(0));

        List<InfoAtorResponse> atores = atoresEncontrados.stream()
                .map(AtorMapper::toInfo)
                .toList();

        ContratoRespostaDTO contrato = definirContrato(atores);

        return construirResposta(contrato, atores);
    }

    private InfoAtorResponse adicionarNovoAtorComoInfo(String nomeAtor) {
        AtorTMDBDtoPesquisaId atorExterno = pesquisarIdPorNome(nomeAtor);
        Ator novoAtor = adicionarAtorService.adicionarAtor(atorExterno);
        return toInfo(novoAtor);
    }

    private PesquisaAtorResponse respostaUnica(InfoAtorResponse ator) {
        return construirResposta(ContratoRespostaDTO.UNIQUE_MATCH, List.of(ator));
    }

    private PesquisaAtorResponse construirResposta(ContratoRespostaDTO contrato, List<InfoAtorResponse> atores) {
        return PesquisaAtorResponse.builder()
                .contratoResposta(contrato)
                .atores(atores)
                .build();
    }

    private ContratoRespostaDTO definirContrato(List<InfoAtorResponse> atores) {
        return atores.size() == 1
                ? ContratoRespostaDTO.UNIQUE_MATCH
                : ContratoRespostaDTO.MULTIPLE_MATCH;
    }

    public AtorTMDBDtoPesquisaId pesquisarIdPorNome(String nomeAtor) {

        PesquisaIdPorNomeDTO response = requisicaoApiService.persquisarIdPorNome(nomeAtor);
        if (response.getResultados() != null && !response.getResultados().isEmpty())
            return pesquisarProducoesAtorService.getAtor(response.getResultados(), nomeAtor);
        else throw new ResponseStatusException(BAD_REQUEST, ATOR_NAO_ENCONTRADO);
    }
}
