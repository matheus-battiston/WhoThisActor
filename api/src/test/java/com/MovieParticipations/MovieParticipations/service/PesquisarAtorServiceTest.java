package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.repository.AtorRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static com.MovieParticipations.MovieParticipations.factories.domain.AtorFactory.getKeanuReevesAtorEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.security.UsuarioAutenticadoFactory.getUsuarioAutenticadoDto;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
@DisplayName("PesquisarAtorService")
class PesquisarAtorServiceTest {

    private static final Long ID_ATOR = 99L;
    private static final String NOME_KEANU_REEVES = "Keanu Reeves";
    private static final String ATOR_NAO_ENCONTRADO = "Ator nao foi encontrado";
    private static final boolean ESTA_FAVORITADO = true;

    @Mock
    private AtorRepository atorRepository;

    @Mock
    private PesquisarProducoesAtorService pesquisarProducoesAtorService;

    @Mock
    private ChecarAtorFavoritadoService checarAtorFavoritadoService;

    @Mock
    private AdicionarAtorService adicionarAtorService;

    @InjectMocks
    private PesquisarAtorService service;

    @Test
    @DisplayName("Deve retornar erro quando ator não existir")
    void deveRetornarErroQuandoAtorNaoExistir() {
        when(atorRepository.findAtorById(ID_ATOR)).thenReturn(empty());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.pesquisarPorId(ID_ATOR, null)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(ATOR_NAO_ENCONTRADO);
        verifyNoInteractions(pesquisarProducoesAtorService, checarAtorFavoritadoService, adicionarAtorService);
    }

    @Test
    @DisplayName("Deve retornar informações do ator sem favorito para usuário anônimo")
    void deveRetornarInformacoesDoAtorSemFavoritoParaUsuarioAnonimo() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        AtorEProducoesResponse respostaProducoes = respostaDeKeanu();

        when(atorRepository.findAtorById(ID_ATOR)).thenReturn(of(keanu));
        when(pesquisarProducoesAtorService.getAtorInfo(keanu)).thenReturn(respostaProducoes);

        AtorEProducoesResponse resultado = service.pesquisarPorId(ID_ATOR, null);

        assertThat(resultado).isSameAs(respostaProducoes);
        assertThat(resultado.getFavoritado()).isNull();
        verify(pesquisarProducoesAtorService).getAtorInfo(keanu);
        verifyNoInteractions(checarAtorFavoritadoService, adicionarAtorService);
    }

    @Test
    @DisplayName("Deve processar ator não inicializado antes de pesquisar produções")
    void deveProcessarAtorNaoInicializadoAntesDePesquisarProducoes() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        keanu.setInicializado(false);
        AtorEProducoesResponse respostaProducoes = respostaDeKeanu();

        when(atorRepository.findAtorById(ID_ATOR)).thenReturn(of(keanu));
        when(pesquisarProducoesAtorService.getAtorInfo(keanu)).thenReturn(respostaProducoes);

        AtorEProducoesResponse resultado = service.pesquisarPorId(ID_ATOR, null);

        assertThat(resultado).isSameAs(respostaProducoes);
        verify(adicionarAtorService).processarAtor(keanu);
        verify(pesquisarProducoesAtorService).getAtorInfo(keanu);
        verifyNoInteractions(checarAtorFavoritadoService);
    }

    @Test
    @DisplayName("Deve informar favorito quando usuário estiver autenticado")
    void deveInformarFavoritoQuandoUsuarioEstiverAutenticado() {
        Ator keanu = getKeanuReevesAtorEntityComId();
        UsuarioAutenticado usuarioAutenticado = getUsuarioAutenticadoDto();
        AtorEProducoesResponse respostaProducoes = respostaDeKeanu();

        when(atorRepository.findAtorById(ID_ATOR)).thenReturn(of(keanu));
        when(pesquisarProducoesAtorService.getAtorInfo(keanu)).thenReturn(respostaProducoes);
        when(checarAtorFavoritadoService.estaFavoritadoPorAuthId(ID_ATOR, usuarioAutenticado.getId()))
                .thenReturn(ESTA_FAVORITADO);

        AtorEProducoesResponse resultado = service.pesquisarPorId(ID_ATOR, usuarioAutenticado);

        assertThat(resultado).isSameAs(respostaProducoes);
        assertThat(resultado.getFavoritado()).isTrue();
        verify(checarAtorFavoritadoService).estaFavoritadoPorAuthId(ID_ATOR, usuarioAutenticado.getId());
        verifyNoInteractions(adicionarAtorService);
    }

    private AtorEProducoesResponse respostaDeKeanu() {
        return AtorEProducoesResponse.builder()
                .id(ID_ATOR)
                .nome(NOME_KEANU_REEVES)
                .build();
    }
}
