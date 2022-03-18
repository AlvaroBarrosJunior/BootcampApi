package com.bootcamp.api.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcamp.api.model.Conteudo;

@Transactional
public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {
	
	Optional<Conteudo> findByTituloConteudo(String tituloConteudo);
	
}
