package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.response.OpcoesAtoresParecidosResponseFactory.getKeanuReevesOpcoesAtoresParecidosResponse;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBDtoPesquisaIdFactory.getKeanuReevesTMDBDto;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnriquecerAtoresClassificadosService")
class EnriquecerAtoresClassificadosServiceTest {

    private static final Long ID_KEANU_REEVES = 99L;
    private static final Long ID_ATOR_DUPLICADO = 100L;
    private static final Long ID_ATOR_SEM_IDENTIDADE = 101L;
    private static final String NOME_KEANU_REEVES_NORMALIZADO = "keanu reeves";
    private static final String NOME_KEANU_REEVES = "Keanu Reeves";
    private static final String IMAGEM_KEANU_REEVES = "https://image.tmdb.org/t/p/w400/keanu.jpg";
    private static final String NOME_ATOR_SEM_ID = "Ator sem ID";
    private static final String NOME_ATOR_SEM_ID_NORMALIZADO = "ator sem id";
    private static final Long ID_TMDB_ATOR_SEM_ID = 123L;
    private static final Double POPULARIDADE_ATOR_SEM_ID = 1.0;
    private static final Double DISTANCIA_MEDIA_ATOR_SEM_ID = 0.40;
    private static final Double DISTANCIA_MEDIA_MAIOR = 0.50;
    private static final Double DISTANCIA_MEDIA_MENOR = 0.10;
    private static final String IMAGEM_ATOR_SEM_ID = "/ator-sem-id.jpg";
    private static final int PRIMEIRO_ATOR = 0;
    private static final int SEGUNDO_ATOR = 1;
    private static final int QUANTIDADE_UM_ATOR = 1;
    private static final int QUANTIDADE_DOIS_ATORES = 2;

    @Mock
    private PesquisarAtorPorNomeService pesquisarAtorPorNomeService;

    @Mock
    private AtorRepository atorRepository;

    @Mock
    private SalvarAtoresClassificadosService salvarAtoresClassificadosService;

    @Captor
    private ArgumentCaptor<List<OpcoesAtoresParecidosResponse>> atoresNovosCaptor;

    @InjectMocks
    private EnriquecerAtoresClassificadosService service;

    @Test
    @DisplayName("Deve preencher os dados de um ator já existente no banco sem consultar o TMDB")
    void devePreencherDadosDoAtorExistenteSemConsultarTmdb() {
        OpcoesAtoresParecidosResponse classificacao = getKeanuReevesOpcoesAtoresParecidosResponse();
        Ator atorNoBanco = getKeanuReevesAtorEntityComId();

        when(atorRepository.findByNomeNormalizadoIn(of(NOME_KEANU_REEVES_NORMALIZADO)))
                .thenReturn(of(atorNoBanco));

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(of(classificacao));

        assertEquals(QUANTIDADE_UM_ATOR, resultado.size());
        OpcoesAtoresParecidosResponse atorResultado = resultado.get(PRIMEIRO_ATOR);

        assertEquals(ID_KEANU_REEVES, atorResultado.getId());
        assertEquals(atorNoBanco.getIdTmdb(), atorResultado.getIdTmdb());
        assertEquals(IMAGEM_KEANU_REEVES, atorResultado.getImagem());
        verify(pesquisarAtorPorNomeService, never()).pesquisarIdPorNome(classificacao.getIdentidade());
        verify(salvarAtoresClassificadosService).salvarNovosEPreencherIds(of());
    }

