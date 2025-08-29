package com.senac.aulaFull.controller;

import com.senac.aulaFull.model.Usuario;
import com.senac.aulaFull.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name= "Controlador de usuarios", description = "Camada responsável por controlar os registros de usuários")
public class UsuarioController {

   @Autowired
   private UsuarioRepository usuarioRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> consultaPorID (@PathVariable Long id){
         var usuario = usuarioRepository.findById(id).orElse(null);

         if (usuario == null){
                return ResponseEntity.notFound().build();
         }
       return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "usuarios",description = "consulta todos os usuários cadastrados.")
    public ResponseEntity<?>consultarTodos(){
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Salvar Usuario",description = "função executada para criar um usuário.")
    public ResponseEntity<?>salvarUsuario(@RequestBody Usuario usuario){
        try {
            var usuarioResponse = usuarioRepository.save(usuario);
            return  ResponseEntity.ok(usuarioResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
