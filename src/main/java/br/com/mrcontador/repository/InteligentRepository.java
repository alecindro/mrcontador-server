package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Inteligent;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Inteligent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InteligentRepository extends JpaRepository<Inteligent, Long>, JpaSpecificationExecutor<Inteligent> {
}
