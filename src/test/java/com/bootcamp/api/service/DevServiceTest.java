package com.bootcamp.api.service;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bootcamp.api.dto.DevCadastroDTO;
import com.bootcamp.api.dto.DevLogadoDTO;
import com.bootcamp.api.dto.LoginDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Dev;
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
	
	private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDcxNzgzOTYsInN1YiI6IjEiLCJleHAiOjE2NDcxODAxOTZ9.2xYsxLCq9BzKRfmVEo9y3_K2sRP-iJcN-KgntvdLwwE";

	private static final int TOKEN_EXPIRATION = 180000;
	
	@Test
	public void deveCadastrarDev() {
		DevCadastroDTO cadastro = new DevCadastroDTO("Teste da Silva", "teste123");
		Dev devCadastro = new Dev(null, "Teste da Silva", "teste123");
		Dev dev = new Dev(1L, "Teste da Silva", "teste123");
		
		when(devRepository.saveAndFlush(devCadastro)).thenReturn(dev);
		when(devRepository.findByNomeDev(cadastro.getNomeDev())).thenReturn(Optional.empty());
		
		try {
			Dev teste = devService.cadastrarDev(cadastro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveFalharCadastro() {
		DevCadastroDTO cadastro = new DevCadastroDTO("Teste da Silva", "teste123");
		Dev devCadastro = new Dev(null, "Teste da Silva", "teste123");
		Dev dev = new Dev(1L, "Teste da Silva", "teste123");
		Optional<Dev> devRetorno = Optional.of(dev);
		
		when(devRepository.saveAndFlush(devCadastro)).thenReturn(dev);
		when(devRepository.findByNomeDev(cadastro.getNomeDev())).thenReturn(devRetorno);
		
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
		Dev dev = new Dev(1L, "Teste da Silva", "teste123");
		Optional<Dev> devRetorno = Optional.of(dev);
		DevLogadoDTO devLogado = new DevLogadoDTO(1L, "Teste da Silva", TOKEN);
		
		when(devRepository.findByNomeDev(login.getNomeDev())).thenReturn(devRetorno);
		
		try (MockedStatic<TokenUtils> tokenUtils = mockStatic(TokenUtils.class)) {
			tokenUtils.when(()->TokenUtils.generateToken(dev)).thenReturn(TOKEN);
			
			DevLogadoDTO teste = devService.logarDev(login);
			
			Assertions.assertEquals(devLogado, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveFalharLogin() {
		LoginDTO login = new LoginDTO("Teste da Silva", "teste123");
		
		when(devRepository.findByNomeDev(login.getNomeDev())).thenReturn(Optional.empty());
		
		try (MockedStatic<TokenUtils> tokenUtils = mockStatic(TokenUtils.class)) {
			DevLogadoDTO teste = devService.logarDev(login);
			
			Assertions.assertThrows(BootcampException.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveValidarDev() {
		Dev dev = new Dev(1L, "Teste da Silva", "teste123");
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
		Dev dev = new Dev(1L, "Teste da Silva", "teste123");
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
}
