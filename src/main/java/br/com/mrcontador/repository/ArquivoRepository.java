package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Arquivo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Arquivo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long>, JpaSpecificationExecutor<Arquivo> {
}
