package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.DTO.instituicao.CriarInstituicaoRequestDto;
import com.senac.aulaFull.application.DTO.login.EsqueciMinhaSenhaDto;
import com.senac.aulaFull.application.DTO.usuario.AlterarSenhaDto;
import com.senac.aulaFull.application.DTO.usuario.SenhaCodigoRequestDto;
import com.senac.aulaFull.application.DTO.usuario.UsuarioRequestDto;
import com.senac.aulaFull.application.DTO.usuario.UsuarioResponseDto;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.domain.repository.UsuarioRepository;
import com.senac.aulaFull.infra.external.EnvioEmailRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnvioEmailRepository envioEmailRepository;

    @Transactional
    public void criarAdminDesktopFlow(CriarInstituicaoRequestDto dados) {

        if (usuarioRepository.findByEmail(dados.emailAdmin()).isPresent()) {
            throw new RuntimeException("O e-mail do administrador já está em uso.");
        }

        Usuario novoAdmin = new Usuario();
        novoAdmin.setNome(dados.nomeAdmin());
        novoAdmin.setEmail(dados.emailAdmin());
        novoAdmin.setCPF(dados.cpfAdmin());

        String senhaCriptografada = passwordEncoder.encode(dados.senhaAdmin());
        novoAdmin.setSenha(senhaCriptografada);

        novoAdmin.setRole("ROLE_ADMIN");
        novoAdmin.setNomeInstituicao(dados.nomeInstituicao());

        usuarioRepository.save(novoAdmin);
    }

    @Transactional
    public Usuario criarAlunoWebFlow(UsuarioRequestDto dados) {
        if (usuarioRepository.findByEmail(dados.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.nome());
        novoUsuario.setEmail(dados.email());
        novoUsuario.setCPF(dados.cpf());

        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        novoUsuario.setSenha(senhaCriptografada);

        novoUsuario.setRole("ROLE_USER");
        novoUsuario.setNomeInstituicao(dados.nomeInstituicao());

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
                .collect(Collectors.toList());
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
                usuario.getRole(),
                usuario.getNomeInstituicao()
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
        String htmlContent = """
            <div style="font-family: Arial, sans-serif; background-color: #F0FFF0; padding: 40px; text-align: center;">
                <div style="max-width: 500px; margin: 0 auto; background-color: #ffffff; border-radius: 15px; padding: 30px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                    <h2 style="color: #2E8B57;">Certify Pro</h2>
                    <p style="color: #555; font-size: 16px;">Olá, <strong>%s</strong>!</p>
                    <p style="color: #666; font-size: 14px;">Você solicitou a redefinição da sua senha. Use o código abaixo para continuar:</p>
                    
                    <div style="background-color: #FFF0F5; border: 2px solid #FFB6C1; border-radius: 10px; padding: 15px; margin: 20px 0;">
                        <span style="font-size: 32px; font-weight: bold; letter-spacing: 5px; color: #D81B60;">%s</span>
                    </div>
                    
                    <p style="color: #999; font-size: 12px;">Este código expira em 15 minutos.</p>
                    <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                    <p style="color: #aaa; font-size: 11px;">Se você não solicitou isso, por favor ignore este e-mail.</p>
                </div>
            </div>
        """.formatted(usuario.getNome(), codigo);

        envioEmailRepository.enviarEmailHtml(usuario.getEmail(), assunto, htmlContent);
    }

    public void redefinirSenha(SenhaCodigoRequestDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado ou código inválido."));

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


    private String gerarCodigoAleatorio(int tamanho) {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            codigo.append(random.nextInt(10));
        }
        return codigo.toString();
    }
    public List<UsuarioResponseDto> listarUsuariosDaInstituicao(String nomeInstituicaoAdmin) {
        List<Usuario> usuarios = usuarioRepository.findByNomeInstituicao(nomeInstituicaoAdmin);

        return usuarios.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<String> listarNomesInstituicoes() {
        return usuarioRepository.findDistinctNomeInstituicao();
    }
}