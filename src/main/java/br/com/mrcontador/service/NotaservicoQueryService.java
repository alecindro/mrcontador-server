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

import br.com.mrcontador.domain.Notaservico;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.NotaservicoRepository;
import br.com.mrcontador.service.dto.NotaservicoCriteria;
import br.com.mrcontador.service.dto.NotaservicoDTO;
import br.com.mrcontador.service.mapper.NotaservicoMapper;

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

    private final NotaservicoMapper notaservicoMapper;

    public NotaservicoQueryService(NotaservicoRepository notaservicoRepository, NotaservicoMapper notaservicoMapper) {
        this.notaservicoRepository = notaservicoRepository;
        this.notaservicoMapper = notaservicoMapper;
    }

    /**
     * Return a {@link List} of {@link NotaservicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotaservicoDTO> findByCriteria(NotaservicoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Notaservico> specification = createSpecification(criteria);
        return notaservicoMapper.toDto(notaservicoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotaservicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotaservicoDTO> findByCriteria(NotaservicoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notaservico> specification = createSpecification(criteria);
        return notaservicoRepository.findAll(specification, page)
            .map(notaservicoMapper::toDto);
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
            if (criteria.getNse_numero() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNse_numero(), Notaservico_.nse_numero));
            }
            if (criteria.getNse_descricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNse_descricao(), Notaservico_.nse_descricao));
            }
            if (criteria.getNse_cnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNse_cnpj(), Notaservico_.nse_cnpj));
            }
            if (criteria.getNse_empresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNse_empresa(), Notaservico_.nse_empresa));
            }
            if (criteria.getNse_datasaida() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNse_datasaida(), Notaservico_.nse_datasaida));
            }
            if (criteria.getNse_valornota() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNse_valornota(), Notaservico_.nse_valornota));
            }
            if (criteria.getNse_dataparcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNse_dataparcela(), Notaservico_.nse_dataparcela));
            }
            if (criteria.getNse_valorparcela() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNse_valorparcela(), Notaservico_.nse_valorparcela));
            }
            if (criteria.getTno_codigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTno_codigo(), Notaservico_.tno_codigo));
            }
            if (criteria.getNse_parcela() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNse_parcela(), Notaservico_.nse_parcela));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Notaservico_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
