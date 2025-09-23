package com.senac.aulaFull.DTO; // Verifique se o nome do seu pacote está correto

public record CertificadoResponseDto(
        Long id,
        String nomeAluno,
        String nomeCurso,
        String codValidacao
) {
}