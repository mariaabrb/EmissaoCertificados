package com.senac.aulaFull.services;

import com.senac.aulaFull.DTO.*;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.infra.external.EnvioEmailRepository;
import com.senac.aulaFull.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnvioEmailRepository envioEmailRepository;

    public Usuario criarUsuario(UsuarioRequestDto dados) {
        if (usuarioRepository.findByEmail(dados.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
        if (usuarioRepository.findByCPF(dados.cpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado.");
        }
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.nome());
        novoUsuario.setEmail(dados.email());

        novoUsuario.setCPF(dados.cpf());

        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setRole("USER");

        return usuarioRepository.save(novoUsuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public UsuarioResponseDto buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
        return toResponseDto(usuario);
    }

    private UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }

    public void solicitarCodigoRedefinicaoSenha(EsqueciMinhaSenhaDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuário com e-mail " + dto.email() + " não encontrado."));

        String codigo = gerarCodigoAleatorio(6);
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        usuario.setCodigoResetSenha(codigo);
        usuario.setCodExpiracao(expiryDate);
        usuarioRepository.save(usuario);

        String assunto = "Seu código de redefinição de senha - Certify Pro";
        String texto = "Olá, " + usuario.getNome() + ".\n\n" +
                "Use o seguinte código para redefinir sua senha:\n\n" +
                "Código: " + codigo + "\n\n" +
                "Este código expira em 15 minutos.";

        envioEmailRepository.enviarEmailSimples(usuario.getEmail(), assunto, texto);
    }

    public void redefinirSenha(SenhaCodigoRequestDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado ou código inválido."));

        // verifica se o código de reset existe, se a data de expiração existe,
        // se a data de expiração ainda nao passou,
        // e se o código no banco é igual ao cod que o usuário digitou
        if (usuario.getCodigoResetSenha() == null ||
                usuario.getCodExpiracao() == null ||
                usuario.getCodExpiracao().isBefore(LocalDateTime.now()) ||
                !usuario.getCodigoResetSenha().equals(dto.code())) {
            throw new IllegalArgumentException("Código inválido ou expirado.");
        }


        String novaSenhaCriptografada = passwordEncoder.encode(dto.novaSenha());
        usuario.setSenha(novaSenhaCriptografada);

        usuario.setCodigoResetSenha(null);
        usuario.setCodExpiracao(null);

        usuarioRepository.save(usuario);
    }


    private String gerarCodigoAleatorio(int tamanho) {

        Random random = new Random();
        StringBuilder codigo = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            codigo.append(random.nextInt(10));
        }
        return codigo.toString();
    }

    public void alterarSenhaUsuarioLogado(Usuario usuarioLogado, AlterarSenhaDto dto) {
        if (!passwordEncoder.matches(dto.senhaAtual(), usuarioLogado.getPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

        if (dto.senhaNova() == null || dto.senhaNova().length() < 6) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(dto.senhaNova());
        usuarioLogado.setSenha(novaSenhaCriptografada);
        usuarioRepository.save(usuarioLogado);
    }
}
