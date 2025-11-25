package com.senac.aulaFull.application.DTO.certificado;

import java.time.LocalDate;

public record CertificadoResponseDto(
        Long id,
        String nomeAluno,
        String nomeCurso,
        String nomeInstituicao,
        String codValidacao,
        LocalDate dataemissao
) {
}