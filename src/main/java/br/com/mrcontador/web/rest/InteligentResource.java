package br.com.mrcontador.web.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.InteligentFunction;
import br.com.mrcontador.service.InteligentQueryService;
import br.com.mrcontador.service.InteligentService;
import br.com.mrcontador.service.dto.InteligentCriteria;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Inteligent}.
 */
@RestController
@RequestMapping("/api")
public class InteligentResource {

    private final Logger log = LoggerFactory.getLogger(InteligentResource.class);

    private final InteligentService inteligentService;

    private final InteligentQueryService inteligentQueryService;
    
    private final InteligentFunction function;
    
    private static final String ENTITY_NAME = "inteligent";
    
	@Value("${jhipster.clientApp.name}")
	private String applicationName;

    public InteligentResource(InteligentService inteligentService, InteligentQueryService inteligentQueryService,InteligentFunction function) {
        this.inteligentService = inteligentService;
        this.inteligentQueryService = inteligentQueryService;
        this.function = function;
    }

    /**
     * {@code GET  /inteligents} : get all the inteligents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inteligents in body.
     */
    @GetMapping("/inteligents")
    public ResponseEntity<List<Inteligent>> getAllInteligents(InteligentCriteria criteria, Sort sort) {
        log.debug("REST request to get Inteligents by criteria: {}", criteria);
        List<Inteligent> page = inteligentQueryService.findByCriteria(criteria, sort);
        //HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /inteligents/count} : count all the inteligents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/inteligents/count")
    public ResponseEntity<Long> countInteligents(InteligentCriteria criteria) {
        log.debug("REST request to count Inteligents by criteria: {}", criteria);
        return ResponseEntity.ok().body(inteligentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inteligents/:id} : get the "id" inteligent.
     *
     * @param id the id of the inteligent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inteligent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inteligents/{id}")
    public ResponseEntity<Inteligent> getInteligent(@PathVariable Long id) {
        log.debug("REST request to get Inteligent : {}", id);
        Optional<Inteligent> inteligent = inteligentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inteligent);
    }
    
    @GetMapping("/inteligents/function")
    public ResponseEntity<Void> processInteligent(@RequestParam("periodo") String periodo, @RequestParam("parceiroId") Long parceiroId, @RequestParam("agenciabancariaId") Long agenciabancariaId)
    		throws Exception {
    	log.debug("processando funcao. Periodo: {}, Parceiro: {}, Agencia:{}", periodo,parceiroId,agenciabancariaId);
    	try {
			function.callInteligent(parceiroId, agenciabancariaId, periodo, SecurityUtils.getCurrentTenantHeader());
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw new MrContadorException("inteligent.fail", e.getMessage());
		}
    	return ResponseEntity
				.created(new URI("/api/inteligents/function/")).headers(HeaderUtil
						.createAlert(applicationName, "mrcontadorFrontApp.inteligent.function.process",""))
				.build();
    }
}
