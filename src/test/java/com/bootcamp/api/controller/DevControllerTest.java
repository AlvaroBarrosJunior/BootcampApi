package com.bootcamp.api.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import com.bootcamp.api.dto.DevCadastroDTO;
import com.bootcamp.api.dto.DevLogadoDTO;
import com.bootcamp.api.dto.LoginDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.exception.TokenException;
import com.bootcamp.api.model.Dev;
import com.bootcamp.api.repository.DevRepository;
import com.bootcamp.api.service.DevService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;
import javassist.NotFoundException;

@SpringBootTest
public class DevControllerTest {

	private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwibm9tZUNsaWVudGUiOiJUZXN0ZSBkYSBTaWx2YSIsInNlbmhhQ2xpZW50ZSI6IlRlc3RlMTIzIn0.hIun_jeWewRztx2lWSkAe3Xbxt_YVkiHyrtgcpAUkmc";

	@Autowired
	private DevController devController;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private DevService devService;
	
	@MockBean
	private DevRepository devRepository;
	
	@BeforeEach
	public void setup() {
		standaloneSetup(devController);
	}
	
	@Test
	public void deveRetornarTodosOsDevs() throws JsonProcessingException {
		Dev dev1 = new Dev(1L, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Dev dev2 = new Dev(2L, "Maria Testeira", "teste456", new ArrayList<>(), new ArrayList<>(),new ArrayList<>(), new ArrayList<>());
		
		List<Dev> listaDev = new ArrayList<>();
		listaDev.add(dev1);
		listaDev.add(dev2);
		
		when(devRepository.findAll()).thenReturn(listaDev);
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get("dev")
		.then()
			.statusCode(HttpStatus.OK.value())
			.contentType(ContentType.JSON)
			.body(is(equalTo(objectMapper.writeValueAsString(listaDev))));
	}
	
	@Test
	public void deveLogarDev() throws BootcampException, JsonProcessingException, TokenException, NotFoundException {
		LoginDTO login = new LoginDTO("Teste da Silva", "teste123");
		DevLogadoDTO devLogado = new DevLogadoDTO(1L, "Teste da Silva", TOKEN);
		
		when(devService.logarDev(login)).thenReturn(devLogado);
		
		given()
			.contentType(ContentType.JSON)
			.body(login)
		.when()
			.post("/dev/login")
		.then()
			.statusCode(HttpStatus.OK.value())
			.contentType(ContentType.JSON)
			.body(is(equalTo(objectMapper.writeValueAsString(devLogado))));
	}
	
	@Test
	public void deveCriarDev() throws BootcampException, JsonProcessingException {
		
		DevCadastroDTO cadastro = new DevCadastroDTO("Teste da Silva", "teste123");
		Dev dev = new Dev(1L, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		
		when(devService.cadastrarDev(cadastro)).thenReturn(dev);
		
		given()
			.contentType(ContentType.JSON)
			.body(cadastro)
		.when()
			.post("/dev/novo")
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.contentType(ContentType.JSON)
			.body(is(equalTo(objectMapper.writeValueAsString(dev))));

	}
}
