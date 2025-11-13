package com.senac.aulaFull.services;

import com.senac.aulaFull.application.DTO.certificado.CertificadoRequestDto;
import com.senac.aulaFull.domain.model.Certificado;
import com.senac.aulaFull.domain.model.Curso;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.repository.CertificadoRepository;
import com.senac.aulaFull.repository.CursoRepository;
import com.senac.aulaFull.repository.UsuarioRepository;
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

    public Certificado emitirCertificado(CertificadoRequestDto dados) {
        Usuario aluno = usuarioRepository.findById(dados.alunoId())
                .orElseThrow(() -> new EntityNotFoundException("aluno não encontrado"));

        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("curso não encontrado"));

        Certificado novoCertificado = new Certificado();
        novoCertificado.setUsuario(aluno);
        novoCertificado.setCurso(curso);
        novoCertificado.setNomeAluno(aluno.getNome());

        return certificadoRepository.save(novoCertificado);
    }

    public List<Certificado> listarCertificados(Usuario usuarioLogado) {
        if ("ROLE_ADMIN".equals(usuarioLogado.getRole())) {
            return certificadoRepository.findAll();
        } else {
            return certificadoRepository.findByUsuario(usuarioLogado);
        }
    }

    public void deletarCertificado(Long id) {
        if (!certificadoRepository.existsById(id)) {
            throw new EntityNotFoundException("certificado não encontrado");
        }
        certificadoRepository.deleteById(id);
    }
}