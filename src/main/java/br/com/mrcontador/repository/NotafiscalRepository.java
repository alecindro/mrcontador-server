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
import br.com.mrcontador.repository.InteligentRepository.InteligentStats;

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
	@Query(nativeQuery = true, value = "select * from processa_notafiscalgeral(?,?)")
	int callProcessaNotafiscalGeral(Long parceiroId, LocalDate date);
	
	  @Modifying(flushAutomatically = true)
	  @Query(value="update notafiscal set processado = true where id = :notaId",nativeQuery = true)
	  void processadoTrue(@Param("notaId") Long id);
	  
	  @Modifying(flushAutomatically = true)
	  @Query(value="update notafiscal set arquivo_id = :arquivoId, arquivopdf_id= :arquivoPdfId  where id = :notaId",nativeQuery = true)
	  void updateArquivo(@Param("notaId") Long id, @Param("arquivoId") Long arquivoId, @Param("arquivoPdfId") Long arquivoPdfId );
	  
	  @Query(value="select * from notafiscal n where  substring(not_cnpj,1,8) = substring(:cnpj,1,8) and  NOT_VALORPARCELA between :valorInicial and :valorFinal "
	  		+ " and (not_dataparcela between :datainicial and  :datafinal) and tno_codigo = 0 and processado = false order by not_dataparcela asc", nativeQuery = true)
		List<Notafiscal> find(@Param("cnpj") String cnpj, @Param("valorInicial") BigDecimal valorInicial, @Param("valorFinal") BigDecimal valorFinal, 
				@Param("datainicial") LocalDate datainicial, @Param("datafinal") LocalDate datafinal );
	  
		@Query(value = "select  max(not_dataparcela) as maxDate, periodo, count(id) as quantidade,  sum(case when processado = true then 0 else 1 end) as divergente from notafiscal  where parceiro_id = :parceiroId group by periodo order by maxDate desc limit 6", nativeQuery = true)
		List<NFSStats> getNotaFiscalStats(@Param("parceiroId") Long parceiroId);

		public interface NFSStats {
			LocalDate getMaxDate();

			String getPeriodo();

			Integer getQuantidade();

			Integer getDivergente();

		}
}
