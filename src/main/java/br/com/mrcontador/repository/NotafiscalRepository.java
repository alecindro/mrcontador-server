package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Notafiscal;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Notafiscal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotafiscalRepository extends JpaRepository<Notafiscal, Long>, JpaSpecificationExecutor<Notafiscal> {
}
