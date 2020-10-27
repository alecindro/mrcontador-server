package br.com.mrcontador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.domain.TipoAgencia;

/**
 * Spring Data  repository for the Agenciabancaria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgenciabancariaRepository extends JpaRepository<Agenciabancaria, Long>, JpaSpecificationExecutor<Agenciabancaria> {
	
	Optional<Agenciabancaria> findByParceiroAndTipoAgencia(Parceiro parceiro, TipoAgencia tipoAgencia);
}
