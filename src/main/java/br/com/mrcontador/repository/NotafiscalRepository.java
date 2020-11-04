package br.com.mrcontador.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Notafiscal;

/**
 * Spring Data  repository for the Notafiscal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotafiscalRepository extends JpaRepository<Notafiscal, Long>, JpaSpecificationExecutor<Notafiscal> {

	Page<Notafiscal> findAll(@Nullable Specification<Notafiscal> spec, Pageable pageable);
	
	@Query("FROM Notafiscal AS nf LEFT JOIN FETCH nf.arquivoPDF WHERE nf.id = ?1")
	Optional<Notafiscal> findById(Long id);
	
	@Query(nativeQuery = true, value = "select * from processa_notafiscal(?)")
	int callProcessaNotafiscal(Long notaId);
	
	@Query(nativeQuery = true, value = "select * from processa_notafiscalgeral(?)")
	int callProcessaAllNotafiscal(Long parceiroId);
	
	  @Modifying(flushAutomatically = true)
	  @Query(value="update notafiscal set processado = true where id = :notaId",nativeQuery = true)
	  void processadoTrue(@Param("notaId") Long id);
	  
	  @Query(value="select * from notafiscal n where  substring(not_cnpj,1,8) = substring(:cnpj,1,8) and  NOT_VALORPARCELA between :valorInicial and :valorFinal "
	  		+ " and (not_dataparcela between :datainicial and  :datafinal) and tno_codigo = 0 and processado = false ", nativeQuery = true)
		List<Notafiscal> find(@Param("cnpj") String cnpj, @Param("valorInicial") BigDecimal valorInicial, @Param("valorFinal") BigDecimal valorFinal, 
				@Param("datainicial") LocalDate datainicial, @Param("datafinal") LocalDate datafinal );
}
