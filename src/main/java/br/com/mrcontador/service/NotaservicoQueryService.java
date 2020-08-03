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
import br.com.mrcontador.domain.Notaservico;
import br.com.mrcontador.domain.Notaservico_;
import br.com.mrcontador.domain.Parceiro_;
import br.com.mrcontador.repository.NotaservicoRepository;
import br.com.mrcontador.service.dto.NotaservicoCriteria;
import br.com.mrcontador.service.dto.NotaservicoDTO;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Notaservico} entities in the database.
 * The main input is a {@link NotaservicoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotaservicoDTO} or a {@link Page} of {@link NotaservicoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotaservicoQueryService extends QueryService<Notaservico> {

    private final Logger log = LoggerFactory.getLogger(NotaservicoQueryService.class);

    private final NotaservicoRepository notaservicoRepository;

    public NotaservicoQueryService(NotaservicoRepository notaservicoRepository) {
        this.notaservicoRepository = notaservicoRepository;
    }

    /**
     * Return a {@link List} of {@link NotaservicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Notaservico> findByCriteria(NotaservicoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Notaservico> specification = createSpecification(criteria);
        return notaservicoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link NotaservicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Notaservico> findByCriteria(NotaservicoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notaservico> specification = createSpecification(criteria);
        return notaservicoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotaservicoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Notaservico> specification = createSpecification(criteria);
        return notaservicoRepository.count(specification);
    }

    /**
     * Function to convert {@link NotaservicoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Notaservico> createSpecification(NotaservicoCriteria criteria) {
        Specification<Notaservico> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Notaservico_.id));
            }
            if (criteria.getNseNumero() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNseNumero(), Notaservico_.nseNumero));
            }
            if (criteria.getNseDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNseDescricao(), Notaservico_.nseDescricao));
            }
            if (criteria.getNseCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNseCnpj(), Notaservico_.nseCnpj));
            }
            if (criteria.getNseEmpresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNseEmpresa(), Notaservico_.nseEmpresa));
            }
            if (criteria.getNseDatasaida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNseDatasaida(), Notaservico_.nseDatasaida));
            }
            if (criteria.getNseValornota() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNseValornota(), Notaservico_.nseValornota));
            }
            if (criteria.getNseDataparcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNseDataparcela(), Notaservico_.nseDataparcela));
            }
            if (criteria.getNseValorparcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNseValorparcela(), Notaservico_.nseValorparcela));
            }
            if (criteria.getTnoCodigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTnoCodigo(), Notaservico_.tnoCodigo));
            }
            if (criteria.getNseParcela() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNseParcela(), Notaservico_.nseParcela));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Notaservico_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
