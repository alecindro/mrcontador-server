package br.com.mrcontador.repository;

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

import br.com.mrcontador.domain.Extrato;

/**
 * Spring Data repository for the Extrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtratoRepository extends JpaRepository<Extrato, Long>, JpaSpecificationExecutor<Extrato> {

	@EntityGraph(attributePaths = { "arquivo" })
	Page<Extrato> findAll(@Nullable Specification<Extrato> spec, Pageable pageable);
	
	@EntityGraph(attributePaths = { "arquivo" })
	List<Extrato> findAll(@Nullable Specification<Extrato> spec, Sort sort);

	@Query("FROM Extrato AS ex LEFT JOIN FETCH ex.arquivo WHERE ex.id = ?1")
	Optional<Extrato> findById(Long id);

	@Query(nativeQuery = true, value = "select extrato_functionBB(?,?,?)")
	int callExtratoBB(Long parceiroId, Long agenciaId,String periodo);
	
	@Query(nativeQuery = true, value = "select extrato_functionBRADESCO(?,?,?)")
	int callExtratoBradesco(Long parceiroId, Long agenciaId,String periodo);
	
	@Query(nativeQuery = true, value = "select extrato_functionSANTANDER(?,?,?)")
	int callExtratoSantander(Long parceiroId, Long agenciaId,String periodo);
	
	@Query(nativeQuery = true, value = "select extrato_functionCEF(?,?,?)")
	int callExtratoCef(Long parceiroId, Long agenciaId,String periodo);

	@Query(nativeQuery = true, value = "select extrato_functionCREDICREA(?,?,?)")
	int callExtratoCredicrea(Long parceiroId, Long agenciaId,String periodo);

	@Query(nativeQuery = true, value = "select extrato_functionITAU(?,?,?)")
	int callExtratoItau(Long parceiroId, Long agenciaId,String periodo);

	@Query(nativeQuery = true, value = "select extrato_functionSAFRA(?,?,?)")
	int callExtratoSafra(Long parceiroId, Long agenciaId,String periodo);

	@Query(nativeQuery = true, value = "select extrato_functionUNICRED(?,?,?)")
	int callExtratoUnicred(Long parceiroId, Long agenciaId,String periodo);
	
	@Query(nativeQuery = true, value = "select extrato_functionSICRED(?,?,?)")
	int callExtratoSicred(Long parceiroId, Long agenciaId,String periodo);
	
	@Query(nativeQuery = true, value = "select extrato_functionINTER(?,?,?)")
	int callExtratoInter(Long parceiroId, Long agenciaId,String periodo);
	
	@Modifying(flushAutomatically = true)
	@Query(value = "update extrato set processado = true where id = :extratoId", nativeQuery = true)
	void processadoTrue(@Param("extratoId") Long id);
	
	@Modifying(flushAutomatically = true)
	@Query(value = "update inteligent set conta_id = subquery.conta_id, " + 
			"historicofinal = subquery.hf, " + 
			"regra_id = subquery.regra_id, " + 
			"associado = true " + 
			"from " + 
			"(select intel.id as intel_id, r.reg_historico as hf ,r.id as regra_id,r.conta_id as conta_id  from " + 
			"(select i.id,e.ext_historico,e.info_adicional, i.beneficiario from inteligent i inner join extrato e " + 
			"on i.extrato_id = e.id where i.parceiro_id = :parceiroId and i.associado = false and i.periodo = :periodo) as intel, regra r " + 
			"where ((r.reg_descricao = intel.ext_historico and r.tipo_regra = 'HISTORICO') " + 
			"or(r.reg_descricao = intel.info_adicional and r.tipo_regra = 'INFORMACAO_ADICIONAL') or(r.reg_descricao = intel.beneficiario and r.tipo_regra = 'BENEFICIARIO'))" + 
			"and r.parceiro_id = :parceiroId) as subquery " + 
			"where id = subquery.intel_id;", nativeQuery = true)
	void regraInteligent(@Param("parceiroId") Long parceiroId,@Param("periodo") String periodo);

}
