package com.senac.aulaFull.DTO; // Verifique o pacote

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        String role
) {}