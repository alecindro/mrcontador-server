package br.com.mrcontador.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Parceiro_;
import br.com.mrcontador.domain.PermissaoParceiro;
import br.com.mrcontador.domain.PermissaoParceiro_;
import br.com.mrcontador.repository.PermissaoParceiroRepository;
import br.com.mrcontador.service.dto.PermissaoParceiroCriteria;
import io.github.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class PermissaoParceiroQueryService extends QueryService<PermissaoParceiro>{

	
	 private final Logger log = LoggerFactory.getLogger(PermissaoParceiroQueryService.class);
	 
	 private final PermissaoParceiroRepository permissaoParceiroRepository;
	 
	 public PermissaoParceiroQueryService(PermissaoParceiroRepository permissaoParceiroRepository) {
		 this.permissaoParceiroRepository = permissaoParceiroRepository;
	 }
	 
	    @Transactional(readOnly = true)
	    public List<PermissaoParceiro> findByCriteria(PermissaoParceiroCriteria criteria) {
	        log.debug("find by criteria : {}", criteria);
	        final Specification<PermissaoParceiro> specification = createSpecification(criteria,Specification.where(null));
	        return permissaoParceiroRepository.findAll(specification);
	    }
	    protected Specification<PermissaoParceiro> createSpecification(PermissaoParceiroCriteria criteria, Specification<PermissaoParceiro> specification) {
	        if (criteria != null) {
	            if (criteria.getId() != null) {
	                specification = specification.and(buildRangeSpecification(criteria.getId(), PermissaoParceiro_.id));
	            }
	            if (criteria.getDataCadastro() != null) {
	                specification = specification.and(buildRangeSpecification(criteria.getDataCadastro(), PermissaoParceiro_.dataCadastro));
	            }
	            if (criteria.getParceiroId() != null) {
	            	specification = specification.and(buildSpecification(criteria.getParceiroId(),
	                        root -> root.join(PermissaoParceiro_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
	            }
	            if (criteria.getUsuario() != null) {
	                specification = specification.and(buildStringSpecification(criteria.getUsuario(), PermissaoParceiro_.usuario));
	            }
	        }
	        return specification;
	    }	
}
