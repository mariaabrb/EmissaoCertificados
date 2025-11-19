package com.senac.aulaFull.domain.repository;

import com.senac.aulaFull.domain.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

    List<Instituicao> findByNome(String nome);

    List<Instituicao> findByCidade(String cidade);
}