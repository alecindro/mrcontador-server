package br.com.mrcontador.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Notafiscal;

/**
 * Spring Data  repository for the Notafiscal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotafiscalRepository extends JpaRepository<Notafiscal, Long>, JpaSpecificationExecutor<Notafiscal> {

	Page<Notafiscal> findAll(@Nullable Specification<Notafiscal> spec, Pageable pageable);
	
	@Query("FROM Notafiscal AS nf LEFT JOIN FETCH nf.arquivoPDF WHERE nf.id = ?1")
	Optional<Notafiscal> findById(Long id);
}
