package com.senac.aulaFull.domain.repository;

import com.senac.aulaFull.domain.model.Curso;
import com.senac.aulaFull.domain.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByInstituicao(Instituicao instituicao);
}
