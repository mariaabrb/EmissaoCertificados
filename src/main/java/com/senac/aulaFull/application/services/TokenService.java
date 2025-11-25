package com.senac.aulaFull.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import com.senac.aulaFull.domain.model.Token;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.domain.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${spring.secretkey}")
    private String secret;

    @Value("${spring.expiration_time}")
    private Long time;

    private String emissor = "test";

    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken(Usuario usuario) {
        try {
            Algorithm algoritm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer(emissor)
                    .withSubject(usuario.getEmail())
                    .withClaim("nome", usuario.getNome())
                    .withClaim("roles", usuario.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algoritm);

            tokenRepository.save(new Token(null, token, usuario));
            return token;
        } catch (Exception exception){
            throw new RuntimeException("erro ao gerar token JWT", exception);
        }
    }

    @Transactional
    public Usuario validarToken(String token){
        Algorithm algoritm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algoritm)
                .withIssuer(emissor)
                .build();
        var decodedJWT = verifier.verify(token);
        var tokenResult = tokenRepository.findByToken(token).orElse(null);
        if (tokenResult == null){
            throw new IllegalArgumentException("Token inválido.");
        }
        return tokenResult.getUsuario();
    }

    private Instant generateExpirationDate() {
        var date = LocalDateTime.now();
        date = date.plusMinutes(time);

        return date.toInstant(ZoneOffset.of("-03:00"));
    }
}