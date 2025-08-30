package com.senac.aulaFull.controller;


import com.senac.aulaFull.DTO.LoginRequestDto;
import com.senac.aulaFull.services.TokenService;
import com.senac.aulaFull.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Controller de autenticação", description = "controller responsável pela autenticação da aplicação")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "método para efetuar login do usuário")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request){

        if (!usuarioService.validarSenha(request)){
            return ResponseEntity.badRequest().body("Usuário ou senha inválido!");
        }

        var token = tokenService.tokenGenerate(request.email(),request.senha());

        return ResponseEntity.ok(token);
    }

}
