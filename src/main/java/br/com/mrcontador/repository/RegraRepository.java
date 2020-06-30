package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Regra;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Regra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegraRepository extends JpaRepository<Regra, Long>, JpaSpecificationExecutor<Regra> {
}
