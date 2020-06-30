package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Notaservico;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Notaservico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotaservicoRepository extends JpaRepository<Notaservico, Long>, JpaSpecificationExecutor<Notaservico> {
}
