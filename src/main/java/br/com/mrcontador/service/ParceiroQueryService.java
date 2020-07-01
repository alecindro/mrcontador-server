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

import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.ParceiroRepository;
import br.com.mrcontador.service.dto.ParceiroCriteria;
import br.com.mrcontador.service.dto.ParceiroDTO;
import br.com.mrcontador.service.mapper.ParceiroMapper;

/**
 * Service for executing complex queries for {@link Parceiro} entities in the database.
 * The main input is a {@link ParceiroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParceiroDTO} or a {@link Page} of {@link ParceiroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParceiroQueryService extends QueryService<Parceiro> {

    private final Logger log = LoggerFactory.getLogger(ParceiroQueryService.class);

    private final ParceiroRepository parceiroRepository;

    private final ParceiroMapper parceiroMapper;

    public ParceiroQueryService(ParceiroRepository parceiroRepository, ParceiroMapper parceiroMapper) {
        this.parceiroRepository = parceiroRepository;
        this.parceiroMapper = parceiroMapper;
    }

    /**
     * Return a {@link List} of {@link ParceiroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParceiroDTO> findByCriteria(ParceiroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Parceiro> specification = createSpecification(criteria);
        return parceiroMapper.toDto(parceiroRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ParceiroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParceiroDTO> findByCriteria(ParceiroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Parceiro> specification = createSpecification(criteria);
        return parceiroRepository.findAll(specification, page)
            .map(parceiroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParceiroCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Parceiro> specification = createSpecification(criteria);
        return parceiroRepository.count(specification);
    }

    /**
     * Function to convert {@link ParceiroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Parceiro> createSpecification(ParceiroCriteria criteria) {
        Specification<Parceiro> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Parceiro_.id));
            }
            if (criteria.getPar_descricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_descricao(), Parceiro_.par_descricao));
            }
            if (criteria.getPar_razaosocial() != null) {
                specification = specification.or(buildStringSpecification(criteria.getPar_razaosocial(), Parceiro_.par_razaosocial));
            }
            if (criteria.getPar_tipopessoa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_tipopessoa(), Parceiro_.par_tipopessoa));
            }
            if (criteria.getPar_cnpjcpf() != null) {
                specification = specification.or(buildStringSpecification(criteria.getPar_cnpjcpf(), Parceiro_.par_cnpjcpf));
            }
            if (criteria.getPar_rgie() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_rgie(), Parceiro_.par_rgie));
            }
            if (criteria.getPar_obs() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_obs(), Parceiro_.par_obs));
            }
            if (criteria.getPar_datacadastro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPar_datacadastro(), Parceiro_.par_datacadastro));
            }
            if (criteria.getSpa_codigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSpa_codigo(), Parceiro_.spa_codigo));
            }
            if (criteria.getLogradouro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogradouro(), Parceiro_.logradouro));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Parceiro_.cep));
            }
            if (criteria.getCidade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCidade(), Parceiro_.cidade));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), Parceiro_.estado));
            }
            if (criteria.getArea_atuacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getArea_atuacao(), Parceiro_.area_atuacao));
            }
            if (criteria.getComercio() != null) {
                specification = specification.and(buildSpecification(criteria.getComercio(), Parceiro_.comercio));
            }
            if (criteria.getNfc_e() != null) {
                specification = specification.and(buildSpecification(criteria.getNfc_e(), Parceiro_.nfc_e));
            }
            if (criteria.getDanfe() != null) {
                specification = specification.and(buildSpecification(criteria.getDanfe(), Parceiro_.danfe));
            }
            if (criteria.getServico() != null) {
                specification = specification.and(buildSpecification(criteria.getServico(), Parceiro_.servico));
            }
            if (criteria.getNfs_e() != null) {
                specification = specification.and(buildSpecification(criteria.getNfs_e(), Parceiro_.nfs_e));
            }
            if (criteria.getTransportadora() != null) {
                specification = specification.and(buildSpecification(criteria.getTransportadora(), Parceiro_.transportadora));
            }
            if (criteria.getConhec_transporte() != null) {
                specification = specification.and(buildSpecification(criteria.getConhec_transporte(), Parceiro_.conhec_transporte));
            }
            if (criteria.getIndustria() != null) {
                specification = specification.and(buildSpecification(criteria.getIndustria(), Parceiro_.industria));
            }
            if (criteria.getCt() != null) {
                specification = specification.and(buildSpecification(criteria.getCt(), Parceiro_.ct));
            }
            if (criteria.getOutras() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutras(), Parceiro_.outras));
            }
        }
        return specification;
    }
}
