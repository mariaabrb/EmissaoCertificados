package com.senac.aulaFull.controller;

import com.senac.aulaFull.model.Certificado;
import com.senac.aulaFull.repository.CertificadoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificados")
@Tag(name = "Controller de Certificado", description = "Cria e valida certificados")
public class CertificadoController {

    @Autowired
    private CertificadoRepository certificadoRepository;

    @PostMapping
    @Operation(summary = "emite um novo certificado", description = "cria e salva um certificado no banco")
    public ResponseEntity<?> emitir(@RequestBody Certificado certificado){
        try {
            var certificadoSalvo = certificadoRepository.save(certificado);
            return ResponseEntity.ok(certificadoSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/validar/{cod}")
    @Operation(summary = "valida um certificado pelo código", description = "fornece o certificado validado")
    public ResponseEntity<?> validar(@PathVariable String cod){
        var certificado = certificadoRepository.findByCodValidacao(cod).orElse(null);

        if(certificado == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(certificado);
        }
    }
}
