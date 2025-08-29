package com.senac.aulaFull.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${spring.secretkey}")
    private String secret;

    @Value("${spring.expiration_time}")
    private Long time;

    private String emissor = "test";

    public String tokenGenerate(String usuario, String senha) {

        Algorithm algoritm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(usuario)
                .withExpiresAt(generateExpirationDate())
                .sign(algoritm);
        return token;
    }
    //verifica o token para saber se é um token válido
    public DecodedJWT validarToken(String token){
        Algorithm algoritm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algoritm)
                .withIssuer(emissor)
                .build();

        return verifier.verify(token);
    }

        private Instant generateExpirationDate() {
        var date = LocalDateTime.now();
        date = date.plusMinutes(time);

        return date.toInstant(ZoneOffset.of("-03:00"));
        }
}
