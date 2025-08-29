package com.senac.aulaFull.controller;


import com.senac.aulaFull.DTO.LoginRequestDto;
import com.senac.aulaFull.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/login")
    @Operation(summary = "Login", description = "método para efetuar login do usuário")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request){
        var token = tokenService.tokenGenerate(request.usuario(),request.senha());

        return ResponseEntity.ok(token);
    }

}
