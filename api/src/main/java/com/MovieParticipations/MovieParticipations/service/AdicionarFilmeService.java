package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.mapper.FilmeMapper;
import com.MovieParticipations.MovieParticipations.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class AdicionarFilmeService {
    private static final String NAO_TEM_ELENCO = "Nao tem elenco";

    private final FilmeRepository filmeRepository;
    private final BuscarElencoService buscarElencoService;
    private final ProcessamentoFilmeService processamentoFilmeService;
    private final BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;
    private final RecarregarCacheClassificacaoService recarregarCacheClassificacaoService;

    @Transactional
    public List<Filme> adicionarFilmes(List<ProducaoTMDBDto> producoesDto) {
        List<Filme> producoes = producoesDto.stream()
                .map(FilmeMapper::toEntity)
                .toList();

        return filmeRepository.saveAll(producoes);
    }

    @Transactional
    public void adicionarElenco(Filme filmeEntity) {

        List<AtorTMDBMovieDto> atores = buscarElencoService.pesquisaElencoFilme(filmeEntity.getIdTmdb());

        if (atores.isEmpty()) throw new ResponseStatusException(BAD_REQUEST, NAO_TEM_ELENCO);

        processamentoFilmeService.processarElenco(filmeEntity, atores);
        filmeEntity.setInicializado(true);
        filmeRepository.save(filmeEntity);
        recarregarCacheClassificacaoService.recarregarCacheAtoresPorProducao();
    }

    @Transactional
    public void adicionarFilmeComNome(String nome){
        List<FilmeTMDBDto> dtos = buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(nome);
        salvarOuAtualizarFilmesBasicos(dtos);
    }

    private void salvarOuAtualizarFilmesBasicos(List<FilmeTMDBDto> producoes) {
        List<Long> idsTmdb = producoes.stream()
                .map(FilmeTMDBDto::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (idsTmdb.isEmpty()) return;

        Map<Long, Filme> filmesPorIdTmdb = filmeRepository.findByIdTmdbIn(idsTmdb).stream()
                .collect(Collectors.toMap(Filme::getIdTmdb, Function.identity()));

        List<Filme> filmesParaSalvar = producoes.stream()
                .filter(producao -> producao.getId() != null)
                .map(producao -> obterOuAtualizarFilmeBasico(producao, filmesPorIdTmdb))
                .toList();

        filmeRepository.saveAll(filmesParaSalvar)
                .forEach(filme -> filmesPorIdTmdb.put(filme.getIdTmdb(), filme));
    }

    private Filme obterOuAtualizarFilmeBasico(FilmeTMDBDto producao, Map<Long, Filme> filmesPorIdTmdb) {
        Filme filme = filmesPorIdTmdb.get(producao.getId());
        if (filme == null) return FilmeMapper.toEntity(producao);

        filme.setTitulo(producao.getTitulo());
        filme.setTituloNormalizado(normalizar(producao.getTitulo()));
        filme.setImagem(producao.getImagemPoster());
        filme.setPopularidade(producao.getPopularidade());
        filme.setUltimaAtualizacao(LocalDate.now());
        return filme;
    }
}
