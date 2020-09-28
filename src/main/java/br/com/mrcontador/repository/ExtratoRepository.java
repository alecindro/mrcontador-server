package br.com.mrcontador.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Extrato;

/**
 * Spring Data  repository for the Extrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtratoRepository extends JpaRepository<Extrato, Long>, JpaSpecificationExecutor<Extrato> {
	
	@EntityGraph(attributePaths = {"arquivo"})
	Page<Extrato> findAll(@Nullable Specification<Extrato> spec, Pageable pageable);
	
	@Query("FROM Extrato AS ex LEFT JOIN FETCH ex.arquivo WHERE ex.id = ?1")
	Optional<Extrato> findById(Long id);
}
