package com.senac.aulaFull.services;

import com.senac.aulaFull.model.Certificado;
import com.senac.aulaFull.repository.CertificadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;
        public Certificado emitirCertificado(Certificado certificado) {
            return certificadoRepository.save(certificado);
        }

        public Certificado validarCertificado(String cod) {
            return certificadoRepository.findByCodValidacao(cod).orElseThrow(()
                    -> new RuntimeException("Certificado não encontrado"));
        }
}
