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
                specification = specification.and(buildStringSpecification(criteria.getPar_descricao(), Parceiro_.parDescricao));
            }
            if (criteria.getPar_razaosocial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_razaosocial(), Parceiro_.parRazaosocial));
            }
            if (criteria.getPar_tipopessoa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_tipopessoa(), Parceiro_.parTipopessoa));
            }
            if (criteria.getPar_cnpjcpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_cnpjcpf(), Parceiro_.parCnpjcpf));
            }
            if (criteria.getPar_rgie() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_rgie(), Parceiro_.parRgie));
            }
            if (criteria.getPar_obs() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPar_obs(), Parceiro_.parObs));
            }
            if (criteria.getPar_datacadastro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPar_datacadastro(), Parceiro_.parDatacadastro));
            }
            if (criteria.getSpa_codigo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSpa_codigo(), Parceiro_.spaCodigo));
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
                specification = specification.and(buildStringSpecification(criteria.getArea_atuacao(), Parceiro_.areaAtuacao));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Parceiro_.numero));
            }
            if (criteria.getBairro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBairro(), Parceiro_.bairro));
            }
            if (criteria.getPorte() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPorte(), Parceiro_.porte));
            }
            if (criteria.getAbertura() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAbertura(), Parceiro_.abertura));
            }
            if (criteria.getNatureza_juridica() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNatureza_juridica(), Parceiro_.naturezaJuridica));
            }
            if (criteria.getUltimaAtualizacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUltimaAtualizacao(), Parceiro_.ultimaAtualizacao));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Parceiro_.status));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), Parceiro_.tipo));
            }
            if (criteria.getComplemento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComplemento(), Parceiro_.complemento));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Parceiro_.email));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefone(), Parceiro_.telefone));
            }
            if (criteria.getData_situacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData_situacao(), Parceiro_.dataSituacao));
            }
            if (criteria.getEfr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEfr(), Parceiro_.efr));
            }
            if (criteria.getMotivo_situacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMotivo_situacao(), Parceiro_.motivoSituacao));
            }
            if (criteria.getSituacao_especial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSituacao_especial(), Parceiro_.situacaoEspecial));
            }
            if (criteria.getData_situacao_especial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData_situacao_especial(), Parceiro_.dataSituacaoEspecial));
            }
            if (criteria.getCapital_social() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCapital_social(), Parceiro_.capitalSocial));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Parceiro_.enabled));
            }
            if (criteria.getAtividadeId() != null) {
                specification = specification.and(buildSpecification(criteria.getAtividadeId(),
                    root -> root.join(Parceiro_.atividades, JoinType.LEFT).get(Atividade_.id)));
            }
            if (criteria.getSocioId() != null) {
                specification = specification.and(buildSpecification(criteria.getSocioId(),
                    root -> root.join(Parceiro_.socios, JoinType.LEFT).get(Socio_.id)));
            }
        }
        return specification;
    }
}
