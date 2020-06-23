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

import br.com.mrcontador.domain.Contador;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.ContadorRepository;
import br.com.mrcontador.service.dto.ContadorCriteria;
import br.com.mrcontador.service.dto.ContadorDTO;
import br.com.mrcontador.service.mapper.ContadorMapper;

/**
 * Service for executing complex queries for {@link Contador} entities in the database.
 * The main input is a {@link ContadorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContadorDTO} or a {@link Page} of {@link ContadorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContadorQueryService extends QueryService<Contador> {

    private final Logger log = LoggerFactory.getLogger(ContadorQueryService.class);

    private final ContadorRepository contadorRepository;

    private final ContadorMapper contadorMapper;

    public ContadorQueryService(ContadorRepository contadorRepository, ContadorMapper contadorMapper) {
        this.contadorRepository = contadorRepository;
        this.contadorMapper = contadorMapper;
    }

    /**
     * Return a {@link List} of {@link ContadorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContadorDTO> findByCriteria(ContadorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Contador> specification = createSpecification(criteria);
        return contadorMapper.toDto(contadorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContadorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContadorDTO> findByCriteria(ContadorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contador> specification = createSpecification(criteria);
        return contadorRepository.findAll(specification, page)
            .map(contadorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContadorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Contador> specification = createSpecification(criteria);
        return contadorRepository.count(specification);
    }

    /**
     * Function to convert {@link ContadorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Contador> createSpecification(ContadorCriteria criteria) {
        Specification<Contador> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Contador_.id));
            }
            if (criteria.getRazao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRazao(), Contador_.razao));
            }
            if (criteria.getFantasia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFantasia(), Contador_.fantasia));
            }
            if (criteria.getTelefones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefones(), Contador_.telefones));
            }
            if (criteria.getDatasource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDatasource(), Contador_.datasource));
            }
            if (criteria.getCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCnpj(), Contador_.cnpj));
            }
            if (criteria.getCidade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCidade(), Contador_.cidade));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), Contador_.estado));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Contador_.cep));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Contador_.email));
            }
        }
        return specification;
    }
}
