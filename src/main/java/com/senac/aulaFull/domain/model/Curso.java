package com.senac.aulaFull.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    @ToString.Exclude
    private Usuario adminResponsavel;

    @ManyToMany(mappedBy = "cursosMatriculados", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Usuario> alunosMatriculados = new HashSet<>();
}