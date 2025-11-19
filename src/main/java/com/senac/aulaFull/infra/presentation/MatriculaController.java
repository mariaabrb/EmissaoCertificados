package com.senac.aulaFull.infra.presentation;

import com.senac.aulaFull.application.DTO.curso.CursoResponseDto;
import com.senac.aulaFull.application.DTO.usuario.UsuarioResponseDto;
import com.senac.aulaFull.services.MatriculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name= "Controller de Matrículas", description = "Controlador para gerenciar a associação entre usuários e cursos")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping("/cursos/{cursoId}/alunos/{alunoId}")
    @Operation(summary = "Matricular Aluno", description = "Associa um usuário (aluno) a um curso específico. Requer perfil ADMIN.")
    public ResponseEntity<Void> matricularAluno(
            @PathVariable Long cursoId,
            @PathVariable Long alunoId) {
        try {
            matriculaService.matricularAlunoEmCurso(alunoId, cursoId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/cursos/{cursoId}/alunos/{alunoId}")
    @Operation(summary = "Desmatricular Aluno", description = "Remove a associação entre um usuário e um curso. Requer perfil ADMIN.")
    public ResponseEntity<Void> desmatricularAluno(
            @PathVariable Long cursoId,
            @PathVariable Long alunoId) {
        try {
            matriculaService.desmatricularAlunoDeCurso(alunoId, cursoId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cursos/{cursoId}/alunos")
    @Operation(summary = "Listar Alunos por Curso", description = "Retorna a lista de usuários matriculados em um curso específico. Requer perfil ADMIN.")
    public ResponseEntity<List<UsuarioResponseDto>> listarAlunosPorCurso(
            @PathVariable Long cursoId) {
        try {
            List<UsuarioResponseDto> alunos = matriculaService.listarAlunosPorCurso(cursoId);
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{alunoId}/cursos")
    @Operation(summary = "Listar Cursos por Aluno", description = "Retorna a lista de cursos em que um usuário está matriculado. Requer perfil ADMIN.")
    public ResponseEntity<List<CursoResponseDto>> listarCursosPorAluno(
            @PathVariable Long alunoId) {
        try {
            List<CursoResponseDto> cursos = matriculaService.listarCursosPorAluno(alunoId);
            return ResponseEntity.ok(cursos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}