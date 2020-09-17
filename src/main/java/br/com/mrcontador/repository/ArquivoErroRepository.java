package br.com.mrcontador.repository;

import br.com.mrcontador.domain.ArquivoErro;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArquivoErro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArquivoErroRepository extends JpaRepository<ArquivoErro, Long>, JpaSpecificationExecutor<ArquivoErro> {
}
