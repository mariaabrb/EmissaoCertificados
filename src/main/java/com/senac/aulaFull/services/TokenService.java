package com.senac.aulaFull.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.senac.aulaFull.DTO.LoginRequestDto;
import com.senac.aulaFull.model.Token;
import com.senac.aulaFull.model.Usuario;
import com.senac.aulaFull.repository.TokenRepository;
import com.senac.aulaFull.repository.UsuarioRepository;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public String generateToken(Usuario usuario) {
        Algorithm algoritm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(usuario.getEmail())
                .withExpiresAt(this.generateExpirationDate())
                .sign(algoritm);
        tokenRepository.save(new Token(null, token, usuario));
        return token;
    }
    //verifica o token para saber se é um token válido
    public Usuario validarToken(String token){
        Algorithm algoritm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algoritm)
                .withIssuer(emissor)
                .build();
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
