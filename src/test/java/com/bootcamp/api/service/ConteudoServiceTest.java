package com.bootcamp.api.service;

import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bootcamp.api.dto.CursoCadastroDTO;
import com.bootcamp.api.dto.MentoriaCadastroDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Curso;
import com.bootcamp.api.model.Mentoria;
import com.bootcamp.api.repository.ConteudoRepository;
import com.bootcamp.api.repository.CursoRepository;
import com.bootcamp.api.repository.MentoriaRepository;

@SpringBootTest
public class ConteudoServiceTest {
	
	@Autowired
	private ConteudoService conteudoService;
	
	@MockBean
	private ConteudoRepository conteudoRepository;
	
	@MockBean
	private MentoriaRepository mentoriaRepository;
	
	@MockBean
	private CursoRepository cursoRepository;
	
	@Test
	public void deveCadastrarCurso() {
		CursoCadastroDTO cadastro = new CursoCadastroDTO("Curso Java", "Descricao curso java", 4);
		
		Curso curso = new Curso();
		curso.setTituloConteudo("Curso Java");
		curso.setDescricaoConteudo("Descricao curso java");
		curso.setChCurso(4);
		
		Curso cursoCadastrado = curso;
		cursoCadastrado.setIdConteudo(1L);
		
		when(conteudoRepository.findByTituloConteudo(curso.getTituloConteudo())).thenReturn(Optional.empty());
		when(cursoRepository.saveAndFlush(curso)).thenReturn(cursoCadastrado);
		
		try {
			Curso teste = conteudoService.cadastrarCurso(cadastro);
			
			Assertions.assertEquals(cursoCadastrado, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveFalharCadastroCurso() {
		CursoCadastroDTO cadastro = new CursoCadastroDTO("Curso Java", "Descricao curso java", 4);
		
		Curso curso = new Curso();
		curso.setTituloConteudo("Curso Java");
		curso.setDescricaoConteudo("Descricao curso java");
		curso.setChCurso(4);
		
		Optional<Conteudo> conteudo = Optional.of(curso);
		
		Curso cursoCadastrado = curso;
		cursoCadastrado.setIdConteudo(1L);
		
		when(conteudoRepository.findByTituloConteudo(curso.getTituloConteudo())).thenReturn(conteudo);
		when(cursoRepository.saveAndFlush(curso)).thenReturn(cursoCadastrado);
		
		try {
			Curso teste = conteudoService.cadastrarCurso(cadastro);
			
			Assertions.assertThrows(BootcampException.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveCadastrarMentoria() {
		Date data = new Date();
		
		MentoriaCadastroDTO cadastro = new MentoriaCadastroDTO("Mentoria TDD Java", "Descricao mentoria tdd java", data);
		
		Mentoria mentoria = new Mentoria();
		mentoria.setTituloConteudo("Mentoria TDD Java");
		mentoria.setDescricaoConteudo("Descricao mentoria tdd java");
		mentoria.setDataMentoria(data);
		
		Mentoria mentoriaCadastrada = mentoria;
		mentoriaCadastrada.setIdConteudo(1L);
		
		when(conteudoRepository.findByTituloConteudo(mentoria.getTituloConteudo())).thenReturn(Optional.empty());
		when(mentoriaRepository.saveAndFlush(mentoria)).thenReturn(mentoriaCadastrada);
		
		try {
			Mentoria teste = conteudoService.cadastrarMentoria(cadastro);
			
			Assertions.assertEquals(mentoriaCadastrada, teste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deveFalharCadastroMentoria() {
		
		Date data = new Date();
		
		MentoriaCadastroDTO cadastro = new MentoriaCadastroDTO("Mentoria TDD Java", "Descricao mentoria tdd java", data);
		
		Mentoria mentoria = new Mentoria();
		mentoria.setTituloConteudo("Mentoria TDD Java");
		mentoria.setDescricaoConteudo("Descricao mentoria tdd java");
		mentoria.setDataMentoria(data);
		
		Optional<Conteudo> conteudo = Optional.of(mentoria);

		Mentoria mentoriaCadastrada = mentoria;
		mentoriaCadastrada.setIdConteudo(1L);
		
		when(conteudoRepository.findByTituloConteudo(mentoria.getTituloConteudo())).thenReturn(conteudo);
		when(mentoriaRepository.saveAndFlush(mentoria)).thenReturn(mentoriaCadastrada);
		
		try {
			Mentoria teste = conteudoService.cadastrarMentoria(cadastro);
			
			Assertions.assertThrows(BootcampException.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
