package com.senac.aulaFull.DTO;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        String role
) {}