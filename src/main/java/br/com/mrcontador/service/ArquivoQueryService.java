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

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.*; // for static metamodels
import br.com.mrcontador.repository.ArquivoRepository;
import br.com.mrcontador.service.dto.ArquivoCriteria;

/**
 * Service for executing complex queries for {@link Arquivo} entities in the database.
 * The main input is a {@link ArquivoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Arquivo} or a {@link Page} of {@link Arquivo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArquivoQueryService extends QueryService<Arquivo> {

    private final Logger log = LoggerFactory.getLogger(ArquivoQueryService.class);

    private final ArquivoRepository arquivoRepository;

    public ArquivoQueryService(ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    /**
     * Return a {@link List} of {@link Arquivo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Arquivo> findByCriteria(ArquivoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Arquivo> specification = createSpecification(criteria);
        return arquivoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Arquivo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Arquivo> findByCriteria(ArquivoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Arquivo> specification = createSpecification(criteria);
        return arquivoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArquivoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Arquivo> specification = createSpecification(criteria);
        return arquivoRepository.count(specification);
    }

    /**
     * Function to convert {@link ArquivoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Arquivo> createSpecification(ArquivoCriteria criteria) {
        Specification<Arquivo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Arquivo_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Arquivo_.nome));
            }
            if (criteria.getNomeOriginal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeOriginal(), Arquivo_.nomeOriginal));
            }
            if (criteria.getDataCadastro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCadastro(), Arquivo_.dataCadastro));
            }
            if (criteria.getTipoArquivo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipoArquivo(), Arquivo_.tipoArquivo));
            }
            if (criteria.getTipoDoc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipoDoc(), Arquivo_.tipoDoc));
            }
            if (criteria.gets3Url() != null) {
                specification = specification.and(buildStringSpecification(criteria.gets3Url(), Arquivo_.s3Url));
            }
            if (criteria.gets3Dir() != null) {
                specification = specification.and(buildStringSpecification(criteria.gets3Dir(), Arquivo_.s3Dir));
            }
            if (criteria.getTamanho() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTamanho(), Arquivo_.tamanho));
            }
            if (criteria.getEtag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtag(), Arquivo_.etag));
            }
            if (criteria.getUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsuario(), Arquivo_.usuario));
            }
            if (criteria.getParceiroId() != null) {
                specification = specification.and(buildSpecification(criteria.getParceiroId(),
                    root -> root.join(Arquivo_.parceiro, JoinType.LEFT).get(Parceiro_.id)));
            }
        }
        return specification;
    }
}
