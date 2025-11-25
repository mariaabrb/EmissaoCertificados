package com.senac.aulaFull.domain.repository;

import com.senac.aulaFull.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCPF(String cpf);
    
    List<Usuario> findByNomeInstituicao(String nomeInstituicao);

    Optional<Usuario> findByCodigoResetSenha(String codigoResetSenha);

    @Query("SELECT DISTINCT u.nomeInstituicao FROM Usuario u WHERE u.nomeInstituicao IS NOT NULL AND u.role = 'ROLE_ADMIN'")
    List<String> findDistinctNomeInstituicao();
}