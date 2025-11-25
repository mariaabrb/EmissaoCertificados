package com.senac.aulaFull.infra.presentation;

import com.senac.aulaFull.application.DTO.certificado.CertificadoRequestDto;
import com.senac.aulaFull.application.DTO.certificado.CertificadoResponseDto;
import com.senac.aulaFull.domain.model.Certificado;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.application.services.CertificadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificados")
@Tag(name = "Controller de Certificados", description = "Emissão e gestão de certificados (Isolado por Instituição)")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @PostMapping
    @Operation(summary = "Emitir Certificado", description = "Admin emite certificado para aluno matriculado em sua instituição")
    public ResponseEntity<?> emitir(
            @RequestBody CertificadoRequestDto requestDTO,
            @AuthenticationPrincipal Usuario usuarioLogado //admin logado
    ) {
        try {
            // verifica se o curso e o aluno pertencem a mesma instituiçaop do admin
            Certificado novoCertificado = certificadoService.emitirCertificado(requestDTO, usuarioLogado);
            return new ResponseEntity<>(novoCertificado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar Certificados", description = "Retorna certificados baseados no perfil (Aluno vê os seus, admin vê da escola)")
    public ResponseEntity<List<CertificadoResponseDto>> listar(
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        List<CertificadoResponseDto> certificados = certificadoService.listarCertificadosDTO(usuarioLogado);
        return ResponseEntity.ok(certificados);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Certificado", description = "Admin remove certificado (apenas da sua instituição).")
    public ResponseEntity<?> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        try {
            certificadoService.deletarCertificado(id, usuarioLogado);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/validar/{codigo}")
    @Operation(summary = "Validar Certificado (Público)", description = "Qualquer pessoa com o código pode validar.")
    public ResponseEntity<?> validar(@PathVariable String codigo) {
        try {
            CertificadoResponseDto dto = certificadoService.validarPorCodigo(codigo);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Certificado inválido ou não encontrado.");
        }
    }
}