package com.senac.aulaFull.application.DTO.usuario;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        String role
) {}