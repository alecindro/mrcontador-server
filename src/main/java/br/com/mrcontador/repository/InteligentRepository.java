package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Inteligent;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Inteligent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InteligentRepository extends JpaRepository<Inteligent, Long>, JpaSpecificationExecutor<Inteligent> {
	
	@Procedure(procedureName = "processa_inteligente")
	int callInteligent(Long parceiroID, Long agenciaBancariaID, LocalDate begin, LocalDate end);

  @EntityGraph(attributePaths = "comprovante,notafiscal,notaservico,conta,extrato,parceiro,agenciabancaria")
	List<Inteligent> findAll();
	

}
