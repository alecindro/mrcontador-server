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

import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.InteligentRepository;
import br.com.mrcontador.service.dto.InteligentCriteria;

/**
 * Service for executing complex queries for {@link Inteligent} entities in the database.
 * The main input is a {@link InteligentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Inteligent} or a {@link Page} of {@link Inteligent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InteligentQueryService extends QueryService<Inteligent> {

    private final Logger log = LoggerFactory.getLogger(InteligentQueryService.class);

    private final InteligentRepository inteligentRepository;

    public InteligentQueryService(InteligentRepository inteligentRepository) {
        this.inteligentRepository = inteligentRepository;
    }

    /**
     * Return a {@link List} of {@link Inteligent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Inteligent> findByCriteria(InteligentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Inteligent> specification = createSpecification(criteria);
        return inteligentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Inteligent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Inteligent> findByCriteria(InteligentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inteligent> specification = createSpecification(criteria);
        return inteligentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InteligentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Inteligent> specification = createSpecification(criteria);
        return inteligentRepository.count(specification);
    }

    /**
     * Function to convert {@link InteligentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Inteligent> createSpecification(InteligentCriteria criteria) {
        Specification<Inteligent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Inteligent_.id));
            }
            if (criteria.getHistorico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHistorico(), Inteligent_.historico));
            }
            if (criteria.getHistoricofinal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHistoricofinal(), Inteligent_.historicofinal));
            }
            if (criteria.getDatalancamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatalancamento(), Inteligent_.datalancamento));
            }
            if (criteria.getAssociado() != null) {
                specification = specification.and(buildSpecification(criteria.getAssociado(), Inteligent_.associado));
            }
            if (criteria.getPeriodo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodo(), Inteligent_.periodo));
            }
            if (criteria.getDebito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDebito(), Inteligent_.debito));
            }
            if (criteria.getCredito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCredito(), Inteligent_.credito));
            }
            if (criteria.getDatainicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatainicio(), Inteligent_.datainicio));
            }
            if (criteria.getDatafim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatafim(), Inteligent_.datafim));
            }
            if (criteria.getCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCnpj(), Inteligent_.cnpj));
            }
            if (criteria.getBeneficiario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBeneficiario(), Inteligent_.beneficiario));
            }
            if (criteria.getComprovanteId() != null) {
                specification = specification.and(buildSpecification(criteria.getComprovanteId(),
                    root -> root.join(Inteligent_.comprovante, JoinType.LEFT).get(Comprovante_.id)));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Inteligent_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
            if (criteria.getNotafiscalId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotafiscalId(),
                    root -> root.join(Inteligent_.notafiscal, JoinType.LEFT).get(Notafiscal_.id)));
            }
            if (criteria.getNotaservicoId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotaservicoId(),
                    root -> root.join(Inteligent_.notaservico, JoinType.LEFT).get(Notaservico_.id)));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Inteligent_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
            if (criteria.getAgenciabancariaId() != null) {
                specification = specification.and(buildSpecification(criteria.getAgenciabancariaId(),
                    root -> root.join(Inteligent_.agenciabancaria, JoinType.LEFT).get(Agenciabancaria_.id)));
            }
        }
        return specification;
    }
}
