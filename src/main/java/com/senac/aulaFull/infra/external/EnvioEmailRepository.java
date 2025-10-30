package com.senac.aulaFull.infra.external;

import com.senac.aulaFull.domain.interfaces.IEnvioEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EnvioEmailRepository implements IEnvioEmail {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Override
    @Async
    public void enviarEmailSimples(String para, String assunto, String texto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);
            javaMailSender.send(message);
        } catch (Exception e) {

            System.err.println("erro ao enviar e-mail simples: " + e.getMessage());
        }
    }

}