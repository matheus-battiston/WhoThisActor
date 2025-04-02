package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.TMDBDto;
import com.MovieParticipations.MovieParticipations.mapper.AtorMapper;
import com.MovieParticipations.MovieParticipations.mapper.SerieMapper;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import com.MovieParticipations.MovieParticipations.validator.ExisteAtorNoDBPorNome;
import com.MovieParticipations.MovieParticipations.validator.ExisteSerieNoDBValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdicionarSerieService {

    private static final String NAO_TEM_ELENCO = "A serie nao tem elenco";

    @Autowired
    SerieRepository serieRepository;
    @Autowired
    SerieAtorRepository serieAtorRepository;
    @Autowired
    AtorRepository atorRepository;
    @Autowired
    BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;
    @Autowired
    BuscarElencoService buscarElencoService;
    @Autowired
    ExisteAtorNoDBPorNome existeAtorNoDBPorNome;
    @Autowired
    ExisteSerieNoDBValidator existeSerieNoDBValidator;

    @Transactional
    public String adicionarSerieComNome(String nomeSerie, TipoMidia tipoMidia){
        TMDBDto dto = buscarProducaoPorNomeTMBDService.buscarIdPorNome(nomeSerie, tipoMidia);
        Serie serieEntity = SerieMapper.toEntity(dto);

        if (existeSerieNoDBValidator.ExisteSerieComNome(serieEntity.getTitulo(), tipoMidia))
            return serieEntity.getTitulo();

        List<AtorTMDBDto> atores = buscarElencoService.pesquisarElenco(dto.getId(), tipoMidia);
        if (atores.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NAO_TEM_ELENCO);
        List<String> nomes = atores.stream().map(AtorTMDBDto::getName).toList();
        List<Ator> atoresNoBanco = atorRepository.findAtorByNomeIn(nomes);
        List<Ator> novosAtores = obterNovosAtores(atores, atoresNoBanco);

        atoresNoBanco.addAll(novosAtores);
        atorRepository.saveAll(novosAtores);

        List<SerieAtor> elenco = atores.stream()
                .map(atorDto -> criarSerieAtor(serieEntity, atorDto, atorDto.getCharacter(), atoresNoBanco))
                .toList();
        serieAtorRepository.saveAll(elenco);

        return serieEntity.getTitulo();
    }

    private SerieAtor criarSerieAtor(Serie serie, AtorTMDBDto atorDto, String personagem, List<Ator> atoresNoBanco){

        Ator ator = atoresNoBanco.stream()
                .filter(a -> a.getNome().equalsIgnoreCase(atorDto.getName()))
                .findFirst()
                .orElseThrow();

        return SerieAtor.builder()
                .serie(serie)
                .ator(ator)
                .personagem(personagem)
                .build();
    }

    private List<Ator> obterNovosAtores(List<AtorTMDBDto> atoresDto, List<Ator> atoresNoBanco) {
        return atoresDto.stream()
                .filter(atorDto -> atoresNoBanco.stream()
                        .noneMatch(ator -> ator.getNome().equalsIgnoreCase(atorDto.getName())))
                .map(AtorMapper::toEntity)
                .toList();
    }
}
