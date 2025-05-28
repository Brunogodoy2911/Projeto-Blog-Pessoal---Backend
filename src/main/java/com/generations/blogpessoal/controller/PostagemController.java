package com.generations.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generations.blogpessoal.model.Postagem;
import com.generations.blogpessoal.repository.PostagemRepository;
import com.generations.blogpessoal.repository.TemaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Postagem", description = "Informações de Postagem")
public class PostagemController {

  @Autowired
  private PostagemRepository postagemRepository;

  @Autowired
  private TemaRepository temaRepository;

  @GetMapping
  @Operation(summary = "Retornar todas as postagens", description = "Essa função é responsável por retornar todas as postagens existentes no banco de dados.")
  public ResponseEntity<List<Postagem>> getAll() {
    return ResponseEntity.ok(postagemRepository.findAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Retorna postagem por ID", description = "Essa função é responsável por retornar a postagem com o ID informado.")
  public ResponseEntity<Postagem> getById(@PathVariable Long id) {
    return postagemRepository.findById(id)
        .map(resposta -> ResponseEntity.ok(resposta))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/titulo/{titulo}")
  @Operation(summary = "Retorna postagens pelo titulo", description = "Essa função é responsável por retornar todas as postagens com o titulo informado.")
  public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable String titulo) {
    return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
  }

  @PostMapping
  @Operation(summary = "Cadastrar postagem", description = "Essa função é responsável por cadastrar a postagem informada no banco de dados.")
  public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
    return temaRepository.findById(postagem.getTema().getId())
        .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem)))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não encontrado", null));
  }

  @PutMapping
  @Operation(summary = "Atualizar postagem", description = "Essa função é responsável por atualizar uma postagem específica.")
  public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {

    if (postagem.getId() == null)
      return ResponseEntity.badRequest().build();

    return postagemRepository.findById(postagem.getId())
        .map(resposta -> {
          if (!temaRepository.existsById(postagem.getTema().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não encontrado!");
          }
          return ResponseEntity.ok(postagemRepository.save(postagem));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Deletar postagem", description = "Essa função é responsável por deletar a postagem informda do banco de dados.")
  public void delete(@PathVariable Long id) {

    Optional<Postagem> postagem = postagemRepository.findById(id);

    if (postagem.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    postagemRepository.deleteById(id);

  }
}
