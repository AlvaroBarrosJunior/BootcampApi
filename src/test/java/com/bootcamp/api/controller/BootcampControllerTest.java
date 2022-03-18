package com.bootcamp.api.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import com.bootcamp.api.dto.BootcampCadastroDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Bootcamp;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Curso;
import com.bootcamp.api.model.Mentoria;
import com.bootcamp.api.repository.BootcampRepository;
import com.bootcamp.api.service.BootcampService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;
import javassist.NotFoundException;

@SpringBootTest
public class BootcampControllerTest {

	@Autowired
	private BootcampController bootcampController;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private BootcampService bootcampService;
	
	@MockBean
	private BootcampRepository bootcampRepository;
	
	@BeforeEach
	public void setup() {
		standaloneSetup(bootcampController);
	}
	
//	@Test
//	public void deveRetornarTodosOsBootcamps() throws JsonProcessingException {
//		Date data = new Date();
//		Date fim = new Date(System.currentTimeMillis()+84600000);
//		
//		Curso curso = new Curso();
//		curso.setIdConteudo(1L);
//		curso.setTituloConteudo("Curso Java");
//		curso.setDescricaoConteudo("Descricao curso java");
//		curso.setChCurso(4);
//		
//		Optional<Conteudo> conteudoCurso = Optional.of(curso);
//		
//		Mentoria mentoria = new Mentoria();
//		mentoria.setIdConteudo(2L);
//		mentoria.setTituloConteudo("Mentoria TDD Java");
//		mentoria.setDescricaoConteudo("Descricao mentoria tdd java");
//		mentoria.setDataMentoria(data);
//		
//		Optional<Conteudo> conteudoMentoria = Optional.of(mentoria);
//		
//		List<Conteudo> lista = new ArrayList<>();
//		lista.add(curso);
//		
//		Bootcamp bootcamp1 = new Bootcamp(1L, "Titulo Bootcamp 1", "Uma breve descrição", data, fim, lista);
//		
//		List<Bootcamp> bootcamps = new ArrayList<>();
//		bootcamps.add(bootcamp1);
//		
//		lista.add(mentoria);
//		
//		Bootcamp bootcamp2 = new Bootcamp(2L, "Titulo Bootcamp 2", "Uma breve descrição", data, fim, lista);
//		
//		bootcamps.add(bootcamp2);
//		
//		when(bootcampRepository.findAll()).thenReturn(bootcamps);
//		
//		given()
//			.accept(ContentType.JSON)
//		.when()
//			.get("bootcamp")
//		.then()
//			.statusCode(HttpStatus.OK.value())
//			.contentType(ContentType.JSON)
//			.body(is(equalTo(objectMapper.writeValueAsString(bootcamps))));
//	}
	
//	@Test
//	public void deveCadastrarBootcamp() throws BootcampException, JsonProcessingException {
//		Date data = new Date();
//		Date fim = new Date(System.currentTimeMillis()+84600000);
//		
//		Curso curso = new Curso();
//		curso.setIdConteudo(1L);
//		curso.setTituloConteudo("Curso Java");
//		curso.setDescricaoConteudo("Descricao curso java");
//		curso.setChCurso(4);
//		
//		Mentoria mentoria = new Mentoria();
//		mentoria.setIdConteudo(2L);
//		mentoria.setTituloConteudo("Mentoria TDD Java");
//		mentoria.setDescricaoConteudo("Descricao mentoria tdd java");
//		mentoria.setDataMentoria(data);
//		
//		List<Conteudo> lista = new ArrayList<>();
//		lista.add(curso);
//		lista.add(mentoria);
//		
//		List<Long> ids = new ArrayList<>();
//		ids.add(1L);
//		ids.add(2L);
//		
//		BootcampCadastroDTO cadastro = new BootcampCadastroDTO("GFT Quality Assurance #1", "Uma breve descrição", 
//				data, fim, ids);
//		
//		Bootcamp bootcamp1 = new Bootcamp(1L, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, lista);
//		
//		when(bootcampService.cadastrarBootcamp(cadastro)).thenReturn(bootcamp1);
//		
//		given()
//			.contentType(ContentType.JSON)
//			.body(cadastro)
//		.when()
//			.post("/bootcamp/novo")
//		.then()
//			.statusCode(HttpStatus.CREATED.value())
//			.contentType(ContentType.JSON)
//			.body(is(equalTo(objectMapper.writeValueAsString(bootcamp1))));
//	}
	
	@Test
	public void deveAdicionarConteudo() throws NotFoundException, BootcampException, JsonProcessingException {
		Date data = new Date();
		Date fim = new Date(System.currentTimeMillis()+84600000);
		
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
		
		Bootcamp bootcamp1 = new Bootcamp(1L, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, lista);
		
		when(bootcampService.adicionarConteudo(1L, 2L)).thenReturn(bootcamp1);
		
		given()
			.contentType(ContentType.JSON)
			.body("")
		.when()
			.put("bootcamp/adicionar-conteudo/{idBootcamp}/{idConteudo}", 1L, 2L)
		.then()
			.statusCode(HttpStatus.ACCEPTED.value())
			.contentType(ContentType.JSON)
			.body(is(equalTo(objectMapper.writeValueAsString(bootcamp1))));
	}
}
