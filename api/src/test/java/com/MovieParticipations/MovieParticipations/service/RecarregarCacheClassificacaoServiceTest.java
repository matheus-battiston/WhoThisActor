package com.MovieParticipations.MovieParticipations.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpMethod.POST;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecarregarCacheClassificacaoService")
class RecarregarCacheClassificacaoServiceTest {

    private static final String ENDERECO_RECARREGAMENTO = "http://classify/cache/reload";
    private static final String ERRO_RECARREGAR_CACHE =
            "Nao foi possivel recarregar o cache de atores por producao do classify";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RecarregarCacheClassificacaoService service;

    @Test
    @DisplayName("Deve chamar endpoint de recarga com POST sem corpo")
    void deveChamarEndpointDeRecargaComPostSemCorpo() {
        configurarEndereco();

        service.recarregarCacheAtoresPorProducao();

        verify(restTemplate).exchange(ENDERECO_RECARREGAMENTO, POST, null, String.class);
    }

    @Test
    @DisplayName("Deve registrar aviso e não propagar erro quando recarga falhar")
    void deveRegistrarAvisoENaoPropagarErroQuandoRecargaFalhar() {
        configurarEndereco();
        RuntimeException erro = new RuntimeException("Falha de comunicação");
        Logger logger = (Logger) LoggerFactory.getLogger(RecarregarCacheClassificacaoService.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        boolean aditivoOriginal = logger.isAdditive();
        logger.setAdditive(false);
        logger.addAppender(appender);
        appender.start();

        doThrow(erro).when(restTemplate).exchange(
                ENDERECO_RECARREGAMENTO,
                POST,
                null,
                String.class
        );

        try {
            service.recarregarCacheAtoresPorProducao();

            assertThat(appender.list).hasSize(1);
            ILoggingEvent evento = appender.list.get(0);
            assertThat(evento.getLevel()).isEqualTo(Level.WARN);
            assertThat(evento.getFormattedMessage()).isEqualTo(ERRO_RECARREGAR_CACHE);
            assertThat(evento.getThrowableProxy().getClassName()).isEqualTo(RuntimeException.class.getName());
        } finally {
            logger.detachAppender(appender);
            logger.setAdditive(aditivoOriginal);
            appender.stop();
        }
    }

    private void configurarEndereco() {
        ReflectionTestUtils.setField(service, "classifyCacheReloadAddress", ENDERECO_RECARREGAMENTO);
    }
}
