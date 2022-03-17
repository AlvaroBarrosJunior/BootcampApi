package com.bootcamp.api.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcamp.api.model.Dev;

@Transactional
public interface DevRepository extends JpaRepository<Dev, Long> {

	Optional<Dev> findByNomeDev(String nomeDev);
}
