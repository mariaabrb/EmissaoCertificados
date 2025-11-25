package com.senac.aulaFull.domain.interfaces;

public interface IEnvioEmail {

    void enviarEmailSimples(String para, String assunto, String texto);

    void enviarEmailHtml(String para, String assunto, String html);
}