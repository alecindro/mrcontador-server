package br.com.mrcontador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Exportacao;
import br.com.mrcontador.domain.Parceiro;

/**
 * Spring Data  repository for the Agenciabancaria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExportacaoRepository extends JpaRepository<Exportacao, Long>, JpaSpecificationExecutor<Exportacao> {
	
	@Query(value = "select  * from exportacao  where parceiro_id = :parceiroId order by data_cadastro desc limit 6", nativeQuery = true)
	List<Exportacao> findByParceiroLimit6(Long parceiroId);
}
