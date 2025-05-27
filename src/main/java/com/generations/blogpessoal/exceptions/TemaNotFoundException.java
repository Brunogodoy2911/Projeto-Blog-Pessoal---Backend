package com.generations.blogpessoal.exceptions;

public class TemaNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public TemaNotFoundException(String message) {
    super(message);
  }

  public TemaNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public TemaNotFoundException(Throwable cause) {
    super(cause);
  }
}
