package com.senac.aulaFull.repository;

import com.senac.aulaFull.domain.model.Certificado;
import com.senac.aulaFull.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    Optional<Certificado> findByCodValidacao(String codValidacao);

    List<Certificado> findByUsuario(Usuario usuario);
}