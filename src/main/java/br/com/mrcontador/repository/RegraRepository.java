package br.com.mrcontador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Regra;

/**
 * Spring Data  repository for the Regra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegraRepository extends JpaRepository<Regra, Long>, JpaSpecificationExecutor<Regra> {
	
	 @EntityGraph(attributePaths = {"parceiro"})
	Optional<Regra> findById(Long id);
}
