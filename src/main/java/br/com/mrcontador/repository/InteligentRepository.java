package br.com.mrcontador.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Inteligent;

/**
 * Spring Data  repository for the Inteligent entity.
 */

@Repository
public interface InteligentRepository extends JpaRepository<Inteligent, Long>, JpaSpecificationExecutor<Inteligent> {
	
	@Query(nativeQuery = true, value = "select processa_inteligente(?,?,?,?)")
	int callInteligent(Long parceiroID,Long agenciaBancariaID,Date begin,Date end);

  @EntityGraph(attributePaths = "comprovante,notafiscal,notaservico,conta,extrato,parceiro,agenciabancaria")
	List<Inteligent> findAll();
	

}
