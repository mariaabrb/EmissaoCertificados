package com.senac.aulaFull.controller;

import com.senac.aulaFull.DTO.CertificadoRequestDto;
import com.senac.aulaFull.DTO.CertificadoResponseDto;
import com.senac.aulaFull.model.Usuario;
import com.senac.aulaFull.services.CertificadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/certificados")
@Tag(name = "Controller de Certificados", description = "Endpoints para o CRUD completo de certificados")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cria um novo certificado", description = "cria um novo certificado associado ao usuário logado.")
    public ResponseEntity<CertificadoResponseDto> emitir(@RequestBody CertificadoRequestDto requestDTO, Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        CertificadoResponseDto novoCertificado = certificadoService.emitirCertificado(requestDTO, usuarioLogado);
        return new ResponseEntity<>(novoCertificado, HttpStatus.CREATED);
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Lista certificados", description = "Lista os certificados do usuário logado (ou todos, se for ADMIN).")
    public ResponseEntity<List<CertificadoResponseDto>> listar(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(certificadoService.listarCertificados(usuarioLogado));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualiza um certificado", description = "Atualiza um certificado existente que pertença ao usuário logado.")
    public ResponseEntity<CertificadoResponseDto> atualizar(@PathVariable Long id, @RequestBody CertificadoRequestDto dados, Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        CertificadoResponseDto certAtualizado = certificadoService.atualizarCertificado(id, dados, usuarioLogado);
        return ResponseEntity.ok(certAtualizado);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Deleta um certificado", description = "Deleta um certificado existente que pertença ao usuário logado.")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        certificadoService.deletarCertificado(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validar/{codigo}")
    @Operation(summary = "Valida um certificado pelo código", description = "Endpoint público para verificar a autenticidade de um certificado.")
    public ResponseEntity<CertificadoResponseDto> validar(@PathVariable String codigo) {
        return ResponseEntity.ok(certificadoService.validarPorCodigo(codigo));
    }
}