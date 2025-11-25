package com.senac.aulaFull.infra.presentation;

import com.senac.aulaFull.application.DTO.curso.CursoRequestDto;
import com.senac.aulaFull.application.DTO.curso.CursoResponseDto;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.application.services.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "Controller de Cursos", description = "gerenciamento de cursos da instituição")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    @Operation(summary = "Criar Curso", description = "cria um curso vinculado a instituição do admin logado.")
    public ResponseEntity<CursoResponseDto> criarCurso(
            @RequestBody CursoRequestDto dados,
            @AuthenticationPrincipal Usuario adminLogado
    ) {
        // passa o adminlogado para o serviço saber qual a instituiçao
        CursoResponseDto novoCurso = cursoService.criarCurso(dados, adminLogado);
        return new ResponseEntity<>(novoCurso, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar Cursos", description = "lista apenas os cursos da instituição do admin logado.")
    public ResponseEntity<List<CursoResponseDto>> listarCursos(
            @AuthenticationPrincipal Usuario adminLogado
    ) {
        List<CursoResponseDto> cursos = cursoService.listarCursos(adminLogado);
        return ResponseEntity.ok(cursos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Curso", description = "deleta um curso (apenas se pertencer à instituição do admin).")
    public ResponseEntity<Void> deletarCurso(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario adminLogado
    ) {
        try {
            cursoService.deletarCurso(id, adminLogado);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Curso", description = "Altera o nome de um curso existente. Requer perfil de ADMIN.")
    public ResponseEntity<?> atualizarCurso(
            @PathVariable Long id,
            @RequestBody CursoRequestDto dados,
            @AuthenticationPrincipal Usuario adminLogado
    ) {
        try {
            CursoResponseDto cursoAtualizado = cursoService.atualizarCurso(id, dados, adminLogado);
            return ResponseEntity.ok(cursoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}