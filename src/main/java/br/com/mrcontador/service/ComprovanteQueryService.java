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
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Comprovante_;
import br.com.mrcontador.domain.Parceiro_;
import br.com.mrcontador.repository.ComprovanteRepository;
import br.com.mrcontador.service.dto.ComprovanteCriteria;
import br.com.mrcontador.service.dto.ComprovanteDTO;
import io.github.jhipster.service.QueryService;

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

    public ComprovanteQueryService(ComprovanteRepository comprovanteRepository) {
        this.comprovanteRepository = comprovanteRepository;
    }

    /**
     * Return a {@link List} of {@link ComprovanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Comprovante> findByCriteria(ComprovanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Comprovante> specification = createSpecification(criteria);
        return comprovanteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ComprovanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Comprovante> findByCriteria(ComprovanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comprovante> specification = createSpecification(criteria);
        return comprovanteRepository.findAll(specification, page);
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
            if (criteria.getParCodigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getParCodigo(), Comprovante_.parCodigo));
            }
            if (criteria.getAgeCodigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAgeCodigo(), Comprovante_.ageCodigo));
            }
            if (criteria.getComCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComCnpj(), Comprovante_.comCnpj));
            }
            if (criteria.getComBeneficiario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComBeneficiario(), Comprovante_.comBeneficiario));
            }
            if (criteria.getComDocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComDocumento(), Comprovante_.comDocumento));
            }
            if (criteria.getComDatavencimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComDatavencimento(), Comprovante_.comDatavencimento));
            }
            if (criteria.getComDatapagamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComDatapagamento(), Comprovante_.comDatapagamento));
            }
            if (criteria.getComValordocumento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComValordocumento(), Comprovante_.comValordocumento));
            }
            if (criteria.getComValorpagamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComValorpagamento(), Comprovante_.comValorpagamento));
            }
            if (criteria.getComObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComObservacao(), Comprovante_.comObservacao));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Comprovante_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
