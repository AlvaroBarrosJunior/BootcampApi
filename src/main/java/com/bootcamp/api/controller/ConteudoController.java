package com.bootcamp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.api.dto.CursoCadastroDTO;
import com.bootcamp.api.dto.DevCadastroDTO;
import com.bootcamp.api.dto.MentoriaCadastroDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Curso;
import com.bootcamp.api.model.Dev;
import com.bootcamp.api.model.Mentoria;
import com.bootcamp.api.repository.ConteudoRepository;
import com.bootcamp.api.service.ConteudoService;
import com.bootcamp.api.utils.ErrorHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Conteudo")
@RestController
@RequestMapping("conteudo")
public class ConteudoController {
	
	@Autowired
	private ConteudoService conteudoService;
	
	@Autowired
	private ConteudoRepository conteudoRepository;
	
	@ApiOperation(value = "Busca conteudos", notes = "Buscar todos os conteudos cadastrados no sistema")
	@GetMapping(value = "", produces = "application/json;charset=utf-8")
	public ResponseEntity<List<Conteudo>> buscarTodasOsDevs(){
		List<Conteudo> lista = conteudoRepository.findAll();
		return ResponseEntity.ok(lista);
	}
	
	@ApiOperation(value = "Cadastrar curso", notes = "Cadastrar um novo curso")
	@PostMapping(value = "novo/curso", produces = "application/json;charset=utf-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cadastrarCurso(@RequestBody CursoCadastroDTO requisicao) throws BootcampException{
		try {
			Curso curso = conteudoService.cadastrarCurso(requisicao);
			return ResponseEntity.status(HttpStatus.CREATED).body(curso);			
		} catch (BootcampException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorHandler(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorHandler(e.getMessage()));
		}
	}
	
	@ApiOperation(value = "Cadastrar mentoria", notes = "Cadastrar uma nova mentoria")
	@PostMapping(value = "novo/mentoria", produces = "application/json;charset=utf-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cadastrarMentoria(@RequestBody MentoriaCadastroDTO requisicao) throws BootcampException{
		try {
			Mentoria mentoria = conteudoService.cadastrarMentoria(requisicao);
			return ResponseEntity.status(HttpStatus.CREATED).body(mentoria);			
		} catch (BootcampException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorHandler(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorHandler(e.getMessage()));
		}
	}

}
