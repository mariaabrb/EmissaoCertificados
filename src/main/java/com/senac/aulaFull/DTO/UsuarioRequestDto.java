package com.senac.aulaFull.DTO;

public record UsuarioRequestDto(
        String nome,
        String email,
        String senha,
        String cpf
) {
}