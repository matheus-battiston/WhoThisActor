package com.MovieParticipations.MovieParticipations.security.config;

import com.MovieParticipations.MovieParticipations.security.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private static final String URL_REDIRECT_AUTENTICADO = "http://localhost:3000/home";

    @Autowired
    CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()

                .and()
                .authorizeRequests()
                .anyRequest().permitAll()  // Requer autenticação para todas as outras rotas

                .and()
                .httpBasic()

                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)  // Usa o serviço personalizado para lidar com o usuário

                .and()
                .defaultSuccessUrl(URL_REDIRECT_AUTENTICADO, true); // Redireciona para "/home" após o login bem-sucedido


        return http.build();
    }
}
