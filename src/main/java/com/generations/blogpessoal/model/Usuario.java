package com.generations.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_usuarios")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "O Atributo Nome é Obrigatório!")
  @Schema(example = "Bruno Godoy", requiredMode = RequiredMode.REQUIRED, description = "Nome do usuário")
  private String nome;

  @NotBlank(message = "O Atributo Usuário é Obrigatório!")
  @Email(message = "O Atributo Usuário deve ser um email válido!")
  @Schema(example = "email@email.com.br", requiredMode = RequiredMode.REQUIRED, description = "Email do usuário")
  private String usuario;

  @NotBlank(message = "O Atributo Senha é Obrigatório!")
  @Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres")
  @Schema(example = "123456", requiredMode = RequiredMode.REQUIRED, description = "Senha do usuário")
  private String senha;

  @Size(max = 5000, message = "O link da foto não pode ser maior do que 5000 caracteres")
  @Schema(example = "http://imageurl.com", description = "Foto do usuário")
  private String foto;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario", cascade = CascadeType.REMOVE)
  @JsonIgnoreProperties("usuario")
  private List<Postagem> postagem;

}