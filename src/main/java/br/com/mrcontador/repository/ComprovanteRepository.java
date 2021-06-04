package br.com.mrcontador.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Comprovante;

/**
 * Spring Data  repository for the Comprovante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Long>, JpaSpecificationExecutor<Comprovante> {
	
	@EntityGraph(attributePaths = {"arquivo"})
	Page<Comprovante> findAll(@Nullable Specification<Comprovante> spec, Pageable pageable);
	
	@EntityGraph(attributePaths = {"arquivo"})
	List<Comprovante> findAll(@Nullable Specification<Comprovante> spec, Sort sort);
	
	@Query("FROM Comprovante AS co LEFT JOIN FETCH co.arquivo WHERE co.id = ?1")
	Optional<Comprovante> findById(Long id);
	
	/*@Query(nativeQuery = true, value = "select processa_comprovante(?)")
	int callComprovante(Long comprovanteId);
	*/
	@Query(nativeQuery = true, value = "select comprovante_bb(?,?,?)")
	int callComprovanteBB(Long parceiroId, Long agenciabancariaId, String periodo);
	
	@Query(nativeQuery = true, value = "select comprovante_santander(?,?,?)")
	int callComprovanteSantander(Long parceiroId, Long agenciabancariaId, String periodo);
	
	@Query(nativeQuery = true, value = "select comprovante_cef(?,?,?)")
	int callComprovanteCEF(Long parceiroId, Long agenciabancariaId, String periodo);

	@Query(nativeQuery = true, value = "select comprovante_credicrea(?,?,?)")
	int callComprovanteCredicrea(Long parceiroId, Long agenciabancariaId, String periodo);

	@Query(nativeQuery = true, value = "select comprovante_itau(?,?,?)")
	int callComprovanteItau(Long parceiroId, Long agenciabancariaId, String periodo);

	@Query(nativeQuery = true, value = "select comprovante_safra(?,?,?)")
	int callComprovanteSafra(Long parceiroId, Long agenciabancariaId, String periodo);
	
	@Query(nativeQuery = true, value = "select comprovante_unicred(?,?,?)")
	int callComprovanteUnicred(Long parceiroId, Long agenciabancariaId, String periodo);
	
	@Query(nativeQuery = true, value = "select comprovante_bradesco(?,?,?)")
	int callComprovanteBradesco(Long parceiroId, Long agenciabancariaId, String periodo);
	
	@Query(nativeQuery = true, value = "select comprovante_sicoob(?,?,?)")
	int callComprovanteSicoob(Long parceiroId, Long agenciabancariaId, String periodo);
	
	@Query(nativeQuery = true, value = "select comprovante_sicred(?,?,?)")
	int callComprovanteSicred(Long parceiroId, Long agenciabancariaId, String periodo);
	
	@Query(nativeQuery = true, value = "select processa_comprovantegeral(?)")
	int callComprovanteGeral(Long parceiroId);
	
	@Query(nativeQuery = true, value = "select comprovante_inter(?,?,?)")
	int callComprovanteInter(Long parceiroId, Long agenciabancariaId, String periodo);
	
	@Modifying(flushAutomatically = true)
	@Query(value = "update comprovante set processado = true where id = :comprovanteId", nativeQuery = true)
	void processadoTrue(@Param("comprovanteId") Long id);
	
	
	@Modifying(flushAutomatically = true)
	@Query(value = "update comprovante set arquivo_id = :arquivoId where id = :comprovanteId", nativeQuery = true)
	void updateArquivo(@Param("comprovanteId") Long id, @Param("arquivoId") Long arquivoId);
	
	@Query(value = "select  max(com_datapagamento) as maxDate, periodo, count(id) as quantidade,  sum(case when processado = true then 0 else 1 end) as divergente from comprovante  where parceiro_id = :parceiroId group by periodo order by maxDate desc limit 6", nativeQuery = true)
	List<ComprovanteStats> getComprovanteStats(@Param("parceiroId") Long parceiroId);

	public interface ComprovanteStats {
		LocalDate getMaxDate();

		String getPeriodo();

		Integer getQuantidade();

		Integer getDivergente();

	}
}
