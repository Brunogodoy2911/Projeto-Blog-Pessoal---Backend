package com.generations.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_temas")
public class Tema {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 1000)
  @NotBlank(message = "O atributo descrição é obrigatório!")
  @Size(min = 10, max = 1000, message = "O atributo descrição deve ter no mínimo 10 e no máximo 1000 caracteres!")
  private String descricao;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE)
  @JsonIgnoreProperties("tema")
  private List<Postagem> postagem;

}
