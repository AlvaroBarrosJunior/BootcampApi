package com.bootcamp.api.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.api.dto.DevCadastroDTO;
import com.bootcamp.api.dto.DevLogadoDTO;
import com.bootcamp.api.dto.LoginDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Dev;
import com.bootcamp.api.repository.DevRepository;
import com.bootcamp.api.utils.TokenUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@Service
public class DevService {

	@Autowired
	private DevRepository devRepository;
	
	public Dev cadastrarDev(DevCadastroDTO requisicao) throws BootcampException {
		Optional<Dev> dev = devRepository.findByNomeDev(requisicao.getNomeDev());
		
		if (dev.isEmpty()) {
			Dev novoDev = new Dev();
			novoDev.setNomeDev(requisicao.getNomeDev());
			novoDev.setSenhaDev(requisicao.getSenhaDev());
			
			return devRepository.saveAndFlush(novoDev);
		} else {
			throw new BootcampException("Registro já existente, tente outro nome");
		}
	}
	
	public DevLogadoDTO logarDev(LoginDTO login) throws BootcampException {
		Optional<Dev> dev = devRepository.findByNomeDev(login.getNomeDev());
		
		if (dev.isPresent()) {
			String token = TokenUtils.generateToken(dev.get());
			
			return new DevLogadoDTO(dev.get().getIdDev(), dev.get().getNomeDev(), token);
		} else {
			throw new BootcampException("Dev não encontrado, verifique as informações fornecidas!");
		}
	}

	public Dev validacao(String token) throws BootcampException {
		try {
			Claims claims = TokenUtils.decodeToken(token);
			
			if(claims.getExpiration().before(new Date()))
				throw new BootcampException("Token Expirado!");
			
			return devRepository.findById(Long.parseLong(claims.getSubject())).get();
			
		} catch (BootcampException e) {
			e.printStackTrace();
			throw e;
		} catch (ExpiredJwtException e) {
			throw new BootcampException("Token Expirado!");
		} catch (Exception e) {
			throw new BootcampException("Token Inválido!");
		}
	}
}
