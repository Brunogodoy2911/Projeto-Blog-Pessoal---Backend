package com.generations.blogpessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generations.blogpessoal.model.Usuario;
import com.generations.blogpessoal.model.UsuarioLogin;
import com.generations.blogpessoal.repository.UsuarioRepository;
import com.generations.blogpessoal.security.JwtService;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

    if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
      return Optional.empty();

    usuario.setSenha(criptografarSenha(usuario.getSenha()));

    return Optional.ofNullable(usuarioRepository.save(usuario));
  }

  private String criptografarSenha(String senha) {
    BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();
    return enconder.encode(senha);
  }

  public Optional<Usuario> atualizarUsuario(Usuario usuario) {
    Optional<Usuario> usuarioRepetido = usuarioRepository.findByUsuario(usuario.getUsuario());

    if (usuarioRepetido.isPresent() && !usuarioRepetido.get().getId().equals(usuario.getId())) {
      return Optional.empty();
    }

    Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
    if (usuarioExistente.isEmpty()) {
      return Optional.empty();
    }

    usuario.setSenha(criptografarSenha(usuario.getSenha()));
    return Optional.ofNullable(usuarioRepository.save(usuario));
  }

  public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

    var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(),
        usuarioLogin.get().getSenha());

    Authentication authentication = authenticationManager.authenticate(credenciais);

    if (authentication.isAuthenticated()) {
      Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

      if (usuario.isPresent()) {
        usuarioLogin.get().setId(usuario.get().getId());
        usuarioLogin.get().setNome(usuario.get().getNome());
        usuarioLogin.get().setFoto(usuario.get().getFoto());
        usuarioLogin.get().setSenha("");
        usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));

        return usuarioLogin;
      }
    }

    return Optional.empty();
  }

  private String gerarToken(String usuario) {
    return "Bearer " + jwtService.generateToken(usuario);
  }

}
