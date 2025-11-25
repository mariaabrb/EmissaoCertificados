package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.DTO.certificado.CertificadoRequestDto;
import com.senac.aulaFull.application.DTO.certificado.CertificadoResponseDto;
import com.senac.aulaFull.domain.model.Certificado;
import com.senac.aulaFull.domain.model.Curso;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.domain.repository.CertificadoRepository;
import com.senac.aulaFull.domain.repository.CursoRepository;
import com.senac.aulaFull.domain.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public Certificado emitirCertificado(CertificadoRequestDto dados, Usuario adminLogado) {

        Usuario aluno = usuarioRepository.findById(dados.alunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        if (!curso.getAdminResponsavel().getId().equals(adminLogado.getId())) {
            throw new RuntimeException("Erro: Você só pode emitir certificados para cursos que sua conta criou.");
        }

        Certificado novoCertificado = new Certificado();
        novoCertificado.setUsuario(aluno);
        novoCertificado.setCurso(curso);
        novoCertificado.setNomeAluno(aluno.getNome());

        return certificadoRepository.save(novoCertificado);
    }

    public List<CertificadoResponseDto> listarCertificadosDTO(Usuario usuarioLogado) {
        List<Certificado> certificados;
        String role = usuarioLogado.getRole();

        if ("ROLE_ADMIN".equals(role)) {
            certificados = certificadoRepository.findByCurso_AdminResponsavel(usuarioLogado);
        } else {
            certificados = certificadoRepository.findByUsuario(usuarioLogado);
        }

        return certificados.stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    private CertificadoResponseDto converterParaDto(Certificado certificado) {
        return new CertificadoResponseDto(
                certificado.getId(),
                certificado.getNomeAluno(),
                certificado.getCurso().getNome(),
                certificado.getCurso().getAdminResponsavel().getNomeInstituicao(),
                certificado.getCodValidacao(),
                java.time.LocalDate.now()
        );
    }


    public void deletarCertificado(Long id, Usuario adminLogado) {
        Certificado certificado = certificadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));

        if (!certificado.getCurso().getAdminResponsavel().getId().equals(adminLogado.getId())) {
            throw new RuntimeException("Acesso Negado: Você não pode deletar este certificado, pois ele não foi criado por você.");
        }

        certificadoRepository.deleteById(id);
    }

    public CertificadoResponseDto validarPorCodigo(String codigo) {
        Certificado certificado = certificadoRepository.findByCodValidacao(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado com o código informado."));

        return new CertificadoResponseDto(
                certificado.getId(),
                certificado.getNomeAluno(),
                certificado.getCurso().getNome(),
                certificado.getCurso().getAdminResponsavel().getNomeInstituicao(),
                certificado.getCodValidacao(),
                java.time.LocalDate.now()
        );
    }
}