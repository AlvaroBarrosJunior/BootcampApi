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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.api.dto.BootcampCadastroDTO;
import com.bootcamp.api.dto.CursoCadastroDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Bootcamp;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Curso;
import com.bootcamp.api.repository.BootcampRepository;
import com.bootcamp.api.service.BootcampService;
import com.bootcamp.api.utils.ErrorHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;

@Api(tags = "Bootcamp")
@RestController
@RequestMapping("bootcamp")
public class BootcampController {
	
	@Autowired
	private BootcampService bootcampService;
	
	@Autowired
	private BootcampRepository bootcampRepository;
	
	@ApiOperation(value = "Busca bootcamps", notes = "Buscar todos os bootcamps cadastrados no sistema")
	@GetMapping(value = "", produces = "application/json;charset=utf-8")
	public ResponseEntity<List<Bootcamp>> buscarTodasOsDevs(){
		List<Bootcamp> lista = bootcampRepository.findAll();
		return ResponseEntity.ok(lista);
	}
	
	@ApiOperation(value = "Cadastrar bootcamp", notes = "Cadastrar um novo bootcamp")
	@PostMapping(value = "novo", produces = "application/json;charset=utf-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cadastrarBootcamp(@RequestBody BootcampCadastroDTO requisicao) throws BootcampException{
		try {
			Bootcamp bootcamp = bootcampService.cadastrarBootcamp(requisicao);
			return ResponseEntity.status(HttpStatus.CREATED).body(bootcamp);			
		} catch (BootcampException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorHandler(e.getMessage()));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorHandler(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorHandler(e.getMessage()));
		}
	}
	
	@ApiOperation(value = "Cadastrar bootcamp", notes = "Cadastrar um novo bootcamp")
	@PutMapping(value = "adicionar-conteudo/{idBootcamp}/{idConteudo}", produces = "application/json;charset=utf-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> adicionarConteudo(@PathVariable Long idBootcamp, @PathVariable Long idConteudo) throws BootcampException{
		try {
			Bootcamp bootcamp = bootcampService.adicionarConteudo(idBootcamp, idConteudo);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(bootcamp);			
		} catch (BootcampException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorHandler(e.getMessage()));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorHandler(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorHandler(e.getMessage()));
		}
	}

}
