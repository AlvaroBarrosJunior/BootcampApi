package com.bootcamp.api.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import com.bootcamp.api.dto.CursoCadastroDTO;
import com.bootcamp.api.dto.MentoriaCadastroDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Curso;
import com.bootcamp.api.model.Mentoria;
import com.bootcamp.api.repository.ConteudoRepository;
import com.bootcamp.api.service.ConteudoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;

@SpringBootTest
public class ConteudoControllerTest {
	
	@Autowired
	private ConteudoController conteudoController;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ConteudoRepository conteudoRepository;
	
	@MockBean
	private ConteudoService conteudoService;
	
	@BeforeEach
	public void setup() {
		standaloneSetup(conteudoController);
	}
	
	@Test
	public void deveRetornarTodosOsConteudos() throws JsonProcessingException {
		Date data = new Date();
		
		Curso curso = new Curso();
		curso.setIdConteudo(1L);
		curso.setTituloConteudo("Curso Java");
		curso.setDescricaoConteudo("Descricao curso java");
		curso.setChCurso(4);
		
		Mentoria mentoria = new Mentoria();
		mentoria.setIdConteudo(2L);
		mentoria.setTituloConteudo("Mentoria TDD Java");
		mentoria.setDescricaoConteudo("Descricao mentoria tdd java");
		mentoria.setDataMentoria(data);
		
		List<Conteudo> lista = new ArrayList<>();
		lista.add(curso);
		lista.add(mentoria);
		
		when(conteudoRepository.findAll()).thenReturn(lista);
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get("conteudo")
		.then()
			.statusCode(HttpStatus.OK.value())
			.contentType(ContentType.JSON)
			.body(is(equalTo(objectMapper.writeValueAsString(lista))));
	}
	
	@Test
	public void deveCadastrarCurso() throws BootcampException, JsonProcessingException {
		CursoCadastroDTO cadastro = new CursoCadastroDTO("Curso Java", "Descricao curso java", 4);
		
		Curso curso = new Curso();
		curso.setIdConteudo(1L);
		curso.setTituloConteudo("Curso Java");
		curso.setDescricaoConteudo("Descricao curso java");
		curso.setChCurso(4);
		
		when(conteudoService.cadastrarCurso(cadastro)).thenReturn(curso);
		
		given()
			.contentType(ContentType.JSON)
			.body(cadastro)
		.when()
			.post("/conteudo/novo/curso")
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.contentType(ContentType.JSON)
			.body(is(equalTo(objectMapper.writeValueAsString(curso))));

	}
	
	@Test
	public void deveCadastrarMentoria() throws BootcampException, JsonProcessingException {
		Date data = new Date();
		
		MentoriaCadastroDTO cadastro = new MentoriaCadastroDTO("Mentoria TDD Java", "Descricao mentoria tdd java", data);
		
		Mentoria mentoria = new Mentoria();
		mentoria.setIdConteudo(2L);
		mentoria.setTituloConteudo("Mentoria TDD Java");
		mentoria.setDescricaoConteudo("Descricao mentoria tdd java");
		mentoria.setDataMentoria(data);
		
		when(conteudoService.cadastrarMentoria(cadastro)).thenReturn(mentoria);
		
		given()
			.contentType(ContentType.JSON)
			.body(cadastro)
		.when()
			.post("/conteudo/novo/mentoria")
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.contentType(ContentType.JSON)
			.body(is(equalTo(objectMapper.writeValueAsString(mentoria))));

	}

}
