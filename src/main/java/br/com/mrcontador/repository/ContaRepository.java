package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Conta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Conta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>, JpaSpecificationExecutor<Conta> {
}
