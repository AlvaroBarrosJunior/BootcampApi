package com.bootcamp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.api.dto.DevCadastroDTO;
import com.bootcamp.api.dto.DevLogadoDTO;
import com.bootcamp.api.dto.LoginDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.exception.TokenException;
import com.bootcamp.api.model.Dev;
import com.bootcamp.api.repository.DevRepository;
import com.bootcamp.api.service.DevService;
import com.bootcamp.api.utils.ErrorHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;

@Api(tags = "Dev")
@RestController
@RequestMapping("dev")
public class DevController {

	@Autowired
	private DevService devService;
	
	@Autowired 
	private DevRepository devRepository;
	
	@ApiOperation(value = "Busca devs", notes = "Buscar todas os devs cadastradas no sistema")
	@GetMapping(value = "", produces = "application/json;charset=utf-8")
	public ResponseEntity<List<Dev>> buscarTodasOsDevs(){
		List<Dev> lista = devRepository.findAll();
		return ResponseEntity.ok(lista);
	}
	
	@ApiOperation(value = "Login", notes = "Logar no sistema")
	@PostMapping(value = "login", produces = "application/json;charset=utf-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logarDev(@RequestBody LoginDTO requisicao) throws BootcampException{
		
		try {
			DevLogadoDTO response = devService.logarDev(requisicao);
			return ResponseEntity.ok(response);			
		} catch (TokenException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorHandler(e.getMessage()));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorHandler(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorHandler(e.getMessage()));
		}
	}
	
	@ApiOperation(value = "Cadastrar dev", notes = "Cadastrar um novo dev")
	@PostMapping(value = "novo", produces = "application/json;charset=utf-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cadastrarDev(@RequestBody DevCadastroDTO requisicao) throws BootcampException{
		try {
			Dev dev = devService.cadastrarDev(requisicao);
			return ResponseEntity.status(HttpStatus.CREATED).body(dev);			
		} catch (BootcampException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorHandler(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorHandler(e.getMessage()));
		}
	}
	
	@ApiOperation(value = "Mtricular dev", notes = "Matricular dev em um bootcamp")
	@PutMapping(value = "matricular/{idBootcamp}/{idDev}", produces = "application/json;charset=utf-8")
	public ResponseEntity<?> matricularDev(@PathVariable Long idBootcamp, @PathVariable Long idDev,  @RequestHeader("Authorization") String token) throws BootcampException{
		try {
			Dev dev = devService.validacao(token);
			if (dev != null) {
				Dev devMatriculado = devService.matricularDev(idBootcamp, idDev);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(devMatriculado);			
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado ou usuário não autorizado, realize login novamente");
			}
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorHandler(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorHandler(e.getMessage()));
		}
	}
	
	@ApiOperation(value = "Progredir dev", notes = "Progredir dev concluindo um conteúdo em que o dev está inscrito!")
	@PutMapping(value = "progredir/{idDev}/{idConteudo}", produces = "application/json;charset=utf-8")
	public ResponseEntity<?> progredirDev(@PathVariable Long idDev, @PathVariable Long idConteudo,  @RequestHeader("Authorization") String token) throws BootcampException{
		try {
			Dev dev = devService.validacao(token);
			if (dev != null) {
				Dev devProgredido = devService.progredirDev(idDev, idConteudo);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(devProgredido);			
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado ou usuário não autorizado, realize login novamente");
			}
		} catch (BootcampException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorHandler(e.getMessage()));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorHandler(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorHandler(e.getMessage()));
		}
	}
	
}
