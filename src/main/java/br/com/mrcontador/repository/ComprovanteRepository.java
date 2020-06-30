package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Comprovante;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Comprovante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Long>, JpaSpecificationExecutor<Comprovante> {
}
