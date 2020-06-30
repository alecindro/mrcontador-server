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

import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.NotafiscalRepository;
import br.com.mrcontador.service.dto.NotafiscalCriteria;
import br.com.mrcontador.service.dto.NotafiscalDTO;
import br.com.mrcontador.service.mapper.NotafiscalMapper;

/**
 * Service for executing complex queries for {@link Notafiscal} entities in the database.
 * The main input is a {@link NotafiscalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotafiscalDTO} or a {@link Page} of {@link NotafiscalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotafiscalQueryService extends QueryService<Notafiscal> {

    private final Logger log = LoggerFactory.getLogger(NotafiscalQueryService.class);

    private final NotafiscalRepository notafiscalRepository;

    private final NotafiscalMapper notafiscalMapper;

    public NotafiscalQueryService(NotafiscalRepository notafiscalRepository, NotafiscalMapper notafiscalMapper) {
        this.notafiscalRepository = notafiscalRepository;
        this.notafiscalMapper = notafiscalMapper;
    }

    /**
     * Return a {@link List} of {@link NotafiscalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotafiscalDTO> findByCriteria(NotafiscalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Notafiscal> specification = createSpecification(criteria);
        return notafiscalMapper.toDto(notafiscalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotafiscalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotafiscalDTO> findByCriteria(NotafiscalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notafiscal> specification = createSpecification(criteria);
        return notafiscalRepository.findAll(specification, page)
            .map(notafiscalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotafiscalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Notafiscal> specification = createSpecification(criteria);
        return notafiscalRepository.count(specification);
    }

    /**
     * Function to convert {@link NotafiscalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Notafiscal> createSpecification(NotafiscalCriteria criteria) {
        Specification<Notafiscal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Notafiscal_.id));
            }
            if (criteria.getNot_numero() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNot_numero(), Notafiscal_.not_numero));
            }
            if (criteria.getNot_descricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNot_descricao(), Notafiscal_.not_descricao));
            }
            if (criteria.getNot_cnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNot_cnpj(), Notafiscal_.not_cnpj));
            }
            if (criteria.getNot_empresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNot_empresa(), Notafiscal_.not_empresa));
            }
            if (criteria.getNot_datasaida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNot_datasaida(), Notafiscal_.not_datasaida));
            }
            if (criteria.getNot_valornota() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNot_valornota(), Notafiscal_.not_valornota));
            }
            if (criteria.getNot_dataparcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNot_dataparcela(), Notafiscal_.not_dataparcela));
            }
            if (criteria.getNot_valorparcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNot_valorparcela(), Notafiscal_.not_valorparcela));
            }
            if (criteria.getTno_codigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTno_codigo(), Notafiscal_.tno_codigo));
            }
            if (criteria.getNot_parcela() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNot_parcela(), Notafiscal_.not_parcela));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Notafiscal_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
