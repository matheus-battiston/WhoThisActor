package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.FilmeAtor;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.repository.FilmeAtorRepository;
import com.MovieParticipations.MovieParticipations.service.internal.ContextoElenco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class ProcessamentoFilmeService {
    private static final String NAO_TEM_ELENCO = "A serie nao tem elenco";

    private final FilmeAtorRepository filmeAtorRepository;
    private final ObterOuCriarAtoresService obterOuCriarAtoresService;


    public void processarElenco(Filme filmeEntity, List<AtorTMDBMovieDto> atoresDto) throws ResponseStatusException {
        if (atoresDto.isEmpty()) throw new ResponseStatusException(BAD_REQUEST, NAO_TEM_ELENCO);
        List<Ator> atores = obterOuCriarAtoresService.obter(atoresDto);

        ContextoElenco contextoElenco = ContextoElenco.criar(
                atores,
                filmeAtorRepository.findAtorIdsTmdbByProducaoId(filmeEntity.getId())
        );

        List<FilmeAtor> elenco = atoresDto.stream()
                .filter(atorDto -> !contextoElenco.atorJaRelacionado(atorDto.getId()))
                .map(atorDto -> criarProducaoAtor(filmeEntity, atorDto, contextoElenco))
                .toList();

        if (elenco.isEmpty()) return;

        filmeAtorRepository.saveAll(elenco);
    }

    private FilmeAtor criarProducaoAtor(
            Filme filme,
            AtorTMDBMovieDto atorDto,
            ContextoElenco contextoElenco
    ) {
        Ator ator = contextoElenco.buscarAtor(atorDto.getId());

        return FilmeAtor.builder()
                .filme(filme)
                .ator(ator)
                .personagem(atorDto.getPersonagem())
                .creditOrder(atorDto.getOrdemDeCredito())
                .build();
    }
}
