package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.PesquisaAtorResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;
import com.MovieParticipations.MovieParticipations.dto.PesquisaIdPorNomeDTO;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static com.MovieParticipations.MovieParticipations.dto.ContratoRespostaDTO.MULTIPLE_MATCH;
import static com.MovieParticipations.MovieParticipations.dto.ContratoRespostaDTO.UNIQUE_MATCH;
import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getBryanCranstonAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBDtoPesquisaIdFactory.getKeanuReevesTMDBDto;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisarAtorPorNomeService")
class PesquisarAtorPorNomeServiceTest {

    private static final String NOME_KEANU_REEVES = "Keanu Reeves";
    private static final String NOME_NORMALIZADO_KEANU_REEVES = "keanu reeves";
    private static final String ATOR_NAO_ENCONTRADO = "Ator nao foi encontrado";

    @Mock
    private AtorRepository atorRepository;

    @Mock
    private AdicionarAtorService adicionarAtorService;

    @Mock
    private RequisicaoApiService requisicaoApiService;

    @Mock
    private PesquisarProducoesAtorService pesquisarProducoesAtorService;

    @InjectMocks
    private PesquisarAtorPorNomeService service;

    @Test
    @DisplayName("Deve retornar ator local inicializado com contrato único")
    void deveRetornarAtorLocalInicializadoComContratoUnico() {
        Ator keanu = getKeanuReevesAtorEntityComId();

        when(atorRepository.findAtorByNomeNormalizado(NOME_NORMALIZADO_KEANU_REEVES)).thenReturn(of(keanu));

        PesquisaAtorResponse resultado = service.pesquisarInfosPorNome(NOME_KEANU_REEVES);

        assertThat(resultado.getContratoResposta()).isEqualTo(UNIQUE_MATCH);
        assertThat(resultado.getAtores()).hasSize(1);
        assertThat(resultado.getAtores().get(0).getId()).isEqualTo(keanu.getId());
        assertThat(resultado.getAtores().get(0).getNome()).isEqualTo(keanu.getNome());
        verify(atorRepository).findAtorByNomeNormalizado(NOME_NORMALIZADO_KEANU_REEVES);
        verifyNoInteractions(adicionarAtorService, requisicaoApiService, pesquisarProducoesAtorService);
    }

    @Test
    @DisplayName("Deve processar primeiro ator local quando ainda não estiver inicializado")
    void deveProcessarPrimeiroAtorLocalQuandoAindaNaoEstiverInicializado() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        keanu.setInicializado(false);

        when(atorRepository.findAtorByNomeNormalizado(NOME_NORMALIZADO_KEANU_REEVES)).thenReturn(of(keanu));

        PesquisaAtorResponse resultado = service.pesquisarInfosPorNome(NOME_KEANU_REEVES);

