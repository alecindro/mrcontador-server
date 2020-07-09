package br.com.mrcontador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Parceiro;

/**
 * Spring Data repository for the Parceiro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParceiroRepository extends JpaRepository<Parceiro, Long>, JpaSpecificationExecutor<Parceiro> {

	Optional<Parceiro> findByParCnpjcpf(String parCnpjcpf);

}
