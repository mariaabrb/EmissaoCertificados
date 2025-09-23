package com.senac.aulaFull.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // metodo que vai criar um codigo unico no momento q registra a entidade no banco
    @PrePersist
    public void gerarCodValidacao() {
        if (codValidacao == null) {
            codValidacao = UUID.randomUUID().toString();
        }
    }
}
