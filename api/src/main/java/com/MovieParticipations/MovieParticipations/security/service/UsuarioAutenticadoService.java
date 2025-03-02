package com.MovieParticipations.MovieParticipations.security.service;


import com.MovieParticipations.MovieParticipations.security.config.UsuarioSecurity;
import com.MovieParticipations.MovieParticipations.security.controller.response.UsuarioResponse;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.MovieParticipations.MovieParticipations.security.mapper.UsuarioMapper.toResponse;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
public class UsuarioAutenticadoService {
    private static final String EMAIL = "email";
    private static final String EMAIL_NAO_EXISTE = "O email fornecido pelo provedor nao existe";

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Long getId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String emailUsuario = (String) oAuth2User.getAttributes().get(EMAIL);

            return usuarioRepository.findByEmail(emailUsuario)
                    .orElseThrow(() -> new ResponseStatusException(UNPROCESSABLE_ENTITY, EMAIL_NAO_EXISTE))
                    .getId();


        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Aqui você deve verificar se o UserDetails é uma instância de UsuarioSecurity ou outra classe.
            if (userDetails instanceof UsuarioSecurity) {
                return ((UsuarioSecurity) userDetails).getId();
            }
        }

        return null;
    }

    public Usuario get() {
        Long id = getId();

        if (isNull(id)) {
            return null;
        }

        return usuarioRepository.findById(getId()).orElse(null);
    }

    public UsuarioResponse getResponse() {
        Usuario entity = get();
        return nonNull(entity) ? toResponse(entity) : new UsuarioResponse();
    }
}
