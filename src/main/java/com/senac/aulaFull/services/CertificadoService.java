package com.senac.aulaFull.services;

import com.senac.aulaFull.DTO.CertificadoRequestDto;
import com.senac.aulaFull.DTO.CertificadoResponseDto;
import com.senac.aulaFull.model.Certificado;
import com.senac.aulaFull.model.Usuario;
import com.senac.aulaFull.repository.CertificadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;

    public CertificadoResponseDto emitirCertificado(CertificadoRequestDto dados, Usuario usuarioLogado) {
        Certificado novoCertificado = new Certificado();
        novoCertificado.setNomeAluno(dados.nomeAluno());
        novoCertificado.setNomeCurso(dados.nomeCurso());
        novoCertificado.setUsuario(usuarioLogado);

        Certificado certificadoSalvo = certificadoRepository.save(novoCertificado);
        return toResponseDto(certificadoSalvo);
    }

    public List<CertificadoResponseDto> listarCertificados(Usuario usuarioLogado) {
        if ("ADMIN".equals(usuarioLogado.getRole())) {
            return certificadoRepository.findAll().stream().map(this::toResponseDto).toList();
        } else {
            return certificadoRepository.findByUsuario(usuarioLogado).stream().map(this::toResponseDto).toList();
        }
    }

    public CertificadoResponseDto atualizarCertificado(Long id, CertificadoRequestDto dados, Usuario usuarioLogado) {
        Certificado certificado = certificadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));

        checkOwnershipOrAdmin(certificado, usuarioLogado);

        certificado.setNomeAluno(dados.nomeAluno());
        certificado.setNomeCurso(dados.nomeCurso());

        Certificado certificadoSalvo = certificadoRepository.save(certificado);
        return toResponseDto(certificadoSalvo);
    }

    public void deletarCertificado(Long id, Usuario usuarioLogado) {
        Certificado certificado = certificadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));

        checkOwnershipOrAdmin(certificado, usuarioLogado);
        certificadoRepository.delete(certificado);
    }

    public CertificadoResponseDto validarPorCodigo(String codigo) {
        Certificado certificado = certificadoRepository.findByCodValidacao(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));
        return toResponseDto(certificado);
    }

    private void checkOwnershipOrAdmin(Certificado certificado, Usuario usuarioLogado) {
        if (!"ADMIN".equals(usuarioLogado.getRole()) && !certificado.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Acesso negado. Você não tem permissão para esta operação.");
        }
    }

    private CertificadoResponseDto toResponseDto(Certificado certificado) {
        return new CertificadoResponseDto(
                certificado.getId(),
                certificado.getNomeAluno(),
                certificado.getNomeCurso(),
                certificado.getCodValidacao()
        );
    }
}