        assertThat(resultado.getContratoResposta()).isEqualTo(UNIQUE_MATCH);
        verify(adicionarAtorService).processarAtor(keanu);
        verifyNoInteractions(requisicaoApiService, pesquisarProducoesAtorService);
    }

    @Test
    @DisplayName("Deve retornar contrato múltiplo quando encontrar mais de um ator local")
    void deveRetornarContratoMultiploQuandoEncontrarMaisDeUmAtorLocal() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        Ator bryan = getBryanCranstonAtorEntityComId();

        when(atorRepository.findAtorByNomeNormalizado(NOME_NORMALIZADO_KEANU_REEVES))
                .thenReturn(of(keanu, bryan));

        PesquisaAtorResponse resultado = service.pesquisarInfosPorNome(NOME_KEANU_REEVES);

        assertThat(resultado.getContratoResposta()).isEqualTo(MULTIPLE_MATCH);
        assertThat(resultado.getAtores()).extracting(InfoAtorResponse::getId)
                .containsExactly(keanu.getId(), bryan.getId());
        verifyNoInteractions(adicionarAtorService, requisicaoApiService, pesquisarProducoesAtorService);
    }

    @Test
    @DisplayName("Deve buscar e adicionar ator do TMDB quando não existir localmente")
    void deveBuscarEAdicionarAtorDoTmdbQuandoNaoExistirLocalmente() {
        AtorTMDBDtoPesquisaId keanuExterno = getKeanuReevesTMDBDto();
        Ator keanuSalvo = getKeanuReevesAtorEntityComId();
        PesquisaIdPorNomeDTO respostaApi = PesquisaIdPorNomeDTO.builder()
                .resultados(of(keanuExterno))
                .build();

        when(atorRepository.findAtorByNomeNormalizado(NOME_NORMALIZADO_KEANU_REEVES)).thenReturn(of());
        when(requisicaoApiService.persquisarIdPorNome(NOME_KEANU_REEVES)).thenReturn(respostaApi);
        when(pesquisarProducoesAtorService.getAtor(of(keanuExterno), NOME_KEANU_REEVES)).thenReturn(keanuExterno);
        when(adicionarAtorService.adicionarAtor(keanuExterno)).thenReturn(keanuSalvo);

        PesquisaAtorResponse resultado = service.pesquisarInfosPorNome(NOME_KEANU_REEVES);

        assertThat(resultado.getContratoResposta()).isEqualTo(UNIQUE_MATCH);
        assertThat(resultado.getAtores()).hasSize(1);
        assertThat(resultado.getAtores().get(0).getId()).isEqualTo(keanuSalvo.getId());
        verify(adicionarAtorService).adicionarAtor(keanuExterno);
    }

    @Test
    @DisplayName("Deve retornar ator selecionado quando API trouxer resultados")
    void deveRetornarAtorSelecionadoQuandoApiTrouxerResultados() {
        AtorTMDBDtoPesquisaId keanuExterno = getKeanuReevesTMDBDto();
        PesquisaIdPorNomeDTO respostaApi = PesquisaIdPorNomeDTO.builder()
                .resultados(of(keanuExterno))
                .build();

        when(requisicaoApiService.persquisarIdPorNome(NOME_KEANU_REEVES)).thenReturn(respostaApi);
        when(pesquisarProducoesAtorService.getAtor(of(keanuExterno), NOME_KEANU_REEVES)).thenReturn(keanuExterno);

        AtorTMDBDtoPesquisaId resultado = service.pesquisarIdPorNome(NOME_KEANU_REEVES);

        assertThat(resultado).isSameAs(keanuExterno);
        verify(pesquisarProducoesAtorService).getAtor(of(keanuExterno), NOME_KEANU_REEVES);
    }

    @Test
    @DisplayName("Deve retornar erro quando API não trouxer resultados")
    void deveRetornarErroQuandoApiNaoTrouxerResultados() {
        PesquisaIdPorNomeDTO respostaApi = PesquisaIdPorNomeDTO.builder().resultados(of()).build();

        when(requisicaoApiService.persquisarIdPorNome(NOME_KEANU_REEVES)).thenReturn(respostaApi);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.pesquisarIdPorNome(NOME_KEANU_REEVES)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(ATOR_NAO_ENCONTRADO);
        verifyNoInteractions(pesquisarProducoesAtorService);
    }

    @Test
    @DisplayName("Deve retornar erro quando API trouxer resultados nulos")
    void deveRetornarErroQuandoApiTrouxerResultadosNulos() {
        PesquisaIdPorNomeDTO respostaApi = PesquisaIdPorNomeDTO.builder().resultados(null).build();

        when(requisicaoApiService.persquisarIdPorNome(NOME_KEANU_REEVES)).thenReturn(respostaApi);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.pesquisarIdPorNome(NOME_KEANU_REEVES)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(ATOR_NAO_ENCONTRADO);
        verifyNoInteractions(pesquisarProducoesAtorService);
    }
}
