package com.senac.aulaFull.domain.repository;

import com.senac.aulaFull.domain.model.Instituicao;
import com.senac.aulaFull.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>  {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCPF(String cpf);
    List<Usuario> findByCursosMatriculados_Id(Long cursoId);

    List<Usuario> findByInstituicaoAndRole(Instituicao instituicao, String role);
    Optional<Usuario> findByIdAndInstituicao(Long id, Instituicao instituicao);
}