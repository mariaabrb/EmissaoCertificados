package com.senac.aulaFull.DTO;

public record SenhaCodigoRequestDto (
        String email,
        String code,
        String novaSenha) {
}