package com.generations.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generations.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
  
}
