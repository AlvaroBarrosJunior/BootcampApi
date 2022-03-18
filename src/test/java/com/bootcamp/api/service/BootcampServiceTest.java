package com.bootcamp.api.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bootcamp.api.dto.BootcampCadastroDTO;
import com.bootcamp.api.model.Bootcamp;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Curso;
import com.bootcamp.api.model.Mentoria;
import com.bootcamp.api.repository.BootcampRepository;
import com.bootcamp.api.repository.ConteudoRepository;

@SpringBootTest
public class BootcampServiceTest {

	@Autowired
	private BootcampService bootcampService;
	
	@MockBean
	private BootcampRepository bootcampRepository;
	
	@MockBean
	private ConteudoRepository conteudoRepository;
	
	@MockBean
	private DevService devService;
	
	@Test
	public void deveCadastrarBootcamp() {
		Date data = new Date();
		Date fim = new Date(System.currentTimeMillis()+84600000);
		
		Curso curso = new Curso();
		curso.setIdConteudo(1L);
		curso.setTituloConteudo("Curso Java");
		curso.setDescricaoConteudo("Descricao curso java");
		curso.setChCurso(4);
		
		Optional<Conteudo> conteudoCurso = Optional.of(curso);
		
		Mentoria mentoria = new Mentoria();
		mentoria.setIdConteudo(2L);
		mentoria.setTituloConteudo("Mentoria TDD Java");
		mentoria.setDescricaoConteudo("Descricao mentoria tdd java");
		mentoria.setDataMentoria(data);
		
		Optional<Conteudo> conteudoMentoria = Optional.of(mentoria);
		
		List<Conteudo> lista = new ArrayList<>();
		lista.add(curso);
		lista.add(mentoria);
		
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);
		
		BootcampCadastroDTO cadastro = new BootcampCadastroDTO("GFT Quality Assurance #1", "Uma breve descrição", 
				data, fim, ids);
		
		Bootcamp bootcamp = new Bootcamp(null, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, lista);
		Bootcamp bootcampCadastrado = new Bootcamp(1L, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, lista);
		
		when(bootcampRepository.findByNomeBootcamp(cadastro.getNomeBootcamp())).thenReturn(Optional.empty());
		when(bootcampRepository.saveAndFlush(bootcamp)).thenReturn(bootcampCadastrado);
		when(conteudoRepository.findById(1L)).thenReturn(conteudoCurso);
		when(conteudoRepository.findById(2L)).thenReturn(conteudoMentoria);
		
		try {
			Bootcamp teste = bootcampService.cadastrarBootcamp(cadastro);
			
			Assertions.assertEquals(bootcampCadastrado, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveAdicionarNovoConteudo() {
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
		
		Optional<Conteudo> optionalMentoria = Optional.of(mentoria);
		
		List<Conteudo> lista = new ArrayList<>();
		lista.add(curso);
		
		List<Conteudo> listaFinal = new ArrayList<>();
		listaFinal.add(curso);
		listaFinal.add(mentoria);
		
		Bootcamp bootcamp = new Bootcamp(1L, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, lista);
		
		Optional<Bootcamp> optionalBootcamp = Optional.of(bootcamp);
		
		Bootcamp bootcampFinal = new Bootcamp(1L, "GFT Quality Assurance #1", "Uma breve descrição", data, fim, listaFinal);
		
		when(conteudoRepository.findById(2L)).thenReturn(optionalMentoria);
		when(bootcampRepository.findById(1L)).thenReturn(optionalBootcamp);
		doNothing().when(devService).atualizarConteudosInscritos(1L);
		
		try {
			Bootcamp teste = bootcampService.adicionarConteudo(1L, 2L);
			
			Assertions.assertEquals(bootcampFinal, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