    @Test
    @DisplayName("Deve manter o primeiro ator quando o banco retornar nomes normalizados duplicados")
    void deveManterPrimeiroAtorQuandoBancoRetornarNomesNormalizadosDuplicados() {
        OpcoesAtoresParecidosResponse classificacao = getKeanuReevesOpcoesAtoresParecidosResponse();
        Ator atorExistente = getKeanuReevesAtorEntityComId();
        Ator atorDuplicado = getKeanuReevesAtorEntityComId();
        atorDuplicado.setId(ID_ATOR_DUPLICADO);

        when(atorRepository.findByNomeNormalizadoIn(of(NOME_KEANU_REEVES_NORMALIZADO)))
                .thenReturn(of(atorExistente, atorDuplicado));

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(of(classificacao));

        assertEquals(QUANTIDADE_UM_ATOR, resultado.size());
        assertEquals(ID_KEANU_REEVES, resultado.get(PRIMEIRO_ATOR).getId());
        verify(pesquisarAtorPorNomeService, never()).pesquisarIdPorNome(classificacao.getIdentidade());
    }

    @Test
    @DisplayName("Deve preservar ator sem identidade ao preencher dados de outros atores do banco")
    void devePreservarAtorSemIdentidadeAoPreencherDadosDeOutrosAtoresDoBanco() {
        OpcoesAtoresParecidosResponse classificacao = getKeanuReevesOpcoesAtoresParecidosResponse();
        OpcoesAtoresParecidosResponse atorSemIdentidade = OpcoesAtoresParecidosResponse.builder()
                .id(ID_ATOR_SEM_IDENTIDADE)
                .build();
        Ator atorExistente = getKeanuReevesAtorEntityComId();

        when(atorRepository.findByNomeNormalizadoIn(of(NOME_KEANU_REEVES_NORMALIZADO)))
                .thenReturn(of(atorExistente));

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(
                of(classificacao, atorSemIdentidade)
        );

        assertEquals(QUANTIDADE_DOIS_ATORES, resultado.size());
        assertEquals(ID_KEANU_REEVES, resultado.get(PRIMEIRO_ATOR).getId());
        assertEquals(ID_ATOR_SEM_IDENTIDADE, resultado.get(SEGUNDO_ATOR).getId());
        assertNull(resultado.get(SEGUNDO_ATOR).getIdentidade());
        verify(pesquisarAtorPorNomeService, never()).pesquisarIdPorNome(null);
    }

    @Test
    @DisplayName("Não deve consultar o banco quando todas as identidades forem nulas")
    void naoDeveConsultarBancoQuandoTodasAsIdentidadesForemNulas() {
        OpcoesAtoresParecidosResponse atorSemIdentidade = OpcoesAtoresParecidosResponse.builder()
                .id(ID_ATOR_SEM_IDENTIDADE)
                .build();

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(of(atorSemIdentidade));

        assertEquals(QUANTIDADE_UM_ATOR, resultado.size());
        assertEquals(ID_ATOR_SEM_IDENTIDADE, resultado.get(PRIMEIRO_ATOR).getId());
        assertNull(resultado.get(PRIMEIRO_ATOR).getIdentidade());
        verify(atorRepository, never()).findByNomeNormalizadoIn(of());
        verify(salvarAtoresClassificadosService).salvarNovosEPreencherIds(of());
    }

    @Test
    @DisplayName("Deve consultar o TMDB e salvar um ator que ainda não existe no banco")
    void deveEnriquecerESalvarAtorNovo() {
        OpcoesAtoresParecidosResponse classificacao = getKeanuReevesOpcoesAtoresParecidosResponse();
        AtorTMDBDtoPesquisaId atorTmdb = getKeanuReevesTMDBDto();

        when(atorRepository.findByNomeNormalizadoIn(of(NOME_KEANU_REEVES_NORMALIZADO)))
                .thenReturn(of());
        when(pesquisarAtorPorNomeService.pesquisarIdPorNome(classificacao.getIdentidade())).thenReturn(atorTmdb);

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(of(classificacao));

        assertEquals(QUANTIDADE_UM_ATOR, resultado.size());
        OpcoesAtoresParecidosResponse atorEnriquecido = resultado.get(PRIMEIRO_ATOR);

        assertEquals(atorTmdb.getId(), atorEnriquecido.getIdTmdb());
        assertEquals(atorTmdb.getPopularidade(), atorEnriquecido.getPopularidade());

        verify(salvarAtoresClassificadosService).salvarNovosEPreencherIds(atoresNovosCaptor.capture());
        assertEquals(QUANTIDADE_UM_ATOR, atoresNovosCaptor.getValue().size());
        OpcoesAtoresParecidosResponse atorNovo = atoresNovosCaptor.getValue().get(PRIMEIRO_ATOR);

        assertEquals(classificacao.getIdentidade(), atorNovo.getIdentidade());
        assertEquals(atorTmdb.getId(), atorNovo.getIdTmdb());
        assertEquals(atorTmdb.getPopularidade(), atorNovo.getPopularidade());
    }

