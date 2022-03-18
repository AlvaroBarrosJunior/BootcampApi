package com.bootcamp.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcamp.api.model.Mentoria;

@Transactional
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {

}
