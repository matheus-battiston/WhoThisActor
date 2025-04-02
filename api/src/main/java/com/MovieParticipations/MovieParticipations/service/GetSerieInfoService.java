package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.SerieInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.mapper.SerieMapper;
import com.MovieParticipations.MovieParticipations.repository.SerieRepository;
import com.MovieParticipations.MovieParticipations.validator.ExisteSerieNoDBValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetSerieInfoService {
    @Autowired
    SerieRepository serieRepository;
    @Autowired
    ExisteSerieNoDBValidator existeSerieNoDBValidator;
    @Autowired
    ProvidersService providersService;
    @Autowired
    AdicionarSerieService adicionarSerieService;

    public SerieInfoResponse getSerieInfo(String nomeDaSerie, TipoMidia tipo){
        String nomeOficial = nomeDaSerie;
        if (!existeSerieNoDBValidator.ExisteSerieComNome(nomeDaSerie, tipo))
            nomeOficial = adicionarSerieService.adicionarSerieComNome(nomeDaSerie, tipo);

        Serie serie = serieRepository.findByTituloIgnoreCaseAndTipo(nomeOficial, tipo);
        List<ProviderDto> providerDtos = providersService.buscarProvidersDto(serie.getIdTmdb(), tipo);

        return SerieMapper.toResponse(serie, providerDtos);
    }
}
