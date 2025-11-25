package com.senac.aulaFull.infra.presentation;

import com.senac.aulaFull.application.DTO.usuario.UsuarioRequestDto;
import com.senac.aulaFull.application.DTO.usuario.UsuarioResponseDto;
import com.senac.aulaFull.application.DTO.usuario.AlterarSenhaDto;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.application.services.UsuarioService;
import com.senac.aulaFull.domain.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/usuarios")
@Tag(name= "Controller de Usuários", description = "Controlador para o gerenciamento de usuários")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Endpoint público para registro de novos usuários.")
    public ResponseEntity<?> salvarUsuario(@RequestBody UsuarioRequestDto dados){
        try {
            Usuario usuarioSalvo = usuarioService.criarAlunoWebFlow(dados);

            UsuarioResponseDto respostaDto = new UsuarioResponseDto(
                    usuarioSalvo.getId(),
                    usuarioSalvo.getNome(),
                    usuarioSalvo.getEmail(),
                    usuarioSalvo.getRole(),
                    usuarioSalvo.getNomeInstituicao()
            );
            return new ResponseEntity<>(respostaDto, HttpStatus.CREATED);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Erro interno ao salvar usuário.");
        }
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista de todos os usuários da instituição do Admin logado.")
    public ResponseEntity<List<UsuarioResponseDto>> consultarTodos(
            @AuthenticationPrincipal Usuario usuarioLogado
    ){
        String nomeInstituicaoAdmin = usuarioLogado.getNomeInstituicao();

        if (nomeInstituicaoAdmin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(List.of());
        }

        List<UsuarioResponseDto> usuarios = usuarioService.listarUsuariosDaInstituicao(nomeInstituicaoAdmin);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário por ID", description = "Retorna um usuário específico. Requer perfil de ADMIN.")
    public ResponseEntity<UsuarioResponseDto> consultaPorID (@PathVariable Long id){
        UsuarioResponseDto usuarioDto = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuarioDto);
    }

    @PutMapping("/me/senha")
    @Operation(summary = "Alterar a própria senha", description = "Altera a senha do usuário logado.")
    public ResponseEntity<?> alterarMinhaSenha(
            @RequestBody AlterarSenhaDto dto,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("usuário não autenticado");
        }

        try {
            usuarioService.alterarSenhaUsuarioLogado(usuarioLogado, dto);
            return ResponseEntity.ok("senha alterada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("erro ao alterar senha.");
        }
    }

    @GetMapping("/instituicoes/nomes")
    @Operation(summary = "Lista Nomes de Instituições", description = "Retorna a lista de nomes das instituições existentes para o dropdown de cadastro.")
    public ResponseEntity<List<String>> listarNomesInstituicoes() {

        List<String> nomes = usuarioService.listarNomesInstituicoes();
        return ResponseEntity.ok(nomes);
    }
}