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

import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.ComprovanteRepository;
import br.com.mrcontador.service.dto.ComprovanteCriteria;
import br.com.mrcontador.service.dto.ComprovanteDTO;
import br.com.mrcontador.service.mapper.ComprovanteMapper;

/**
 * Service for executing complex queries for {@link Comprovante} entities in the database.
 * The main input is a {@link ComprovanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComprovanteDTO} or a {@link Page} of {@link ComprovanteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComprovanteQueryService extends QueryService<Comprovante> {

    private final Logger log = LoggerFactory.getLogger(ComprovanteQueryService.class);

    private final ComprovanteRepository comprovanteRepository;

    private final ComprovanteMapper comprovanteMapper;

    public ComprovanteQueryService(ComprovanteRepository comprovanteRepository, ComprovanteMapper comprovanteMapper) {
        this.comprovanteRepository = comprovanteRepository;
        this.comprovanteMapper = comprovanteMapper;
    }

    /**
     * Return a {@link List} of {@link ComprovanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComprovanteDTO> findByCriteria(ComprovanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Comprovante> specification = createSpecification(criteria);
        return comprovanteMapper.toDto(comprovanteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ComprovanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComprovanteDTO> findByCriteria(ComprovanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comprovante> specification = createSpecification(criteria);
        return comprovanteRepository.findAll(specification, page)
            .map(comprovanteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComprovanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Comprovante> specification = createSpecification(criteria);
        return comprovanteRepository.count(specification);
    }

    /**
     * Function to convert {@link ComprovanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Comprovante> createSpecification(ComprovanteCriteria criteria) {
        Specification<Comprovante> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Comprovante_.id));
            }
            if (criteria.getPar_codigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPar_codigo(), Comprovante_.par_codigo));
            }
            if (criteria.getAge_codigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge_codigo(), Comprovante_.age_codigo));
            }
            if (criteria.getCom_cnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCom_cnpj(), Comprovante_.com_cnpj));
            }
            if (criteria.getCom_beneficiario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCom_beneficiario(), Comprovante_.com_beneficiario));
            }
            if (criteria.getCom_documento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCom_documento(), Comprovante_.com_documento));
            }
            if (criteria.getCom_datavencimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCom_datavencimento(), Comprovante_.com_datavencimento));
            }
            if (criteria.getCom_datapagamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCom_datapagamento(), Comprovante_.com_datapagamento));
            }
            if (criteria.getCom_valordocumento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCom_valordocumento(), Comprovante_.com_valordocumento));
            }
            if (criteria.getCom_valorpagamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCom_valorpagamento(), Comprovante_.com_valorpagamento));
            }
            if (criteria.getCom_observacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCom_observacao(), Comprovante_.com_observacao));
            }
        }
        return specification;
    }
}
