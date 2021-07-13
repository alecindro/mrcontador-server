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
  
  @Query(value="select  max(i.datalancamento) as maxDate, i.periodo, count(i.id) as quantidade,  sum(case when i.associado = true then 0 else 1 end) as divergente, " + 
  		"a.age_numero as agencia, b.ban_sigla as siglabanco, "+
  		"(select COUNT(i1.id) from inteligent i1 inner join agenciabancaria a1 on i1.agenciabancaria_id = a1.id where i1.parceiro_id = :parceiroId and i1.agenciabancaria_id = :agenciabancariaId) as total from inteligent i " + 
  		"inner join agenciabancaria a on i.agenciabancaria_id = a.id " + 
  		"inner join banco b on a.banco_id = b.id " + 
  		"where i.parceiro_id = :parceiroId and i.agenciabancaria_id = :agenciabancariaId group by i.periodo, a.age_numero, a.id,b.ban_sigla order by maxDate desc, a.id  limit 6",nativeQuery = true)
  List<InteligentStats> getInteligentStats(@Param("parceiroId") Long parceiroId, @Param("agenciabancariaId") Long agenciabancariaId);
  
  @Query(value="select  max(datalancamento) as maxDate, periodo, count(id) as quantidade,  sum(case when associado = true then 0 else 1 end) as divergente from inteligent  where parceiro_id = :parceiroId group by periodo order by maxDate desc limit 6",nativeQuery = true)
  List<InteligentStats> getInteligentStats(@Param("parceiroId") Long parceiroId);
  
  
  public interface InteligentStats{
	  LocalDate getMaxDate();
	  String getPeriodo();
	  Integer getQuantidade();
	  Integer getDivergente();
	  String getAgencia();
	  String getSiglabanco();
	  Long getTotal();
  }
  
  @Query(value="select  count(distinct periodo) from inteligent  where parceiro_id = :parceiroId",nativeQuery = true)
  Long getCountPeriodos(@Param("parceiroId") Long parceiroId);
    
  @Query(value="select i1.periodo as periodo, max(i1.datalancamento) as maxLancamento, (case when max(e.data_cadastro) is null then false else true end) as exportado from inteligent i1 " + 
  		" left join exportacao e on i1.periodo = e.periodo and " + 
  		" i1.agenciabancaria_id =e.agenciabancaria_id and i1.parceiro_id = e.parceiro_id " + 
  		" where i1.parceiro_id = :parceiroId and i1.agenciabancaria_id = :agenciabancariaId " + 
  		" and not exists (select 1 from inteligent i2 where i1.id = i2.id and associado  = false limit 1) " + 
  		" group by i1.periodo " + 
  		" order by maxLancamento desc " + 
  		" limit 6;",nativeQuery = true)
  List<StatsExport> getStatsExport(@Param("parceiroId") Long parceiroId,@Param("agenciabancariaId") Long agenciabancariaId);
  
  public interface StatsExport{
	  String getPeriodo();
	  Boolean getExportado();
  }
}
