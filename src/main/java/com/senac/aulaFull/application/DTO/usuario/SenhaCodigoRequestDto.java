package com.senac.aulaFull.application.DTO.usuario;

public record SenhaCodigoRequestDto (
        String email,
        String code,
        String novaSenha) {
}