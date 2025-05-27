package com.generations.blogpessoal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionHandlerController {
  public ResponseEntity<String> handleTemaNotFoundException(TemaNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
}
