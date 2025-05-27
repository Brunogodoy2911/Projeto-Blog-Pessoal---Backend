package com.generations.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generations.blogpessoal.model.Usuario;
import com.generations.blogpessoal.model.UsuarioLogin;
import com.generations.blogpessoal.repository.UsuarioRepository;
import com.generations.blogpessoal.service.UsuarioService;
import com.generations.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String USUARIO_ROOT_EMAIL = "root@email.com";
	private static final String USUARIO_ROOT_SENHA = "rootroot";
	private static final String BASE_URL_USUARIOS = "/usuarios";

	@BeforeAll
	void start(){
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuarioRoot());
	}
	
	@Test
	@DisplayName("✔ 01 - Deve cadastrar um novo usuário com sucesso")
	public void deveCadastrarUsuario() {
		
		// Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278");

		// When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL_USUARIOS + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		// Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals("Paulo Antunes", resposta.getBody().getNome());
		assertEquals("paulo_antunes@email.com.br", resposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("❌ 02 - Não deve permitir duplicação do usuário")
	public void naoDeveDuplicarUsuario() {
		
		//Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Maria da Silva", "maria_silva@email.com.br", "13465278");
		usuarioService.cadastrarUsuario(usuario);

		//When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL_USUARIOS + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		//Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	@Test
	@DisplayName("✔ 03 - Deve atualizar um usuário existente")
	public void deveAtualizarUmUsuario() {
		
		//Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Juliana Andrews", "juliana_andrews@email.com.br", "juliana123");
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);
		
		Usuario usuarioUpdate = TestBuilder.criarUsuario(usuarioCadastrado.get().getId(), "Juliana Ramos", 
				"juliana_ramos@email.com.br", "juliana123");

		//When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("Juliana Ramos", resposta.getBody().getNome());
		assertEquals("juliana_ramos@email.com.br", resposta.getBody().getUsuario());
	}

	@Test
	@DisplayName("✔ 04 - Deve listar todos os usuários")
	public void deveListarTodosUsuarios() {
		
		//Given
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Ana Clara", "ana@email.com", "senha123"));
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Carlos Souza", "carlos@email.com", "senha123"));

		//When
		ResponseEntity<Usuario[]> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/all", HttpMethod.GET, null, Usuario[].class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 05 - Deve listar um usuário específico - pelo id")
	public void deveListarUmUsuarioPorId() {
		
		//Given
		Optional<Usuario> usuario = usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Ana Paula", "ana_paula@email.com", "senha123"));
		var id = usuario.get().getId();
		
		//When
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/" + id, HttpMethod.GET, null, Usuario.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 06 - Deve Autenticar um usuário com sucesso")
	public void deveAutenticarUsuario() {
		
		//Given
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Márcia Marques", "marcia_marques@email.com.br", "13465278"));
		UsuarioLogin usuarioLogin = TestBuilder.criarUsuarioLogin("marcia_marques@email.com.br", "13465278");

		//When
		HttpEntity<UsuarioLogin> requisicao = new HttpEntity<>(usuarioLogin);

		ResponseEntity<UsuarioLogin> resposta = testRestTemplate.exchange(
				BASE_URL_USUARIOS + "/logar", HttpMethod.POST, requisicao, UsuarioLogin.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("marcia_marques@email.com.br", resposta.getBody().getUsuario());
	}
	
}