    @Test
    @DisplayName("Deve manter classificação posterior quando possuir menor distância")
    void deveManterClassificacaoPosteriorQuandoPossuirMenorDistancia() {
        OpcoesAtoresParecidosResponse classificacaoComMaiorDistancia = OpcoesAtoresParecidosResponse.builder()
                .identidade(NOME_KEANU_REEVES)
                .distanciaMedia(DISTANCIA_MEDIA_MAIOR)
                .build();
        OpcoesAtoresParecidosResponse classificacaoComMenorDistancia = OpcoesAtoresParecidosResponse.builder()
                .identidade(NOME_KEANU_REEVES)
                .distanciaMedia(DISTANCIA_MEDIA_MENOR)
                .build();
        AtorTMDBDtoPesquisaId atorTmdb = getKeanuReevesTMDBDto();

        when(atorRepository.findByNomeNormalizadoIn(of(NOME_KEANU_REEVES_NORMALIZADO))).thenReturn(of());
        when(pesquisarAtorPorNomeService.pesquisarIdPorNome(NOME_KEANU_REEVES)).thenReturn(atorTmdb);

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(
                of(classificacaoComMaiorDistancia, classificacaoComMenorDistancia)
        );

        assertEquals(QUANTIDADE_UM_ATOR, resultado.size());
        assertEquals(DISTANCIA_MEDIA_MENOR, resultado.get(PRIMEIRO_ATOR).getDistanciaMedia());
        verify(pesquisarAtorPorNomeService).pesquisarIdPorNome(NOME_KEANU_REEVES);
        verify(salvarAtoresClassificadosService).salvarNovosEPreencherIds(atoresNovosCaptor.capture());
        assertEquals(QUANTIDADE_UM_ATOR, atoresNovosCaptor.getValue().size());
    }

    @Test
    @DisplayName("Deve manter primeira classificação quando possuir menor distância")
    void deveManterPrimeiraClassificacaoQuandoPossuirMenorDistancia() {
        OpcoesAtoresParecidosResponse classificacaoComMenorDistancia = OpcoesAtoresParecidosResponse.builder()
                .identidade(NOME_KEANU_REEVES)
                .distanciaMedia(DISTANCIA_MEDIA_MENOR)
                .build();
        OpcoesAtoresParecidosResponse classificacaoComMaiorDistancia = OpcoesAtoresParecidosResponse.builder()
                .identidade(NOME_KEANU_REEVES)
                .distanciaMedia(DISTANCIA_MEDIA_MAIOR)
                .build();
        AtorTMDBDtoPesquisaId atorTmdb = getKeanuReevesTMDBDto();

        when(atorRepository.findByNomeNormalizadoIn(of(NOME_KEANU_REEVES_NORMALIZADO))).thenReturn(of());
        when(pesquisarAtorPorNomeService.pesquisarIdPorNome(NOME_KEANU_REEVES)).thenReturn(atorTmdb);

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(
                of(classificacaoComMenorDistancia, classificacaoComMaiorDistancia)
        );

        assertEquals(QUANTIDADE_UM_ATOR, resultado.size());
        assertEquals(DISTANCIA_MEDIA_MENOR, resultado.get(PRIMEIRO_ATOR).getDistanciaMedia());
        verify(pesquisarAtorPorNomeService).pesquisarIdPorNome(NOME_KEANU_REEVES);
        verify(salvarAtoresClassificadosService).salvarNovosEPreencherIds(atoresNovosCaptor.capture());
        assertEquals(QUANTIDADE_UM_ATOR, atoresNovosCaptor.getValue().size());
    }

