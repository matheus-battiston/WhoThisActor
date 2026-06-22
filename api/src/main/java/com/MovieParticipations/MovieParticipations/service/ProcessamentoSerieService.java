package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.repository.SerieAtorRepository;
import com.MovieParticipations.MovieParticipations.service.internal.ContextoElenco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
class ProcessamentoSerieService {

    private static final String NAO_TEM_ELENCO = "A serie nao tem elenco";

    private final SerieAtorRepository serieAtorRepository;
    private final ObterOuCriarAtoresService obterOuCriarAtoresService;

    public void processarElenco(Serie serieEntity, List<AtorTMDBSerieDto> atoresDto) throws ResponseStatusException {
        if (atoresDto.isEmpty()) throw new ResponseStatusException(BAD_REQUEST, NAO_TEM_ELENCO);
        List<Ator> atores = obterOuCriarAtoresService.obter(atoresDto);

        ContextoElenco contextoElenco = ContextoElenco.criar(
                atores,
                serieAtorRepository.findAtorIdsTmdbByProducaoId(serieEntity.getId())
        );

        List<SerieAtor> elenco = atoresDto.stream()
                .filter(atorDto -> !contextoElenco.atorJaRelacionado(atorDto.getId()))
                .map(atorDto -> criarProducaoAtor(serieEntity, atorDto, contextoElenco))
                .toList();

        if (elenco.isEmpty()) return;
        serieAtorRepository.saveAll(elenco);
    }


    private SerieAtor criarProducaoAtor(
            Serie serie,
            AtorTMDBSerieDto atorDto,
            ContextoElenco contextoElenco
    ) {
        Ator ator = contextoElenco.buscarAtor(atorDto.getId());

        return SerieAtor.builder()
                .serie(serie)
                .ator(ator)
                .personagem(atorDto.getPapeis().get(0).getPersonagem())
                .numeroEpisodios(atorDto.getPapeis().get(0).getNumeroEpisodios())
                .build();
    }
}
