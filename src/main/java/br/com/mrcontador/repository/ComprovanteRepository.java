package br.com.mrcontador.repository;

import br.com.mrcontador.domain.Comprovante;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Comprovante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Long>, JpaSpecificationExecutor<Comprovante> {
	
	@EntityGraph(attributePaths = {"arquivo"})
	Page<Comprovante> findAll(@Nullable Specification<Comprovante> spec, Pageable pageable);
	
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
	
	@Modifying(flushAutomatically = true)
	@Query(value = "update comprovante set processado = true where id = :comprovanteId", nativeQuery = true)
	void processadoTrue(@Param("comprovanteId") Long id);
	
	
	@Modifying(flushAutomatically = true)
	@Query(value = "update comprovante set arquivo_id = :arquivoId where id = :comprovanteId", nativeQuery = true)
	void updateArquivo(@Param("comprovanteId") Long id, @Param("arquivoId") Long arquivoId);
}
