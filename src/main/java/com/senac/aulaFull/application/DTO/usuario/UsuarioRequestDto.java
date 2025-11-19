package com.senac.aulaFull.application.DTO.usuario;

public record UsuarioRequestDto(
        String nome,
        String email,
        String senha,
        String cpf
) {
}