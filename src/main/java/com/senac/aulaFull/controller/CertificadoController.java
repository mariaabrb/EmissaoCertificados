package com.senac.aulaFull.controller;

import com.senac.aulaFull.DTO.CertificadoRequestDto;
import com.senac.aulaFull.domain.model.Certificado;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.services.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/certificados")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @PostMapping
    public ResponseEntity<Certificado> emitir(@RequestBody CertificadoRequestDto requestDTO) {
        Certificado novoCertificado = certificadoService.emitirCertificado(requestDTO);
        return new ResponseEntity<>(novoCertificado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Certificado>> listar(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        List<Certificado> certificados = certificadoService.listarCertificados(usuarioLogado);
        return ResponseEntity.ok(certificados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        certificadoService.deletarCertificado(id);
        return ResponseEntity.noContent().build();
    }
}