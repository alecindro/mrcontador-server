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

// for static metamodels
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Conta_;
import br.com.mrcontador.domain.Parceiro_;
import br.com.mrcontador.repository.ContaRepository;
import br.com.mrcontador.service.dto.ContaCriteria;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Conta} entities in the database.
 * The main input is a {@link ContaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContaDTO} or a {@link Page} of {@link ContaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContaQueryService extends QueryService<Conta> {

    private final Logger log = LoggerFactory.getLogger(ContaQueryService.class);

    private final ContaRepository contaRepository;

        public ContaQueryService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    /**
     * Return a {@link List} of {@link ContaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Conta> findByCriteria(ContaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Conta> specification = createSpecification(criteria);
        return contaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ContaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Conta> findByCriteria(ContaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Conta> specification = createSpecification(criteria);
        return contaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Conta> specification = createSpecification(criteria);
        return contaRepository.count(specification);
    }

    /**
     * Function to convert {@link ContaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Conta> createSpecification(ContaCriteria criteria) {
        Specification<Conta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Conta_.id));
            }
            if (criteria.getConConta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConConta(), Conta_.conConta));
            }
            if (criteria.getConClassificacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConClassificacao(), Conta_.conClassificacao));
            }
            if (criteria.getConTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConTipo(), Conta_.conTipo));
            }
            if (criteria.getConDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConDescricao(), Conta_.conDescricao));
            }
            if (criteria.getConCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConCnpj(), Conta_.conCnpj));
            }
            if (criteria.getConGrau() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConGrau(), Conta_.conGrau));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Conta_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
