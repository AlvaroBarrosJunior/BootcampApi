package com.bootcamp.api.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bootcamp.api.model.Dev;

@Transactional
public interface DevRepository extends JpaRepository<Dev, Long> {

	Optional<Dev> findByNomeDev(String nomeDev);
	
	@Query(value = "SELECT d.* from bootcamp.tb001_dev d \n"
			+ "JOIN bootcamp.tb009_bootcamps_inscritos bi ON d.id_dev = bi.id_dev \n"
			+ "WHERE bi.id_bootcamp = ?1", nativeQuery = true)
	List<Dev> findByIdBootcampsInscritos(Long idBootcamp);
	
	@Query(value = "SELECT d.* from bootcamp.tb001_dev d \n"
			+ "JOIN bootcamp.tb009_bootcamps_concluidos bc ON d.id_dev = bc.id_dev \n"
			+ "WHERE bc.id_bootcamp = ?1", nativeQuery = true)
	List<Dev> findByIdBootcampsConcluidos(Long idBootcamp);
}
