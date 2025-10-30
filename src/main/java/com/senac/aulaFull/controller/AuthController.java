package com.senac.aulaFull.controller;

import com.senac.aulaFull.DTO.EsqueciMinhaSenhaDto;
import com.senac.aulaFull.DTO.LoginRequestDto;
import com.senac.aulaFull.DTO.SenhaCodigoRequestDto;
import com.senac.aulaFull.DTO.TokenDto;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.services.TokenService;
import com.senac.aulaFull.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Controller de autenticação", description = "Controller responsável pela autenticação da aplicação")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Efetua o login do usuário e retorna um token JWT.")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
            Authentication auth = authenticationManager.authenticate(usernamePassword);
            var usuario = (Usuario) auth.getPrincipal();
            var token = tokenService.generateToken(usuario);
            return ResponseEntity.ok(new TokenDto(token));
        } catch (Exception e) {
            // É melhor logar o erro e.getMessage() aqui para debug
            return ResponseEntity.badRequest().body("Usuário ou senha inválido!");
        }
    }

    @PostMapping("/esquecisenha")
    @Operation(summary = "Solicitar Código de Redefinição", description = "envia um código por e-mail.")
    public ResponseEntity<?> solicitarCodigo(@RequestBody EsqueciMinhaSenhaDto request) {

        try {
            usuarioService.solicitarCodigoRedefinicaoSenha(request);
            return ResponseEntity.ok("se o e-mail estiver cadastrado, um código de redefinição será enviado.");
        } catch (Exception e) {
            return ResponseEntity.ok("se o e-mail estiver cadastrado, um código de redefinição será enviado.");
        }
    }

    @PostMapping("/redefinirsenha")
    @Operation(summary = "Redefinir Senha com Código", description = "Verifica o código e define uma nova senha.")
    public ResponseEntity<?> redefinirSenha(@RequestBody SenhaCodigoRequestDto request) {

       try {
            usuarioService.redefinirSenha(request);
            return ResponseEntity.ok("senha redefinida com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ocorreu um erro ao redefinir a senha.");
        }
    }
}