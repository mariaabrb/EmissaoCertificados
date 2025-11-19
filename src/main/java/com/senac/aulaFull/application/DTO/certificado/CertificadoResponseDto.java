package com.senac.aulaFull.application.DTO.certificado;

public record CertificadoResponseDto(
        Long id,
        String nomeAluno,
        String nomeCurso,
        String codValidacao
) {
}