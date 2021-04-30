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
import br.com.mrcontador.domain.Agenciabancaria_;
import br.com.mrcontador.domain.Arquivo_;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Extrato_;
import br.com.mrcontador.domain.Parceiro_;
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.service.dto.ExtratoCriteria;
import io.github.jhipster.service.QueryService;

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

    public ExtratoQueryService(ExtratoRepository extratoRepository) {
        this.extratoRepository = extratoRepository;
    }

    /**
     * Return a {@link List} of {@link ExtratoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Extrato> findByCriteria(ExtratoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Extrato> specification = createSpecification(criteria);
        return extratoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExtratoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Extrato> findByCriteria(ExtratoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Extrato> specification = createSpecification(criteria);
        return extratoRepository.findAll(specification, page);
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
            if (criteria.getExtDatalancamento() != null) {
            	specification = specification.and(buildRangeSpecification(criteria.getExtDatalancamento(), Extrato_.extDatalancamento));
            }
            if (criteria.getExtHistorico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtHistorico(), Extrato_.extHistorico));
            }
            if (criteria.getExtNumerodocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtNumerodocumento(), Extrato_.extNumerodocumento));
            }
            if (criteria.getExtNumerocontrole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtNumerocontrole(), Extrato_.extNumerocontrole));
            }
            if (criteria.getExtDebito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExtDebito(), Extrato_.extDebito));
            }
            if (criteria.getExtCredito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExtCredito(), Extrato_.extCredito));
            }
            if (criteria.getExtDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtDescricao(), Extrato_.extDescricao));
            }
            if (criteria.getProcessado() != null) {
                specification = specification.and(buildSpecification(criteria.getProcessado(), Extrato_.processado));
            }

            if (criteria.getPeriodo() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodo(), Extrato_.periodo));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Extrato_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
            if (criteria.getAgenciabancariaId() != null) {
                specification = specification.and(buildSpecification(criteria.getAgenciabancariaId(),
                    root -> root.join(Extrato_.agenciabancaria, JoinType.LEFT).get(Agenciabancaria_.id)));
            }
            if (criteria.getArquivoId() != null) {
                specification = specification.and(buildSpecification(criteria.getArquivoId(),
                    root -> root.join(Extrato_.arquivo, JoinType.LEFT).get(Arquivo_.id)));
            }
        }
        return specification;
    }
}
