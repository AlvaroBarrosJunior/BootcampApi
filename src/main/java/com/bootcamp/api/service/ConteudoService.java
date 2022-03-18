package com.bootcamp.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.api.dto.CursoCadastroDTO;
import com.bootcamp.api.dto.MentoriaCadastroDTO;
import com.bootcamp.api.exception.BootcampException;
import com.bootcamp.api.model.Conteudo;
import com.bootcamp.api.model.Curso;
import com.bootcamp.api.model.Mentoria;
import com.bootcamp.api.repository.ConteudoRepository;
import com.bootcamp.api.repository.CursoRepository;
import com.bootcamp.api.repository.MentoriaRepository;

@Service
public class ConteudoService {

	@Autowired 
	private ConteudoRepository conteudoRepository;
	
	@Autowired
	private MentoriaRepository mentoriaRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	public Curso cadastrarCurso(CursoCadastroDTO curso) throws BootcampException {
		Optional<Conteudo> conteudo = conteudoRepository.findByTituloConteudo(curso.getTituloConteudo());
		
		if (conteudo.isEmpty()) {
			Curso novoCurso = new Curso();
			novoCurso.setTituloConteudo(curso.getTituloConteudo());
			novoCurso.setDescricaoConteudo(curso.getDescricaoConteudo());
			novoCurso.setChCurso(curso.getChCurso());
			
			return cursoRepository.saveAndFlush(novoCurso);
		} else {
			throw new BootcampException("Título já cadastrado!");
		}
	}

	public Mentoria cadastrarMentoria(MentoriaCadastroDTO cadastro) throws BootcampException {
		Optional<Conteudo> conteudo = conteudoRepository.findByTituloConteudo(cadastro.getTituloConteudo());
		
		if (conteudo.isEmpty()) {
			Mentoria novaMentoria = new Mentoria();
			novaMentoria.setTituloConteudo(cadastro.getTituloConteudo());
			novaMentoria.setDescricaoConteudo(cadastro.getDescricaoConteudo());
			novaMentoria.setDataMentoria(cadastro.getDataMentoria());
			
			return mentoriaRepository.saveAndFlush(novaMentoria);
		} else {
			throw new BootcampException("Título já cadastrado!");
		}
	}
	
}
