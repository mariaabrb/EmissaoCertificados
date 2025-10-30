package com.senac.aulaFull.services;

import com.senac.aulaFull.DTO.CursoResponseDto;
import com.senac.aulaFull.DTO.UsuarioResponseDto;
import com.senac.aulaFull.domain.model.Curso;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.repository.CursoRepository;
import com.senac.aulaFull.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public void matricularAlunoEmCurso(Long usuarioId, Long cursoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("usuário não encontrado com ID: " + usuarioId));
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("curso não encontrado com ID: " + cursoId));

        usuario.getCursosMatriculados().add(curso);
        curso.getAlunosMatriculados().add(usuario);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desmatricularAlunoDeCurso(Long usuarioId, Long cursoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + usuarioId));
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + cursoId));


        usuario.getCursosMatriculados().remove(curso);
        curso.getAlunosMatriculados().remove(usuario);

        usuarioRepository.save(usuario);
    }

    public List<UsuarioResponseDto> listarAlunosPorCurso(Long cursoId) {
        if (!cursoRepository.existsById(cursoId)) {
            throw new EntityNotFoundException("Curso não encontrado com ID: " + cursoId);
        }

        List<Usuario> alunos = usuarioRepository.findByCursosMatriculados_Id(cursoId);

        return alunos.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }
    public List<CursoResponseDto> listarCursosPorAluno(Long alunoId) {
        Usuario usuario = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("usuário não encontrado com ID: " + alunoId));
        return usuario.getCursosMatriculados()
                .stream()
                .map(this::toCursoResponseDto)
                .collect(Collectors.toList());
    }

    private CursoResponseDto toCursoResponseDto(Curso curso) {
        return new CursoResponseDto(
                curso.getId(),
                curso.getNome()
        );
    }
}