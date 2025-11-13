package com.senac.aulaFull.DTO;

public record CertificadoResponseDto(
        Long id,
        String nomeAluno,
        String nomeCurso,
        String codValidacao
) {
}