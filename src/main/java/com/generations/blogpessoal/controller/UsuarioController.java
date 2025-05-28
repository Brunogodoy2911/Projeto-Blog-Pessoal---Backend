package com.generations.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generations.blogpessoal.model.UsuarioLogin;
import com.generations.blogpessoal.model.Usuario;
import com.generations.blogpessoal.repository.UsuarioRepository;
import com.generations.blogpessoal.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Usuarios", description = "Informações do Usuário")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @GetMapping("/all")
  @Operation(summary = "Retornar todos os usuários", description = "Essa função é responsável por buscar todos os usuários cadastrados no banco de dados.")
  public ResponseEntity<List<Usuario>> getAll() {

    return ResponseEntity.ok(usuarioRepository.findAll());

  }

  @GetMapping("/{id}")
  @Operation(summary = "Retornar usuário por ID", description = "Essa função é responsável por retornar usuário pelo ID informado.")
  public ResponseEntity<Usuario> getById(@PathVariable Long id) {
    return usuarioRepository.findById(id)
        .map(resposta -> ResponseEntity.ok(resposta))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/logar")
  @Operation(summary = "Logar usuário com as credenciais", description = "Essa função é responsável por logar usuário com usuario(email) e senha.")
  public ResponseEntity<UsuarioLogin> autenticar(@RequestBody Optional<UsuarioLogin> usuarioLogin) {

    return usuarioService.autenticarUsuario(usuarioLogin)
        .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @PostMapping("/cadastrar")
  @Operation(summary = "Cadastrar usuário", description = "Essa função é responsável por cadastrar usuário.")
  public ResponseEntity<Usuario> post(@RequestBody @Valid Usuario usuario) {

    return usuarioService.cadastrarUsuario(usuario)
        .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
        .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

  }

  @PutMapping("/atualizar")
  @Operation(summary = "Atualizar usuário", description = "Essa função é responsável por atualizar os dados do usuário.")
  public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {

    return usuarioService.atualizarUsuario(usuario)
        .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

  }

}