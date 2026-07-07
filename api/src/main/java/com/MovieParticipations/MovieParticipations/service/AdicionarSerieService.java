package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;
import com.MovieParticipations.MovieParticipations.mapper.SerieMapper;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;
import static java.time.LocalDate.*;
import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Service
public class AdicionarSerieService {

    private final SerieRepository serieRepository;
    private final ProcessamentoSerieService processamentoSerieService;
    private final BuscarElencoService buscarElencoService;
    private final BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;
    private final RecarregarCacheClassificacaoService recarregarCacheClassificacaoService;

    @Transactional
    public List<Serie> adicionarSeries(List<ProducaoTMDBDto> producoesDto) {
        List<Serie> producoes = producoesDto.stream()
                .map(SerieMapper::toEntity)
                .toList();

        return serieRepository.saveAll(producoes);
    }

    @Transactional
    public void adicionarElenco(Serie serieEntity) {

        List<AtorTMDBSerieDto> atores = buscarElencoService.pesquisaElencoSerie(serieEntity.getIdTmdb());
        List<AtorTMDBSerieDto> atoresSemDuplicado = deduplicarAtoresDto(atores);

        if (atoresSemDuplicado.isEmpty()) return;

        processamentoSerieService.processarElenco(serieEntity, atoresSemDuplicado);
        serieEntity.setElencoInicializado(true);
        serieRepository.save(serieEntity);
        recarregarCacheClassificacaoService.recarregarCacheAtoresPorProducao();
    }

    @Transactional
    public void adicionarSerieComNome(String nome) {
        List<SerieTMDBDto> dtos = buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(nome);
        salvarOuAtualizarSeriesBasicas(dtos);

    }

    private void salvarOuAtualizarSeriesBasicas(List<SerieTMDBDto> producoes) {
        List<Long> idsTmdb = producoes.stream()
                .map(SerieTMDBDto::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (idsTmdb.isEmpty()) return;

        Map<Long, Serie> seriesPorIdTmdb = serieRepository.findByIdTmdbIn(idsTmdb).stream()
                .collect(toMap(Serie::getIdTmdb, Function.identity()));

        List<Serie> seriesParaSalvar = producoes.stream()
                .filter(producao -> producao.getId() != null)
                .map(producao -> obterOuAtualizarSerieBasica(producao, seriesPorIdTmdb))
                .toList();

        serieRepository.saveAll(seriesParaSalvar)
                .forEach(serie -> seriesPorIdTmdb.put(serie.getIdTmdb(), serie));

    }

    private Serie obterOuAtualizarSerieBasica(SerieTMDBDto producao, Map<Long, Serie> seriesPorIdTmdb) {
        Serie serie = seriesPorIdTmdb.get(producao.getId());
        if (serie == null) return SerieMapper.toEntity(producao);

        atualizarSerieBasica(serie, producao);
        return serie;
    }

    private void atualizarSerieBasica(Serie serie, SerieTMDBDto producao) {
        serie.setTitulo(producao.getNome());
        serie.setTituloNormalizado(normalizar(producao.getNome()));
        serie.setImagem(producao.getImagemPoster());
        serie.setPopularidade(producao.getPopularidade());
        serie.setUltimaAtualizacao(now());
    }

    private List<AtorTMDBSerieDto> deduplicarAtoresDto(List<AtorTMDBSerieDto> atoresDto) {
        return new ArrayList<>(
                atoresDto.stream()
                        .collect(toMap(
                                AtorTMDBSerieDto::getId,
                                Function.identity(),
                                this::escolherMelhorCredito
                        ))
                        .values()
        );
    }

    private AtorTMDBSerieDto escolherMelhorCredito(AtorTMDBSerieDto a1, AtorTMDBSerieDto a2) {
        String p1 = a1.getPapeis().get(0).getPersonagem().trim();
        String p2 = a2.getPapeis().get(0).getPersonagem().trim();

        return p2.length() > p1.length() ? a2 : a1;
    }
}
