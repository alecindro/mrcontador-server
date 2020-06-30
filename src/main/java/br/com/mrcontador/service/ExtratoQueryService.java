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

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.service.dto.ExtratoCriteria;
import br.com.mrcontador.service.dto.ExtratoDTO;
import br.com.mrcontador.service.mapper.ExtratoMapper;

/**
 * Service for executing complex queries for {@link Extrato} entities in the database.
 * The main input is a {@link ExtratoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExtratoDTO} or a {@link Page} of {@link ExtratoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExtratoQueryService extends QueryService<Extrato> {

    private final Logger log = LoggerFactory.getLogger(ExtratoQueryService.class);

    private final ExtratoRepository extratoRepository;

    private final ExtratoMapper extratoMapper;

    public ExtratoQueryService(ExtratoRepository extratoRepository, ExtratoMapper extratoMapper) {
        this.extratoRepository = extratoRepository;
        this.extratoMapper = extratoMapper;
    }

    /**
     * Return a {@link List} of {@link ExtratoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExtratoDTO> findByCriteria(ExtratoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Extrato> specification = createSpecification(criteria);
        return extratoMapper.toDto(extratoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExtratoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExtratoDTO> findByCriteria(ExtratoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Extrato> specification = createSpecification(criteria);
        return extratoRepository.findAll(specification, page)
            .map(extratoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExtratoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Extrato> specification = createSpecification(criteria);
        return extratoRepository.count(specification);
    }

    /**
     * Function to convert {@link ExtratoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Extrato> createSpecification(ExtratoCriteria criteria) {
        Specification<Extrato> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Extrato_.id));
            }
            if (criteria.getExt_datalancamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExt_datalancamento(), Extrato_.ext_datalancamento));
            }
            if (criteria.getExt_historico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExt_historico(), Extrato_.ext_historico));
            }
            if (criteria.getExt_numerodocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExt_numerodocumento(), Extrato_.ext_numerodocumento));
            }
            if (criteria.getExt_numerocontrole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExt_numerocontrole(), Extrato_.ext_numerocontrole));
            }
            if (criteria.getExt_debito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExt_debito(), Extrato_.ext_debito));
            }
            if (criteria.getExt_credito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExt_credito(), Extrato_.ext_credito));
            }
            if (criteria.getExt_descricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExt_descricao(), Extrato_.ext_descricao));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Extrato_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
            if (criteria.getAgenciabancariaId() != null) {
                specification = specification.and(buildSpecification(criteria.getAgenciabancariaId(),
                    root -> root.join(Extrato_.agenciabancaria, JoinType.LEFT).get(Agenciabancaria_.id)));
            }
        }
        return specification;
    }
}
