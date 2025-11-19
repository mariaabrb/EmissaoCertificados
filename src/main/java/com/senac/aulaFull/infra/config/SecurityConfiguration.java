package com.senac.aulaFull.infra.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        //rotas publicas p autenticacao e validacao do certificado emitido
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/certificados/validar/**").permitAll()
                        //rotas de cursos usuarios logados
                        .requestMatchers(HttpMethod.GET, "/api/cursos").authenticated()
                        //rotas de cursos adm
                        .requestMatchers("/api/cursos/{cursoId}/alunos/**").hasRole("ADMIN")
                        .requestMatchers("/api/usuarios/{alunoId}/cursos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/cursos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cursos/**").hasRole("ADMIN")
                        //rotas de certificados p adm
                        .requestMatchers(HttpMethod.POST, "/api/certificados").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/certificados/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/certificados/**").hasRole("ADMIN")
                        //usuario logado listar e senha
                        .requestMatchers(HttpMethod.GET, "/api/certificados").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/me/senha").authenticated()

                        // rota de uusuarios
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        //rota adm listar usuarios
                        .requestMatchers(HttpMethod.GET, "/usuarios", "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/me/senha").authenticated()

                        //qualquer outra requisicao precisa estar autenticado com token
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}