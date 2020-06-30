package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Parceiro;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Parceiro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParceiroRepository extends JpaRepository<Parceiro, Long>, JpaSpecificationExecutor<Parceiro> {
}
