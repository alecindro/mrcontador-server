package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Agenciabancaria;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Agenciabancaria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgenciabancariaRepository extends JpaRepository<Agenciabancaria, Long>, JpaSpecificationExecutor<Agenciabancaria> {
}
