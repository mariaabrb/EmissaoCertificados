package com.senac.aulaFull.controller;

import com.senac.aulaFull.DTO.AlterarSenhaDto;
import com.senac.aulaFull.DTO.UsuarioRequestDto;
import com.senac.aulaFull.DTO.UsuarioResponseDto;
import com.senac.aulaFull.domain.model.Usuario;
import com.senac.aulaFull.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios") // Mapeamento base para usuários
@Tag(name= "Controller de Usuários", description = "Controlador para o gerenciamento de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Endpoint público para registro de novos usuários.")
    public ResponseEntity<UsuarioResponseDto> salvarUsuario(@RequestBody UsuarioRequestDto dados){
        try {
            Usuario usuarioSalvo = usuarioService.criarUsuario(dados);
            UsuarioResponseDto respostaDto = new UsuarioResponseDto(
                    usuarioSalvo.getId(),
                    usuarioSalvo.getNome(),
                    usuarioSalvo.getEmail(),
                    usuarioSalvo.getRole()
            );
            return new ResponseEntity<>(respostaDto, HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista de todos os usuários. Requer perfil de ADMIN.")
    public ResponseEntity<List<UsuarioResponseDto>> consultarTodos(){
        List<UsuarioResponseDto> usuarios = usuarioService.listarTodos();
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
            // @AuthenticationPrincipal injeeta o objeto Usuariologado
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
}