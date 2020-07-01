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

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.AgenciabancariaRepository;
import br.com.mrcontador.service.dto.AgenciabancariaCriteria;
import br.com.mrcontador.service.dto.AgenciabancariaDTO;
import br.com.mrcontador.service.mapper.AgenciabancariaMapper;

/**
 * Service for executing complex queries for {@link Agenciabancaria} entities in the database.
 * The main input is a {@link AgenciabancariaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgenciabancariaDTO} or a {@link Page} of {@link AgenciabancariaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgenciabancariaQueryService extends QueryService<Agenciabancaria> {

    private final Logger log = LoggerFactory.getLogger(AgenciabancariaQueryService.class);

    private final AgenciabancariaRepository agenciabancariaRepository;

    private final AgenciabancariaMapper agenciabancariaMapper;

    public AgenciabancariaQueryService(AgenciabancariaRepository agenciabancariaRepository, AgenciabancariaMapper agenciabancariaMapper) {
        this.agenciabancariaRepository = agenciabancariaRepository;
        this.agenciabancariaMapper = agenciabancariaMapper;
    }

    /**
     * Return a {@link List} of {@link AgenciabancariaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Agenciabancaria> findByCriteria(AgenciabancariaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Agenciabancaria> specification = createSpecification(criteria);
        return agenciabancariaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AgenciabancariaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Agenciabancaria> findByCriteria(AgenciabancariaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agenciabancaria> specification = createSpecification(criteria);
        return agenciabancariaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgenciabancariaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Agenciabancaria> specification = createSpecification(criteria);
        return agenciabancariaRepository.count(specification);
    }

    /**
     * Function to convert {@link AgenciabancariaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Agenciabancaria> createSpecification(AgenciabancariaCriteria criteria) {
        Specification<Agenciabancaria> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Agenciabancaria_.id));
            }
            if (criteria.getAge_numero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAge_numero(), Agenciabancaria_.age_numero));
            }
            if (criteria.getAge_digito() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAge_digito(), Agenciabancaria_.age_digito));
            }
            if (criteria.getAge_agencia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAge_agencia(), Agenciabancaria_.age_agencia));
            }
            if (criteria.getAge_descricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAge_descricao(), Agenciabancaria_.age_descricao));
            }
            if (criteria.getAge_situacao() != null) {
                specification = specification.and(buildSpecification(criteria.getAge_situacao(), Agenciabancaria_.age_situacao));
            }
            if (criteria.getBancoId() != null) {
                specification = specification.and(buildSpecification(criteria.getBancoId(),
                    root -> root.join(Agenciabancaria_.banco, JoinType.LEFT).get(Banco_.id)));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Agenciabancaria_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