    @Test
    @DisplayName("Deve manter primeira classificação quando distâncias forem iguais")
    void deveManterPrimeiraClassificacaoQuandoDistanciasForemIguais() {
        OpcoesAtoresParecidosResponse primeiraClassificacao = OpcoesAtoresParecidosResponse.builder()
                .identidade(NOME_KEANU_REEVES)
                .id(ID_KEANU_REEVES)
                .distanciaMedia(DISTANCIA_MEDIA_MENOR)
                .build();
        OpcoesAtoresParecidosResponse segundaClassificacao = OpcoesAtoresParecidosResponse.builder()
                .identidade(NOME_KEANU_REEVES)
                .distanciaMedia(DISTANCIA_MEDIA_MENOR)
                .build();

        when(atorRepository.findByNomeNormalizadoIn(of(NOME_KEANU_REEVES_NORMALIZADO))).thenReturn(of());

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(
                of(primeiraClassificacao, segundaClassificacao)
        );

        assertEquals(QUANTIDADE_UM_ATOR, resultado.size());
        assertEquals(ID_KEANU_REEVES, resultado.get(PRIMEIRO_ATOR).getId());
        verify(pesquisarAtorPorNomeService, never()).pesquisarIdPorNome(NOME_KEANU_REEVES);
        verify(salvarAtoresClassificadosService).salvarNovosEPreencherIds(of());
    }

    @Test
    @DisplayName("Deve enriquecer e enviar para salvamento apenas ator sem ID")
    void deveEnriquecerEEnviarParaSalvamentoApenasAtorSemId() {
        OpcoesAtoresParecidosResponse atorExistente = getKeanuReevesOpcoesAtoresParecidosResponse();
        OpcoesAtoresParecidosResponse atorSemId = OpcoesAtoresParecidosResponse.builder()
                .identidade(NOME_ATOR_SEM_ID)
                .distanciaMedia(DISTANCIA_MEDIA_ATOR_SEM_ID)
                .build();
        Ator atorNoBanco = getKeanuReevesAtorEntityComId();
        AtorTMDBDtoPesquisaId atorTmdbSemId = AtorTMDBDtoPesquisaId.builder()
                .id(ID_TMDB_ATOR_SEM_ID)
                .nome(NOME_ATOR_SEM_ID)
                .popularidade(POPULARIDADE_ATOR_SEM_ID)
                .fotoDePerfil(IMAGEM_ATOR_SEM_ID)
                .build();

        when(atorRepository.findByNomeNormalizadoIn(of(
                NOME_KEANU_REEVES_NORMALIZADO,
                NOME_ATOR_SEM_ID_NORMALIZADO
        ))).thenReturn(of(atorNoBanco));
        when(pesquisarAtorPorNomeService.pesquisarIdPorNome(NOME_ATOR_SEM_ID)).thenReturn(atorTmdbSemId);

        List<OpcoesAtoresParecidosResponse> resultado = service.enriquecerComDadosTmdb(of(atorExistente, atorSemId));

        assertEquals(QUANTIDADE_DOIS_ATORES, resultado.size());

        verify(salvarAtoresClassificadosService).salvarNovosEPreencherIds(atoresNovosCaptor.capture());
        assertEquals(QUANTIDADE_UM_ATOR, atoresNovosCaptor.getValue().size());
        OpcoesAtoresParecidosResponse atorNovo = atoresNovosCaptor.getValue().get(PRIMEIRO_ATOR);

        assertEquals(NOME_ATOR_SEM_ID, atorNovo.getIdentidade());
    }
}
