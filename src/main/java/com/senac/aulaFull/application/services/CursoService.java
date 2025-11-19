package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.DTO.curso.CursoRequestDto;
import com.senac.aulaFull.application.DTO.curso.CursoResponseDto;
import com.senac.aulaFull.domain.model.Curso;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.domain.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    // cria curso para o admin logado
    public CursoResponseDto criarCurso(CursoRequestDto dados, Usuario adminLogado) {
        Curso novoCurso = new Curso();
        novoCurso.setNome(dados.nome());

        //vincula à instituição do admin
        novoCurso.setInstituicao(adminLogado.getInstituicao());

        Curso cursoSalvo = cursoRepository.save(novoCurso);

        return new CursoResponseDto(
                cursoSalvo.getId(),
                cursoSalvo.getNome()
        );
    }

    public List<CursoResponseDto> listarCursos(Usuario adminLogado) {
        List<Curso> cursos = cursoRepository.findByInstituicao(adminLogado.getInstituicao());

        return cursos.stream()
                .map(curso -> new CursoResponseDto(
                        curso.getId(),
                        curso.getNome())
                )
                .collect(Collectors.toList());
    }

    public void deletarCurso(Long id, Usuario adminLogado) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        if (!curso.getInstituicao().getId().equals(adminLogado.getInstituicao().getId())) {
            throw new RuntimeException("Acesso Negado: Você não pode deletar cursos de outra instituição.");
        }

        cursoRepository.delete(curso);
    }
}