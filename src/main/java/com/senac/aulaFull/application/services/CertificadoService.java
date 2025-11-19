package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.DTO.certificado.CertificadoRequestDto;
import com.senac.aulaFull.domain.model.Certificado;
import com.senac.aulaFull.domain.model.Curso;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.domain.repository.CertificadoRepository;
import com.senac.aulaFull.domain.repository.CursoRepository;
import com.senac.aulaFull.domain.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public Certificado emitirCertificado(CertificadoRequestDto dados, Usuario adminLogado) {
        Usuario aluno = usuarioRepository.findById(dados.alunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        if (!"ROLE_ADMIN_MASTER".equals(adminLogado.getRole())) {
            Long idInstituicaoAdmin = adminLogado.getInstituicao().getId();

            if (!curso.getInstituicao().getId().equals(idInstituicaoAdmin)) {
                throw new RuntimeException("este curso não pertence à sua instituição.");
            }

            if (!aluno.getInstituicao().getId().equals(idInstituicaoAdmin)) {
                throw new RuntimeException("este aluno não pertence à sua instituição.");
            }
        }

        Certificado novoCertificado = new Certificado();
        novoCertificado.setUsuario(aluno);
        novoCertificado.setCurso(curso);
        novoCertificado.setNomeAluno(aluno.getNome());

        return certificadoRepository.save(novoCertificado);
    }

    public List<Certificado> listarCertificados(Usuario usuarioLogado) {
        String role = usuarioLogado.getRole();

        if ("ROLE_ADMIN_MASTER".equals(role)) {
            return certificadoRepository.findAll();
        }
        else if ("ROLE_ADMIN_INSTITUICAO".equals(role)) {
            return certificadoRepository.findByCurso_Instituicao(usuarioLogado.getInstituicao());
        }
        else {
            return certificadoRepository.findByUsuario(usuarioLogado);
        }
    }

    public void deletarCertificado(Long id, Usuario adminLogado) {
        Certificado certificado = certificadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));

        if (!"ROLE_ADMIN_MASTER".equals(adminLogado.getRole())) {
            if (!certificado.getCurso().getInstituicao().getId().equals(adminLogado.getInstituicao().getId())) {
                throw new RuntimeException("você não pode deletar este certificado.");
            }
        }
        certificadoRepository.deleteById(id);
    }
}