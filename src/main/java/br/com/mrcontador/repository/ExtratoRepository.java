package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Extrato;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Extrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtratoRepository extends JpaRepository<Extrato, Long>, JpaSpecificationExecutor<Extrato> {
}
