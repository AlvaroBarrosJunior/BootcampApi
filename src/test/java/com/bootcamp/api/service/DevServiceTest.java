package com.bootcamp.api.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bootcamp.api.dto.DevCadastroDTO;
import com.bootcamp.api.dto.DevLogadoDTO;
import com.bootcamp.api.dto.LoginDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Bootcamp;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Curso;
import com.bootcamp.api.model.Dev;
import com.bootcamp.api.model.Mentoria;
import com.bootcamp.api.repository.BootcampRepository;
import com.bootcamp.api.repository.ConteudoRepository;
import com.bootcamp.api.repository.DevRepository;
import com.bootcamp.api.utils.TokenUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@SpringBootTest
public class DevServiceTest {

	@Autowired
	private DevService devService;
	
	@MockBean
	private DevRepository devRepository;
	
	@MockBean
	private BootcampRepository bootcampRepository;
	
	@MockBean
	private ConteudoRepository conteudoRepository;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDcxNzgzOTYsInN1YiI6IjEiLCJleHAiOjE2NDcxODAxOTZ9.2xYsxLCq9BzKRfmVEo9y3_K2sRP-iJcN-KgntvdLwwE";

	private static final int TOKEN_EXPIRATION = 180000;
	
	@Test
	public void deveCadastrarDev() {
		DevCadastroDTO cadastro = new DevCadastroDTO("Teste da Silva", "teste123");
		Dev devCadastro = new Dev(null, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Dev dev = new Dev(1L, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		
		when(devRepository.saveAndFlush(devCadastro)).thenReturn(dev);
		when(devRepository.findByNomeDev(cadastro.getNomeDev())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(cadastro.getSenhaDev())).thenReturn(cadastro.getSenhaDev());
		
		try {
			Dev teste = devService.cadastrarDev(cadastro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveFalharCadastro() {
		DevCadastroDTO cadastro = new DevCadastroDTO("Teste da Silva", "teste123");
		Dev devCadastro = new Dev(null, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Dev dev = new Dev(1L, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Optional<Dev> devRetorno = Optional.of(dev);
		
		when(devRepository.saveAndFlush(devCadastro)).thenReturn(dev);
		when(devRepository.findByNomeDev(cadastro.getNomeDev())).thenReturn(devRetorno);
		when(passwordEncoder.encode(cadastro.getSenhaDev())).thenReturn(cadastro.getSenhaDev());
		
		try {
			Dev teste = devService.cadastrarDev(cadastro);
			
			Assertions.assertThrows(BootcampException.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveLogar() {
		LoginDTO login = new LoginDTO("Teste da Silva", "teste123");
		Dev dev = new Dev(1L, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Optional<Dev> devRetorno = Optional.of(dev);
		DevLogadoDTO devLogado = new DevLogadoDTO(1L, "Teste da Silva", TOKEN);
		
		when(devRepository.findByNomeDev(login.getNomeDev())).thenReturn(devRetorno);
		when(passwordEncoder.matches(dev.getSenhaDev(), login.getSenhaDev())).thenReturn(true);
		
		try (MockedStatic<TokenUtils> tokenUtils = mockStatic(TokenUtils.class)) {
			tokenUtils.when(()->TokenUtils.generateToken(dev)).thenReturn(TOKEN);
			
			DevLogadoDTO teste = devService.logarDev(login);
			
			Assertions.assertEquals(devLogado, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveFalharLoginUsuarioNaoEncontrado() {
		LoginDTO login = new LoginDTO("Teste da Silva", "teste123");
		
		when(devRepository.findByNomeDev(login.getNomeDev())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(login.getSenhaDev())).thenReturn(login.getSenhaDev());
		
		try (MockedStatic<TokenUtils> tokenUtils = mockStatic(TokenUtils.class)) {
			DevLogadoDTO teste = devService.logarDev(login);
			
			Assertions.assertThrows(BootcampException.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveFalharLoginSenhaInvalida() {
		LoginDTO login = new LoginDTO("Teste da Silva", "teste123");
		Dev dev = new Dev(1L, "Teste da Silva", "teste123",new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Optional<Dev> devRetorno = Optional.of(dev);
		
		when(devRepository.findByNomeDev(login.getNomeDev())).thenReturn(devRetorno);
		when(passwordEncoder.matches(dev.getSenhaDev(), login.getSenhaDev())).thenReturn(false);
		
		try (MockedStatic<TokenUtils> tokenUtils = mockStatic(TokenUtils.class)) {
			DevLogadoDTO teste = devService.logarDev(login);
			
			Assertions.assertThrows(BootcampException.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveValidarDev() {
		Dev dev = new Dev(1L, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Optional<Dev> devRetorno = Optional.of(dev);
		Claims claims = Jwts.claims();
		claims.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION));
		claims.setSubject("1");
		
		when(devRepository.findById(1L)).thenReturn(devRetorno);
		
		try (MockedStatic<TokenUtils> tokenUtils = mockStatic(TokenUtils.class)) {
			tokenUtils.when(()->TokenUtils.decodeToken(TOKEN)).thenReturn(claims);
			
			Dev teste = devService.validacao(TOKEN);
			
			Assertions.assertEquals(dev, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveFalharTokenExpirado() {
		Dev dev = new Dev(1L, "Teste da Silva", "teste123", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Optional<Dev> devRetorno = Optional.of(dev);
		Claims claims = Jwts.claims();
		claims.setExpiration(new Date(System.currentTimeMillis() - TOKEN_EXPIRATION));
		claims.setSubject("1");
		
		when(devRepository.findById(1L)).thenReturn(devRetorno);
		
		try (MockedStatic<TokenUtils> tokenUtils = mockStatic(TokenUtils.class)) {
			tokenUtils.when(()->TokenUtils.decodeToken(TOKEN)).thenReturn(claims);
			
			Dev teste = devService.validacao(TOKEN);
			
			Assertions.assertThrows(ExpiredJwtException.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void atualizarConteudos() {
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
//		
//		List<Conteudo> listaFinal = new ArrayList<>();
//		listaFinal.add(curso);
//		listaFinal.add(mentoria);
//		
//		Bootcamp bootcampFinal = new Bootcamp(1L, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, listaFinal);
//		List<Bootcamp> listaBootcamp = new ArrayList<>();
//		listaBootcamp.add(bootcampFinal);
//		Optional<Bootcamp> optionalBootcamp = Optional.of(bootcampFinal);
//		
//		Dev dev = new Dev(1L, "Teste da Silva", "teste123", lista, new ArrayList<>(), listaBootcamp, new ArrayList<>());
//		List<Dev> listaDev = new ArrayList<>();
//		listaDev.add(dev);
//		
//		Dev devFinal = new Dev(1L, "Teste da Silva", "teste123", listaFinal, new ArrayList<>(), listaBootcamp, new ArrayList<>());
//		
//		when(devRepository.findByIdBootcampsInscritos(1L)).thenReturn(listaDev);
//		when(bootcampRepository.findById(1L)).thenReturn(optionalBootcamp);
//		doNothing().when(devRepository).save(devFinal);
//		
//		try {
//			devService.atualizarConteudosInscritos(1L);
//			
//			Assertions.assertDoesNotThrow(() -> {});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void matricularDevEmBootcamp() {
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
		
		List<Conteudo> listaFinal = new ArrayList<>();
		listaFinal.add(curso);
		listaFinal.add(mentoria);
		
		Bootcamp bootcampFinal = new Bootcamp(1L, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, listaFinal);
		List<Bootcamp> listaBootcamp = new ArrayList<>();
		listaBootcamp.add(bootcampFinal);
		Optional<Bootcamp> optionalBootcamp = Optional.of(bootcampFinal);
		
		Dev dev = new Dev(1L, "Teste da Silva", "teste123", lista, new ArrayList<>(), listaBootcamp, new ArrayList<>());
		List<Dev> listaDev = new ArrayList<>();
		listaDev.add(dev);
		
		Dev devFinal = new Dev(1L, "Teste da Silva", "teste123", listaFinal, new ArrayList<>(), listaBootcamp, new ArrayList<>());
		Optional<Dev> optionalDev = Optional.of(dev);
		
		when(bootcampRepository.findById(1L)).thenReturn(optionalBootcamp);
		when(devRepository.findById(1L)).thenReturn(null).thenReturn(optionalDev);
		when(devRepository.saveAndFlush(devFinal)).thenReturn(devFinal);
		
		try {
			Dev teste = devService.matricularDev(1L, 1L); 
					
			Assertions.assertEquals(devFinal, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveProgredirDev() {
		Date data = new Date();
		Date fim = new Date(System.currentTimeMillis()+84600000);
		
		Curso curso = new Curso();
		curso.setIdConteudo(1L);
		curso.setTituloConteudo("Curso Java");
		curso.setDescricaoConteudo("Descricao curso java");
		curso.setChCurso(4);
		
		Optional<Conteudo> optionalCurso = Optional.of(curso);
		
		Mentoria mentoria = new Mentoria();
		mentoria.setIdConteudo(2L);
		mentoria.setTituloConteudo("Mentoria TDD Java");
		mentoria.setDescricaoConteudo("Descricao mentoria tdd java");
		mentoria.setDataMentoria(data);
		
		List<Conteudo> lista1 = new ArrayList<>();
		lista1.add(curso);
		
		List<Conteudo> lista2 = new ArrayList<>();
		lista2.add(mentoria);
		
		List<Conteudo> listaFinal = new ArrayList<>();
		listaFinal.add(curso);
		listaFinal.add(mentoria);
		
		Bootcamp bootcampFinal = new Bootcamp(1L, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, listaFinal);
		List<Bootcamp> listaBootcamp = new ArrayList<>();
		listaBootcamp.add(bootcampFinal);
		Optional<Bootcamp> optionalBootcamp = Optional.of(bootcampFinal);
		
		Dev dev = new Dev(1L, "Teste da Silva", "teste123", listaFinal, new ArrayList<>(), listaBootcamp, new ArrayList<>());
		List<Dev> listaDev = new ArrayList<>();
		listaDev.add(dev);
		
		Dev devFinal = new Dev(1L, "Teste da Silva", "teste123", lista1, lista2, listaBootcamp, new ArrayList<>());
		Optional<Dev> optionalDev = Optional.of(dev);
		
		when(conteudoRepository.findById(1L)).thenReturn(optionalCurso);
		when(devRepository.findById(1L)).thenReturn(optionalDev);
		when(devRepository.saveAndFlush(devFinal)).thenReturn(devFinal);
		
		try {
			Dev teste = devService.progredirDev(1L, 2L); 
					
			Assertions.assertEquals(devFinal, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
