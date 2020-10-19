package br.com.mrcontador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Contador;

/**
 * Spring Data  repository for the Contador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContadorRepository extends JpaRepository<Contador, Long>, JpaSpecificationExecutor<Contador> {
	
	Optional<Contador> findOneByCnpjOrCrc(String cnpj, String crc);
	Optional<Contador> findOneByDatasource(String datasource);
}
