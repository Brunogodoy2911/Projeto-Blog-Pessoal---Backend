package com.generations.blogpessoal.controller;

import java.util.List;

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

import com.generations.blogpessoal.exceptions.TemaNotFoundException;
import com.generations.blogpessoal.model.Tema;
import com.generations.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

  @Autowired
  private TemaRepository temaRepository;

  @GetMapping
  public ResponseEntity<List<Tema>> getAll() {
    return ResponseEntity.ok(temaRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tema> getById(@PathVariable Long id) {
    return temaRepository.findById(id)
        .map(resposta -> ResponseEntity.ok(resposta))
        .orElseThrow(() -> new TemaNotFoundException("Tema com ID " + id + " não encontrado"));
  }

  @GetMapping("/descricao/{descricao}")
  public ResponseEntity<List<Tema>> getAllByDescricao(@PathVariable String descricao) {
    return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
  }

  @PostMapping
  public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema) {
    return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(tema));
  }

  @PutMapping
  public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema) {
    if (tema.getId() == null) {
      return ResponseEntity.badRequest().build();
    }

    return temaRepository.findById(tema.getId())
        .map(resposta -> ResponseEntity.ok(temaRepository.save(tema)))
        .orElseThrow(() -> new TemaNotFoundException("Tema com ID " + tema.getId() + " não encontrado"));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {

    if (!temaRepository.existsById(id))
      throw new TemaNotFoundException("Tema com ID " + id + " não encontrado");

    temaRepository.deleteById(id);
  }
}
