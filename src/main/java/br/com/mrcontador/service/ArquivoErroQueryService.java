package br.com.mrcontador.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.ArquivoErroRepository;
import br.com.mrcontador.service.dto.ArquivoErroCriteria;

/**
 * Service for executing complex queries for {@link ArquivoErro} entities in the database.
 * The main input is a {@link ArquivoErroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArquivoErro} or a {@link Page} of {@link ArquivoErro} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArquivoErroQueryService extends QueryService<ArquivoErro> {

    private final Logger log = LoggerFactory.getLogger(ArquivoErroQueryService.class);

    private final ArquivoErroRepository arquivoErroRepository;

    public ArquivoErroQueryService(ArquivoErroRepository arquivoErroRepository) {
        this.arquivoErroRepository = arquivoErroRepository;
    }

    /**
     * Return a {@link List} of {@link ArquivoErro} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArquivoErro> findByCriteria(ArquivoErroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ArquivoErro> specification = createSpecification(criteria);
        return arquivoErroRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ArquivoErro} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArquivoErro> findByCriteria(ArquivoErroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArquivoErro> specification = createSpecification(criteria);
        return arquivoErroRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArquivoErroCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ArquivoErro> specification = createSpecification(criteria);
        return arquivoErroRepository.count(specification);
    }

    /**
     * Function to convert {@link ArquivoErroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ArquivoErro> createSpecification(ArquivoErroCriteria criteria) {
        Specification<ArquivoErro> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ArquivoErro_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), ArquivoErro_.nome));
            }
            if (criteria.getNomeOriginal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeOriginal(), ArquivoErro_.nomeOriginal));
            }
            if (criteria.getTipoArquivo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipoArquivo(), ArquivoErro_.tipoArquivo));
            }
            if (criteria.gets3Url() != null) {
                specification = specification.and(buildStringSpecification(criteria.gets3Url(), ArquivoErro_.s3Url));
            }
            if (criteria.gets3Dir() != null) {
                specification = specification.and(buildStringSpecification(criteria.gets3Dir(), ArquivoErro_.s3Dir));
            }
            if (criteria.getDataCadastro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCadastro(), ArquivoErro_.dataCadastro));
            }
            if (criteria.getUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsuario(), ArquivoErro_.usuario));
            }
            if (criteria.getContador() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContador(), ArquivoErro_.contador));
            }
            if (criteria.getTamanho() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTamanho(), ArquivoErro_.tamanho));
            }
        }
        return specification;
    }
}
