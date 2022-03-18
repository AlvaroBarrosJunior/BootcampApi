package com.bootcamp.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bootcamp.api.dto.DevCadastroDTO;
import com.bootcamp.api.dto.DevLogadoDTO;
import com.bootcamp.api.dto.LoginDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.exception.TokenException;
import com.bootcamp.api.model.Bootcamp;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Dev;
import com.bootcamp.api.repository.BootcampRepository;
import com.bootcamp.api.repository.ConteudoRepository;
import com.bootcamp.api.repository.DevRepository;
import com.bootcamp.api.utils.TokenUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import javassist.NotFoundException;

@Service
public class DevService {

	@Autowired
	private DevRepository devRepository;
	
	@Autowired
	private BootcampRepository bootcampRepository;
	
	@Autowired
	private ConteudoRepository conteudoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Dev cadastrarDev(DevCadastroDTO requisicao) throws BootcampException {
		Optional<Dev> dev = devRepository.findByNomeDev(requisicao.getNomeDev());
		
		if (dev.isEmpty()) {
			Dev novoDev = new Dev();
			novoDev.setNomeDev(requisicao.getNomeDev());
			novoDev.setSenhaDev(passwordEncoder.encode(requisicao.getSenhaDev()));
			
			return devRepository.saveAndFlush(novoDev);
		} else {
			throw new BootcampException("Registro já existente, tente outro nome");
		}
	}
	
	public DevLogadoDTO logarDev(LoginDTO login) throws TokenException, NotFoundException {
		Optional<Dev> dev = devRepository.findByNomeDev(login.getNomeDev());
		
		if (dev.isPresent()) {
			String token = TokenUtils.generateToken(dev.get());
			
			if (passwordEncoder.matches(login.getSenhaDev(), dev.get().getSenhaDev())) {				
				return new DevLogadoDTO(dev.get().getIdDev(), dev.get().getNomeDev(), token);
			} else {
				throw new TokenException("Senha inválida!");
			}
		} else {
			throw new NotFoundException("Dev não encontrado, verifique as informações fornecidas!");
		}
	}

	public Dev validacao(String token) throws TokenException {
		try {
			Claims claims = TokenUtils.decodeToken(token);
			
			if(claims.getExpiration().before(new Date()))
				throw new TokenException("Token Expirado!");
			
			return devRepository.findById(Long.parseLong(claims.getSubject())).get();
			
		} catch (TokenException e) {
			e.printStackTrace();
			throw e;
		} catch (ExpiredJwtException e) {
			throw new TokenException("Token Expirado!");
		} catch (Exception e) {
			throw new TokenException("Token Inválido!");
		}
	}
	
	public void atualizarConteudosInscritos(Long idBootcamp) {
		List<Dev> listaDevs = devRepository.findByIdBootcampsInscritos(idBootcamp);
		Bootcamp bootcamp = bootcampRepository.findById(idBootcamp).get();
		
		for (Dev dev : listaDevs) {
			dev.setConteudosInscritos(listaConteudosInscritos(bootcamp, dev));
			devRepository.save(dev);
		}
	}
	
	public List<Conteudo> listaConteudosInscritos(Bootcamp bootcamp, Dev dev){
		List<Conteudo> lista = dev.getConteudosInscritos();
		for (Conteudo c : bootcamp.getConteudosInscritos()) {
			if (!dev.getConteudosInscritos().contains(c) && !dev.getConteudosConcluidos().contains(c)) {
				lista.add(c);
			}
		}
		
		return lista;
	}
	
	public Dev matricularDev(Long idBootcamp, Long idDev) throws NotFoundException {
		Optional<Bootcamp> optionalBootcamp = bootcampRepository.findById(idBootcamp);
		
		if (optionalBootcamp.isPresent()) {
			
			Bootcamp bootcamp = optionalBootcamp.get();
			Optional<Dev> optionalDev = devRepository.findById(idDev);
			
			if (optionalDev.isPresent()) {
				
				Dev dev = optionalDev.get();
				List<Conteudo> conteudosConcluidos = dev.getConteudosConcluidos();
				
				if (conteudosConcluidos.containsAll(bootcamp.getConteudosInscritos())) {
					List<Bootcamp> bootcampsConcluidos = dev.getBootcampsConcluidos();
					
					bootcampsConcluidos.add(bootcamp);
					
					dev.setBootcampsConcluidos(bootcampsConcluidos);
				} else {
					List<Bootcamp> bootcampsInscritos = dev.getBootcampsInscritos();
					bootcampsInscritos.add(bootcamp);
					
					dev.setConteudosInscritos(listaConteudosInscritos(bootcamp, dev));
					dev.setBootcampsInscritos(bootcampsInscritos);
				}
				
				devRepository.save(dev);
				
				return dev;
			} else {
				throw new NotFoundException("Dev ["+idDev+"] não encontrado!");
			}
		} else {
			throw new NotFoundException("Bootcamp ["+idBootcamp+"] não encontrado!");
		}
	}

	public Dev progredirDev(long idDev, long idConteudo) throws NotFoundException, BootcampException {
		Optional<Dev> optionalDev = devRepository.findById(idDev);
		
		if (optionalDev.isPresent()) {
			Dev dev = optionalDev.get();
			
			Optional<Conteudo> optionalConteudo = conteudoRepository.findById(idConteudo);
			
			if (optionalConteudo.isPresent()) {
				Conteudo conteudo = optionalConteudo.get();
				
				if (dev.getConteudosInscritos().contains(conteudo)) {
					List<Conteudo> conteudosInscritos = dev.getConteudosInscritos();
					List<Conteudo> conteudosConcluidos = dev.getConteudosConcluidos();
					
					conteudosInscritos.remove(conteudo);
					conteudosConcluidos.add(conteudo);
					
					dev.setConteudosInscritos(conteudosInscritos);
					dev.setConteudosConcluidos(conteudosConcluidos);
					
					List<Bootcamp> bootcampsConcluidos = dev.getBootcampsConcluidos();
					List<Bootcamp> bootcampsInscritos = new ArrayList<>();
					
					for (Bootcamp b : dev.getBootcampsInscritos()) {
						bootcampsInscritos.add(b);
					}
					
					for (Bootcamp b : dev.getBootcampsInscritos()) {
						if(dev.getConteudosConcluidos().containsAll(b.getConteudosInscritos())) {
							bootcampsInscritos.remove(b);
							bootcampsConcluidos.add(b);
						}
					}
					
					dev.setBootcampsInscritos(bootcampsInscritos);
					dev.setBootcampsConcluidos(bootcampsConcluidos);
					
					return devRepository.saveAndFlush(dev);
				} else {
					throw new BootcampException("Conteúdo não inscrito para o Dev!");
				}
			} else {
				throw new NotFoundException("Conteudo ["+idConteudo+"] não encontrado!");
			}
		} else {
			throw new NotFoundException("Dev ["+idDev+"] não encontrado!");
		}
	}
	
}
