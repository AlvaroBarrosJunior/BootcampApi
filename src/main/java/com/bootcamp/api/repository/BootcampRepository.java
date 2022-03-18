package com.bootcamp.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bootcamp.api.model.Bootcamp;

public interface BootcampRepository extends JpaRepository<Bootcamp, Long> {

	Optional<Bootcamp> findByNomeBootcamp(String nomeBootcamp);
}
