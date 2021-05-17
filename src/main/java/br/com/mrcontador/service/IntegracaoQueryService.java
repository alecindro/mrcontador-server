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

import br.com.mrcontador.domain.Integracao;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.IntegracaoRepository;
import br.com.mrcontador.service.dto.IntegracaoCriteria;

/**
 * Service for executing complex queries for {@link Integracao} entities in the database.
 * The main input is a {@link IntegracaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Integracao} or a {@link Page} of {@link Integracao} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IntegracaoQueryService extends QueryService<Integracao> {

    private final Logger log = LoggerFactory.getLogger(IntegracaoQueryService.class);

    private final IntegracaoRepository integracaoRepository;

    public IntegracaoQueryService(IntegracaoRepository integracaoRepository) {
        this.integracaoRepository = integracaoRepository;
    }

    /**
     * Return a {@link List} of {@link Integracao} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Integracao> findByCriteria(IntegracaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Integracao> specification = createSpecification(criteria);
        return integracaoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Integracao} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Integracao> findByCriteria(IntegracaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Integracao> specification = createSpecification(criteria);
        return integracaoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IntegracaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Integracao> specification = createSpecification(criteria);
        return integracaoRepository.count(specification);
    }

    /**
     * Function to convert {@link IntegracaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Integracao> createSpecification(IntegracaoCriteria criteria) {
        Specification<Integracao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Integracao_.id));
            }
            if (criteria.getIntegrador() != null) {
                specification = specification.and(buildSpecification(criteria.getIntegrador(), Integracao_.integrador));
            }
            if (criteria.getTipoIntegracao() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoIntegracao(), Integracao_.tipoIntegracao));
            }
            if (criteria.getDataInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataInicio(), Integracao_.dataInicio));
            }
            if (criteria.getDataCadastro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCadastro(), Integracao_.dataCadastro));
            }
            if (criteria.getHabilitado() != null) {
                specification = specification.and(buildSpecification(criteria.getHabilitado(), Integracao_.habilitado));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Integracao_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
