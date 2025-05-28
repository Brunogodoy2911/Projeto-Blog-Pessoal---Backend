package com.generations.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_postagens")
public class Postagem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100)
  @NotBlank(message = "O atributo título é obrigatório!")
  @Size(min = 5, max = 100, message = "O atributo título deve ter no mínimo 5 e no máximo 100 caracteres!")
  @Schema(example = "Como fazer um arroz bem soltinho", requiredMode = RequiredMode.REQUIRED, description = "Título da postagem")
  private String titulo;

  @Column(length = 1000)
  @NotBlank(message = "O atributo texto é obrigatório!")
  @Size(min = 10, max = 1000, message = "O atributo texto deve ter no mínimo 10 e no máximo 1000 caracteres!")
  @Schema(example = "Para fazer um arroz bem soltinho pegue uma panela....", requiredMode = RequiredMode.REQUIRED, description = "Texto da postagem")
  private String texto;

  @UpdateTimestamp
  private LocalDateTime data;

  @ManyToOne
  @JsonIgnoreProperties("postagem")
  private Tema tema;

  @ManyToOne
  @JsonIgnoreProperties("postagem")
  private Usuario usuario;

}
