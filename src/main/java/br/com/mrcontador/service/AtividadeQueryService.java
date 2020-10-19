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
import br.com.mrcontador.domain.Atividade;
import br.com.mrcontador.domain.Atividade_;
import br.com.mrcontador.domain.Parceiro_;
import br.com.mrcontador.repository.AtividadeRepository;
import br.com.mrcontador.service.dto.AtividadeCriteria;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Atividade} entities in the database.
 * The main input is a {@link AtividadeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Atividade} or a {@link Page} of {@link Atividade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AtividadeQueryService extends QueryService<Atividade> {

    private final Logger log = LoggerFactory.getLogger(AtividadeQueryService.class);

    private final AtividadeRepository atividadeRepository;

    public AtividadeQueryService(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    /**
     * Return a {@link List} of {@link Atividade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Atividade> findByCriteria(AtividadeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Atividade> specification = createSpecification(criteria);
        return atividadeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Atividade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Atividade> findByCriteria(AtividadeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Atividade> specification = createSpecification(criteria);
        return atividadeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AtividadeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Atividade> specification = createSpecification(criteria);
        return atividadeRepository.count(specification);
    }

    /**
     * Function to convert {@link AtividadeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Atividade> createSpecification(AtividadeCriteria criteria) {
        Specification<Atividade> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Atividade_.id));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Atividade_.descricao));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Atividade_.code));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), Atividade_.tipo));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Atividade_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
