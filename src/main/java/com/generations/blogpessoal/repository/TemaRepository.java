package com.generations.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generations.blogpessoal.model.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long> {

  List<Tema> findAllByDescricaoContainingIgnoreCase(String descricao);
}
