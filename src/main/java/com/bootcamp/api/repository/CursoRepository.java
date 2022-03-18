package com.bootcamp.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcamp.api.model.Curso;

@Transactional
public interface CursoRepository extends JpaRepository<Curso, Long> {

}
