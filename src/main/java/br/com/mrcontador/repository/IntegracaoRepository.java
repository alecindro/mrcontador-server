package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Integracao;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Integracao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegracaoRepository extends JpaRepository<Integracao, Long>, JpaSpecificationExecutor<Integracao> {
}
