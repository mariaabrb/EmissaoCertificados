package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.DTO.instituicao.CriarInstituicaoRequestDto;
import com.senac.aulaFull.domain.model.Instituicao;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.domain.repository.InstituicaoRepository;
import com.senac.aulaFull.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //se der erro ao salvar o usuario, ele cancela e apaga a ionstituição criada,
    // pq ele precisa alterar 2 dados no banco
    @Transactional
    public void criarInstituicaoEAdmin(CriarInstituicaoRequestDto dados) {

        if (usuarioRepository.findByEmail(dados.emailAdmin()).isPresent()) {
            throw new RuntimeException("o email do administrador já está em uso");
        }
    //cria instituicao e salva cidade
        Instituicao novaInstituicao = new Instituicao();
        novaInstituicao.setNome(dados.nomeInstituicao());
        novaInstituicao.setCidade(dados.cidade());

        Instituicao instituicaoSalva = instituicaoRepository.save(novaInstituicao);

        //cria o usuarioa dmin vinculado nessa instituicao
        Usuario adminInstituicao = new Usuario();
        adminInstituicao.setNome(dados.nomeAdmin());
        adminInstituicao.setEmail(dados.emailAdmin());
        adminInstituicao.setCPF(dados.cpfAdmin());

        String senhaCriptografada = passwordEncoder.encode(dados.senhaAdmin());
        adminInstituicao.setSenha(senhaCriptografada);

        //seta usuario p ser admin da sua instituicao
        adminInstituicao.setRole("ROLE_ADMIN_INSTITUICAO");
        adminInstituicao.setInstituicao(instituicaoSalva);

        usuarioRepository.save(adminInstituicao);
    }
}