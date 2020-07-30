package br.com.mrcontador.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.domain.Banco_;
import br.com.mrcontador.repository.BancoRepository;
import br.com.mrcontador.service.dto.BancoCriteria;
import br.com.mrcontador.service.dto.BancoDTO;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Banco} entities in the database.
 * The main input is a {@link BancoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BancoDTO} or a {@link Page} of {@link BancoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BancoQueryService extends QueryService<Banco> {

    private final Logger log = LoggerFactory.getLogger(BancoQueryService.class);

    private final BancoRepository bancoRepository;

    public BancoQueryService(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    /**
     * Return a {@link List} of {@link BancoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Banco> findByCriteria(BancoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Banco> specification = createSpecification(criteria);
        return bancoRepository.findAll(specification);
    }
    
    @Transactional(readOnly = true)
    public List<Banco> findBancoByCriteria(BancoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Banco> specification = createSpecification(criteria);
        return bancoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BancoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Banco> findByCriteria(BancoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Banco> specification = createSpecification(criteria);
        return bancoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BancoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Banco> specification = createSpecification(criteria);
        return bancoRepository.count(specification);
    }

    /**
     * Function to convert {@link BancoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Banco> createSpecification(BancoCriteria criteria) {
        Specification<Banco> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Banco_.id));
            }
            if (criteria.getBanDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBanDescricao(), Banco_.banDescricao));
            }
            if (criteria.getBanSigla() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBanSigla(), Banco_.banSigla));
            }
            if (criteria.getBanIspb() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBanIspb(), Banco_.banIspb));
            }
            if (criteria.getBanCodigobancario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBanCodigobancario(), Banco_.banCodigobancario));
            }
        }
        return specification;
    }
}
