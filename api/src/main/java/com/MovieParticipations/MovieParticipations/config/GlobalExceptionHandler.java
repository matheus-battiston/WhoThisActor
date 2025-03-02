package com.MovieParticipations.MovieParticipations.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        // Acessando o status corretamente
        HttpStatusCode status = ex.getStatusCode(); // O método correto é getStatusCode()

        // Acessando a mensagem da exceção
        String message = ex.getReason();

        // Retornando a resposta com o código de status e a mensagem da exceção
        return new ResponseEntity<>(message, status);
    }
}