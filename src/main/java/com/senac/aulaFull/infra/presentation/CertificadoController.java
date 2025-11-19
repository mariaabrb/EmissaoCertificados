package com.senac.aulaFull.infra.presentation;

import com.senac.aulaFull.application.DTO.certificado.CertificadoRequestDto;
import com.senac.aulaFull.domain.model.Certificado;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.application.services.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/certificados")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @PostMapping
    public ResponseEntity<Certificado> emitir(
            @RequestBody CertificadoRequestDto requestDTO,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        Certificado novoCertificado = certificadoService.emitirCertificado(requestDTO, usuarioLogado);
        return new ResponseEntity<>(novoCertificado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Certificado>> listar(
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        List<Certificado> certificados = certificadoService.listarCertificados(usuarioLogado);
        return ResponseEntity.ok(certificados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        certificadoService.deletarCertificado(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

}