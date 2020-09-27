package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Comprovante;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Comprovante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Long>, JpaSpecificationExecutor<Comprovante> {
	
	@EntityGraph(attributePaths = {"arquivo"})
	Page<Comprovante> findAll(@Nullable Specification<Comprovante> spec, Pageable pageable);
	
	@EntityGraph(attributePaths = {"arquivo"})
	Optional<Comprovante> findById(Long id);
}
