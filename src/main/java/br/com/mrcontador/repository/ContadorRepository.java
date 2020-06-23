package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Contador;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Contador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContadorRepository extends JpaRepository<Contador, Long>, JpaSpecificationExecutor<Contador> {
}
