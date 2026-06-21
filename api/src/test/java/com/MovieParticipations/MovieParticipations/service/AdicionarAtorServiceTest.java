package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBDtoPesquisaIdFactory.getKeanuReevesTMDBDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdicionarAtorService")
class AdicionarAtorServiceTest {
    private static final Long ID_TMDB_KEANU_REEVES = 6384L;
    private static final String NOME_KEANU_REEVES = "Keanu Reeves";
    private static final String NOME_NORMALIZADO_KEANU_REEVES = "keanu reeves";
    private static final String IMAGEM_KEANU_REEVES = "/keanu.jpg";
    private static final Double POPULARIDADE_KEANU_REEVES = 10.0;
    private static final boolean NAO_INICIALIZADO = false;

    @Mock
    private AtorRepository atorRepository;

    @Mock
    private AdicionarProducoesAtorService adicionarProducoesAtorService;

    @InjectMocks
    private AdicionarAtorService adicionarAtorService;

    @Test
    @DisplayName("Deve adicionar ator a partir do retorno do TMDB")
    void deveAdicionarAtorAPartirDoRetornoDoTmdb() {
        AtorTMDBDtoPesquisaId response = getKeanuReevesTMDBDto();

        Ator resultado = adicionarAtorService.adicionarAtor(response);

        assertThat(resultado.getIdTmdb()).isEqualTo(ID_TMDB_KEANU_REEVES);
        assertThat(resultado.getNome()).isEqualTo(NOME_KEANU_REEVES);
        assertThat(resultado.getNomeNormalizado()).isEqualTo(NOME_NORMALIZADO_KEANU_REEVES);
        assertThat(resultado.getImagem()).isEqualTo(IMAGEM_KEANU_REEVES);
        assertThat(resultado.getPopularity()).isEqualTo(POPULARIDADE_KEANU_REEVES);
        assertThat(resultado.getInicializado()).isFalse();
        assertThat(resultado.getUltimaAtualizacao()).isNotNull();

        verify(atorRepository).save(resultado);
        verify(adicionarProducoesAtorService).adicionar(resultado);
    }

    @Test
    @DisplayName("Deve salvar ator antes de adicionar suas producoes")
    void deveSalvarAtorAntesDeAdicionarSuasProducoes() {
        AtorTMDBDtoPesquisaId response = getKeanuReevesTMDBDto();

        Ator resultado = adicionarAtorService.adicionarAtor(response);

        InOrder inOrder = inOrder(atorRepository, adicionarProducoesAtorService);
        inOrder.verify(atorRepository).save(resultado);
        inOrder.verify(adicionarProducoesAtorService).adicionar(resultado);
    }

    @Test
    @DisplayName("Deve processar ator existente, adicionar producoes e marcar como inicializado")
    void deveProcessarAtorExistenteAdicionarProducoesEMarcarComoInicializado() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ator.setInicializado(NAO_INICIALIZADO);

        adicionarAtorService.processarAtor(ator);

        assertThat(ator.getInicializado()).isTrue();

        verify(adicionarProducoesAtorService).adicionar(ator);
        verify(atorRepository).save(ator);
    }

    @Test
    @DisplayName("Deve adicionar producoes antes de salvar ator processado")
    void deveAdicionarProducoesAntesDeSalvarAtorProcessado() {
        Ator ator = getKeanuReevesAtorEntityComId();
        ator.setInicializado(NAO_INICIALIZADO);

        adicionarAtorService.processarAtor(ator);

        InOrder inOrder = inOrder(adicionarProducoesAtorService, atorRepository);
        inOrder.verify(adicionarProducoesAtorService).adicionar(ator);
        inOrder.verify(atorRepository).save(ator);
    }
}
