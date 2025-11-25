package com.senac.aulaFull.domain.repository;

import com.senac.aulaFull.domain.model.Curso;
import com.senac.aulaFull.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByAdminResponsavel(Usuario adminResponsavel);
    List<Curso> findByAdminResponsavelAndAtivoTrue(Usuario adminResponsavel);
}
