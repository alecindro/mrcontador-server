package br.com.mrcontador.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import br.com.mrcontador.domain.Regra;
import br.com.mrcontador.domain.Regra_;
import br.com.mrcontador.repository.RegraRepository;
import br.com.mrcontador.service.dto.RegraCriteria;
import br.com.mrcontador.service.dto.RegraDTO;
import br.com.mrcontador.service.mapper.RegraMapper;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Regra} entities in the database.
 * The main input is a {@link RegraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RegraDTO} or a {@link Page} of {@link RegraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegraQueryService extends QueryService<Regra> {

    private final Logger log = LoggerFactory.getLogger(RegraQueryService.class);

    private final RegraRepository regraRepository;

    private final RegraMapper regraMapper;

    public RegraQueryService(RegraRepository regraRepository, RegraMapper regraMapper) {
        this.regraRepository = regraRepository;
        this.regraMapper = regraMapper;
    }

    /**
     * Return a {@link List} of {@link RegraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegraDTO> findByCriteria(RegraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Regra> specification = createSpecification(criteria);
        return regraMapper.toDto(regraRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RegraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RegraDTO> findByCriteria(RegraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Regra> specification = createSpecification(criteria);
        return regraRepository.findAll(specification, page)
            .map(regraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Regra> specification = createSpecification(criteria);
        return regraRepository.count(specification);
    }

    /**
     * Function to convert {@link RegraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Regra> createSpecification(RegraCriteria criteria) {
        Specification<Regra> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Regra_.id));
            }
            if (criteria.getPar_codigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPar_codigo(), Regra_.par_codigo));
            }
            if (criteria.getReg_descricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReg_descricao(), Regra_.reg_descricao));
            }
            if (criteria.getReg_conta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReg_conta(), Regra_.reg_conta));
            }
            if (criteria.getReg_historico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReg_historico(), Regra_.reg_historico));
            }
            if (criteria.getReg_todos() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReg_todos(), Regra_.reg_todos));
            }
        }
        return specification;
    }
}
