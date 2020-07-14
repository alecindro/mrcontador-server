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
            if (criteria.getNotNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotNumero(), Notafiscal_.notNumero));
            }
            if (criteria.getNotDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotDescricao(), Notafiscal_.notDescricao));
            }
            if (criteria.getNotCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotCnpj(), Notafiscal_.notCnpj));
            }
            if (criteria.getNotEmpresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotEmpresa(), Notafiscal_.notEmpresa));
            }
            if (criteria.getNotDatasaida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNotDatasaida(), Notafiscal_.notDatasaida));
            }
            if (criteria.getNotValornota() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNotValornota(), Notafiscal_.notValornota));
            }
            if (criteria.getNotDataparcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNotDataparcela(), Notafiscal_.notDataparcela));
            }
            if (criteria.getNotValorparcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNotValorparcela(), Notafiscal_.notValorparcela));
            }
            if (criteria.getTnoCodigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTnoCodigo(), Notafiscal_.tnoCodigo));
            }
            if (criteria.getNotParcela() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotParcela(), Notafiscal_.notParcela));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Notafiscal_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
