package br.com.mrcontador.repository;

import java.util.Date;
import java.util.List;

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

import br.com.mrcontador.domain.Inteligent;

/**
 * Spring Data  repository for the Inteligent entity.
 */

@Repository
public interface InteligentRepository extends JpaRepository<Inteligent, Long>, JpaSpecificationExecutor<Inteligent> {
	
	@Query(nativeQuery = true, value = "select processa_inteligente(?,?,?,?)")
	int callInteligent(Long parceiroID,Long agenciaBancariaID,Date begin,Date end);

  @EntityGraph(attributePaths = "{comprovante,notafiscal,conta,extrato}")
	List<Inteligent> findAll();
  
  @EntityGraph(attributePaths = {"comprovante","notafiscal","conta","extrato"})
  List<Inteligent> findAll(@Nullable Specification<Inteligent> spec, Sort sort);
  
  @EntityGraph(attributePaths = {"comprovante","notafiscal","conta","extrato"})
  List<Inteligent> findAll(@Nullable Specification<Inteligent> spec);
  
  @Query(value="select periodo from inteligent where parceiro_id = :parceiroId and agenciabancaria_id = :agenciabancariaId group by periodo order by cast(periodo as integer) desc", nativeQuery = true)
  public List<String> periodos(@Param("parceiroId") Long parceiroId, @Param("agenciabancariaId") Long agenciabancariaId);
  
  @Modifying(flushAutomatically = true)
  @Query(value ="update inteligent set historicofinal = :historico, conta_id = :contaId, regra_id = :regraId, associado = true where id in (select i.id from inteligent i inner join extrato e on i.extrato_id = e.id " + 
  		"where e.info_adicional = :descricao and " + 
  		"i.parceiro_id = :parceiroId and i.associado = false )",nativeQuery = true )
  void updateFromInformacaoRegra(@Param("historico") String historico, @Param("descricao") String descricao, @Param("contaId") Long contaId, @Param("parceiroId") Long parceiroId, @Param("regraId") Long regraId);
  
  @Modifying(flushAutomatically = true)
  @Query(value="update inteligent set historicofinal = :historico, conta_id = :contaId,  regra_id = :regraId, associado = true where historico = :descricao and parceiro_id = :parceiroId  and associado = false",nativeQuery = true)
  void updateFromHistoricoRegra(@Param("historico") String historico, @Param("descricao") String descricao, @Param("contaId") Long contaId, @Param("parceiroId") Long parceiroId, @Param("regraId") Long regraId);
  
  @Modifying(flushAutomatically = true)
  @Query(value="update inteligent set historicofinal = :historico, conta_id = :contaId where parceiro_id = :parceiroId  and regra_id = :regraId",nativeQuery = true)
  void updateRegra(@Param("historico") String historico, @Param("parceiroId") Long parceiroId, @Param("regraId") Long regraId, @Param("contaId") Long contaId);
  
  
  @Modifying(flushAutomatically = true)
  @Query(value="update inteligent set historicofinal = null, conta_id = null, associado = false, regra_id = null  where regra_id = :regraId and parceiro_id = :parceiroId",nativeQuery = true)
  void deleteRegra(@Param("regraId") Long regraId, @Param("parceiroId") Long parceiroId);
  
}
