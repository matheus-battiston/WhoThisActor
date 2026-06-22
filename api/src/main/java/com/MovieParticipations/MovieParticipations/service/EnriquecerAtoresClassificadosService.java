package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.MovieParticipations.MovieParticipations.util.NormalizadorDeString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.MovieParticipations.MovieParticipations.mapper.AtorClassificadoMapper.toResponse;
import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;

@RequiredArgsConstructor
@Service
public class EnriquecerAtoresClassificadosService {

    private final PesquisarAtorPorNomeService pesquisarAtorPorNomeService;
    private final AtorRepository atorRepository;
    private final SalvarAtoresClassificadosService salvarAtoresClassificadosService;

    public List<OpcoesAtoresParecidosResponse> enriquecerComDadosTmdb(List<OpcoesAtoresParecidosResponse> atores) {
        List<OpcoesAtoresParecidosResponse> atoresSemDuplicados = removerDuplicadosPorIdentidade(atores);
        List<OpcoesAtoresParecidosResponse> atoresComDadosDoBanco = preencherDadosDoBanco(atoresSemDuplicados);

        List<OpcoesAtoresParecidosResponse> atoresNovos = atoresComDadosDoBanco.stream()
                .filter(ator -> ator.getId() == null)
                .map(this::enriquecerComDadosTmdb)
                .toList();

        salvarAtoresClassificadosService.salvarNovosEPreencherIds(atoresNovos);

        return juntarAtores(atoresComDadosDoBanco, atoresNovos);
    }

    private List<OpcoesAtoresParecidosResponse> removerDuplicadosPorIdentidade(
            List<OpcoesAtoresParecidosResponse> atores
    ) {
        Map<String, OpcoesAtoresParecidosResponse> melhorAtorPorIdentidade = atores.stream()
                .filter(ator -> ator.getIdentidade() != null)
                .collect(Collectors.toMap(
                        OpcoesAtoresParecidosResponse::getIdentidade,
                        Function.identity(),
                        this::manterAtorComMenorDistancia,
                        LinkedHashMap::new
                ));

        return atores.stream()
                .filter(ator -> ator.getIdentidade() == null || melhorAtorPorIdentidade.get(ator.getIdentidade()) == ator)
                .toList();
    }

    private OpcoesAtoresParecidosResponse manterAtorComMenorDistancia(
            OpcoesAtoresParecidosResponse atorExistente,
            OpcoesAtoresParecidosResponse atorDuplicado
    ) {
        return atorExistente.getDistanciaMedia() <= atorDuplicado.getDistanciaMedia()
                ? atorExistente
                : atorDuplicado;
    }

    private List<OpcoesAtoresParecidosResponse> preencherDadosDoBanco(List<OpcoesAtoresParecidosResponse> atores) {
        List<String> nomesNormalizados = atores.stream()
                .map(OpcoesAtoresParecidosResponse::getIdentidade)
                .filter(Objects::nonNull)
                .map(NormalizadorDeString::normalizar)
                .distinct()
                .toList();

        if (nomesNormalizados.isEmpty()) return atores;

        Map<String, Ator> atoresPorNomeNormalizado = atorRepository.findByNomeNormalizadoIn(nomesNormalizados).stream()
                .collect(Collectors.toMap(
                        Ator::getNomeNormalizado,
                        Function.identity(),
                        (atorExistente, atorDuplicado) -> atorExistente
                ));

        return atores.stream()
                .map(atorResponse -> preencherDadosDoBanco(atorResponse, atoresPorNomeNormalizado))
                .toList();
    }

    private OpcoesAtoresParecidosResponse preencherDadosDoBanco(
            OpcoesAtoresParecidosResponse atorResponse,
            Map<String, Ator> atoresPorNomeNormalizado
    ) {
        if (atorResponse.getIdentidade() == null) return atorResponse;
        Ator ator = atoresPorNomeNormalizado.get(normalizar(atorResponse.getIdentidade()));
        if (ator == null) return atorResponse;
        return toResponse(atorResponse, ator);
    }

    private OpcoesAtoresParecidosResponse enriquecerComDadosTmdb(OpcoesAtoresParecidosResponse ator) {
        AtorTMDBDtoPesquisaId pesquisa = pesquisarAtorPorNomeService.pesquisarIdPorNome(ator.getIdentidade());
        return toResponse(ator, pesquisa);
    }

    private List<OpcoesAtoresParecidosResponse> juntarAtores(
            List<OpcoesAtoresParecidosResponse> atores,
            List<OpcoesAtoresParecidosResponse> atoresNovos
    ) {
        Map<String, OpcoesAtoresParecidosResponse> atoresNovosPorNome = atoresNovos.stream()
                .collect(Collectors.toMap(
                        OpcoesAtoresParecidosResponse::getIdentidade,
                        Function.identity(),
                        this::manterAtorComMenorDistancia
                ));

        return atores.stream()
                .map(ator -> atoresNovosPorNome.getOrDefault(ator.getIdentidade(), ator))
                .toList();
    }
}
