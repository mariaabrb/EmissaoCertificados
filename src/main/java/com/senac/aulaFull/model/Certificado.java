package com.senac.aulaFull.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeAluno;
    private String nomeCurso;

    @Column(nullable = false, unique = true)
    private String codValidacao;

    // metodo que vai criar um codigo unicmo no omento q registra a entidade no banco
    @PrePersist
    public void gerarCodValidacao() {
        if (codValidacao == null) {
            codValidacao = UUID.randomUUID().toString();
        }
    }
}
