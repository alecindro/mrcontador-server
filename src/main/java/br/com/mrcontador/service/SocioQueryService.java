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
import br.com.mrcontador.domain.Parceiro_;
import br.com.mrcontador.domain.Socio;
import br.com.mrcontador.domain.Socio_;
import br.com.mrcontador.repository.SocioRepository;
import br.com.mrcontador.service.dto.SocioCriteria;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Socio} entities in the database.
 * The main input is a {@link SocioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Socio} or a {@link Page} of {@link Socio} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SocioQueryService extends QueryService<Socio> {

    private final Logger log = LoggerFactory.getLogger(SocioQueryService.class);

    private final SocioRepository socioRepository;

    public SocioQueryService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    /**
     * Return a {@link List} of {@link Socio} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Socio> findByCriteria(SocioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Socio> specification = createSpecification(criteria);
        return socioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Socio} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Socio> findByCriteria(SocioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Socio> specification = createSpecification(criteria);
        return socioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SocioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Socio> specification = createSpecification(criteria);
        return socioRepository.count(specification);
    }

    /**
     * Function to convert {@link SocioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Socio> createSpecification(SocioCriteria criteria) {
        Specification<Socio> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Socio_.id));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Socio_.descricao));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Socio_.nome));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Socio_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
