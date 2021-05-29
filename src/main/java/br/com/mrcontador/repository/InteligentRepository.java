package br.com.mrcontador.repository;

import java.time.LocalDate;
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

  @EntityGraph(attributePaths = {"comprovante","notafiscal","conta","extrato","parceiro","agenciabancaria"})
	List<Inteligent> findAll();
  
  @EntityGraph(attributePaths = {"comprovante","notafiscal","conta","extrato","parceiro","agenciabancaria"})
  List<Inteligent> findAll(@Nullable Specification<Inteligent> spec, Sort sort);
  
  @EntityGraph(attributePaths = {"comprovante","notafiscal","conta","extrato","parceiro","agenciabancaria"})
  List<Inteligent> findAll(@Nullable Specification<Inteligent> spec);
  
  @Query(value="select concat(mes,ano) as periodo from (select extract(month from datalancamento) as mes,  extract(YEAR from datalancamento) as ano from inteligent where parceiro_id = 10 and agenciabancaria_id = 22 group by ano, mes order by ano  desc, mes desc) as p", nativeQuery = true)
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
  @Query(value="update inteligent set historicofinal = :historico, conta_id = :contaId,  regra_id = :regraId, associado = true where beneficiario = :descricao and parceiro_id = :parceiroId  and associado = false",nativeQuery = true)
  void updateFromBeneficiarioRegra(@Param("historico") String historico, @Param("descricao") String descricao, @Param("contaId") Long contaId, @Param("parceiroId") Long parceiroId, @Param("regraId") Long regraId);
  
  
  @Modifying(flushAutomatically = true)
  @Query(value="update inteligent set historicofinal = :historico, conta_id = :contaId where parceiro_id = :parceiroId  and regra_id = :regraId",nativeQuery = true)
  void updateRegra(@Param("historico") String historico, @Param("parceiroId") Long parceiroId, @Param("regraId") Long regraId, @Param("contaId") Long contaId);
  
  
  @Modifying(flushAutomatically = true)
  @Query(value="update inteligent set historicofinal = null, conta_id = null, associado = false, regra_id = null  where regra_id = :regraId and parceiro_id = :parceiroId",nativeQuery = true)
  void deleteRegra(@Param("regraId") Long regraId, @Param("parceiroId") Long parceiroId);
  
  @Query(value="select  max(datalancamento) as max_data, periodo, count(id) as qtd,  sum(case when associado = true then 0 else 1 end) as divergente from ds_04656282000130.inteligent  where parceiro_id = :parceiroId group by periodo order by max_data desc limit 6",nativeQuery = true)
  List<InteligentStats> getInteligentStats(@Param("parceiroId") Long parceiroId);
  
  public interface InteligentStats{
	  LocalDate getMaxDate();
	  String getPeriodo();
	  Integer getQuantidade();
	  Integer getDivergente();
	  
  }
  
}
