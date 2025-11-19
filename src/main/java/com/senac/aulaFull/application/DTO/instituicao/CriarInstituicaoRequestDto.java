package com.senac.aulaFull.application.DTO.instituicao;

public record CriarInstituicaoRequestDto(
        String nomeInstituicao,
        String cidade,

        String nomeAdmin,
        String emailAdmin,
        String senhaAdmin,
        String cpfAdmin
) {}