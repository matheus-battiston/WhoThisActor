package com.MovieParticipations.MovieParticipations.validator;


import org.springframework.stereotype.Component;

@Component
public class ChecarNomePersonagem {
    private static final String SELF = "self";
    public boolean checarSeAutoAtuacao(String nomePersonagem, String nomeAtor ){
        return nomePersonagem != null &&
                (nomePersonagem.toLowerCase().contains("self") ||
                    nomePersonagem.toLowerCase().contains(nomeAtor.toLowerCase()));

    }
}
