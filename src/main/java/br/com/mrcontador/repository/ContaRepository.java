package br.com.mrcontador.repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;

/**
 * Spring Data  repository for the Conta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>, JpaSpecificationExecutor<Conta> {
	
	Optional<Conta> findFirstByParceiro(Parceiro parceiro);
	Optional<Conta> findByConConta(Integer conConta);
	List<Conta> findByParceiro(Parceiro parceiro);
	
	  @Modifying(flushAutomatically = true)
	  @Query(value ="update conta set con_classificacao = :conClassificacao, con_tipo = :conTipo, con_descricao = :conDescricao, "
	  		+ "con_cnpj = :conCnpj, con_grau = :conGrau, last_modified_by = :user, last_modified_date = :lastModifiedDate where con_conta = :conConta and parceiro_id = :parceiroId",nativeQuery = true )
	  void updateConta(@Param("conClassificacao") String conClassificacao, @Param("conTipo") String conTipo,
			  @Param("conDescricao") String conDescricao, @Param("conCnpj") String conCnpj, @Param("conGrau") Integer conGrau, 
			  @Param("lastModifiedDate") Instant lastModifiedDate, @Param("user") String user, @Param("conConta") Integer conConta, @Param("parceiroId") Long parceiroId);
	  
	  @Modifying(flushAutomatically = true)
	  @Query(value ="insert into conta (data_cadastro,con_classificacao, con_tipo, con_descricao, con_cnpj, con_grau, con_conta, created_by, created_date, parceiro_id) values ("
	  		+ ":dataCadastro,:conClassificacao, :conTipo, :conDescricao, :conCnpj, :conGrau, :conConta,:createdBy, :createdDate, :parceiroId) ON CONFLICT on constraint conta_unique DO nothing",nativeQuery = true )
	  void createConta(@Param("dataCadastro") Date dataCadastro, @Param("conClassificacao") String conClassificacao, @Param("conTipo") String conTipo,
			  @Param("conDescricao") String conDescricao, @Param("conCnpj") String conCnpj, @Param("conGrau") Integer conGrau, @Param("conConta") Integer conConta, 
			  @Param("createdDate") Instant createdDate, @Param("createdBy") String user, 
			  @Param("parceiroId") Long parceiroId);
	  
	  @Modifying(flushAutomatically = true)
	  @Query(value ="update conta set arquivo_id = :arquivoId where id = :id ",nativeQuery = true )
	  void updateArquivo(@Param("id") Long id, @Param("arquivoId") Long arquivoId);
	  
		@Query(nativeQuery = true, value = "select processa_conta_update(?)")
		int callContaUpdateFunction(Long parceiroId);
		
		@Query(nativeQuery = true, value = "select processa_conta(?,?)")
		int callContaFunction(Long parceiroId,String periodo);
		
		@Query(nativeQuery = true, value = "select processa_contaInteligent(?)")
		int callContaFunctionInteligent(Long inteligentId);
		
}
