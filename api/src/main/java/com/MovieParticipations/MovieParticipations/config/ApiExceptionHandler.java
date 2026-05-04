package com.MovieParticipations.MovieParticipations.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ApiExceptionHandler {
    private static final String ERRO_VALIDACAO = "Erro de validação";
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String PATH = "path";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                                     HttpServletRequest request) {
        HttpStatus httpStatus = BAD_REQUEST;
        String message = extrairErro(ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, now());
        body.put(STATUS, httpStatus.value());
        body.put(ERROR, httpStatus.getReasonPhrase());
        body.put(MESSAGE, message);
        body.put(PATH, request.getServletPath());

        return new ResponseEntity<>(body, httpStatus);

    }

    private String extrairErro(MethodArgumentNotValidException ex) {

        Optional<ObjectError> erroOpt = ex.getBindingResult().getAllErrors().stream()
                .findFirst();

        if (erroOpt.isEmpty()){
            return ERRO_VALIDACAO;
        }
        FieldError erro = (FieldError) erroOpt.get();
        return erro.getDefaultMessage();
    }
}