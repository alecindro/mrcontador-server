package br.com.mrcontador.web.rest;

import br.com.mrcontador.domain.Integracao;
import br.com.mrcontador.service.IntegracaoService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.IntegracaoCriteria;
import br.com.mrcontador.service.IntegracaoQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Integracao}.
 */
@RestController
@RequestMapping("/api")
public class IntegracaoResource {

    private final Logger log = LoggerFactory.getLogger(IntegracaoResource.class);

    private static final String ENTITY_NAME = "integracao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntegracaoService integracaoService;

    private final IntegracaoQueryService integracaoQueryService;

    public IntegracaoResource(IntegracaoService integracaoService, IntegracaoQueryService integracaoQueryService) {
        this.integracaoService = integracaoService;
        this.integracaoQueryService = integracaoQueryService;
    }

    /**
     * {@code POST  /integracaos} : Create a new integracao.
     *
     * @param integracao the integracao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new integracao, or with status {@code 400 (Bad Request)} if the integracao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/integracaos")
    public ResponseEntity<Integracao> createIntegracao(@Valid @RequestBody Integracao integracao) throws URISyntaxException {
        log.debug("REST request to save Integracao : {}", integracao);
        if (integracao.getId() != null) {
            throw new BadRequestAlertException("A new integracao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Integracao result = integracaoService.save(integracao);
        return ResponseEntity.created(new URI("/api/integracaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /integracaos} : Updates an existing integracao.
     *
     * @param integracao the integracao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated integracao,
     * or with status {@code 400 (Bad Request)} if the integracao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the integracao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/integracaos")
    public ResponseEntity<Integracao> updateIntegracao(@Valid @RequestBody Integracao integracao) throws URISyntaxException {
        log.debug("REST request to update Integracao : {}", integracao);
        if (integracao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Integracao result = integracaoService.save(integracao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, integracao.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /integracaos} : get all the integracaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of integracaos in body.
     */
    @GetMapping("/integracaos")
    public ResponseEntity<List<Integracao>> getAllIntegracaos(IntegracaoCriteria criteria) {
        log.debug("REST request to get Integracaos by criteria: {}", criteria);
        List<Integracao> entityList = integracaoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /integracaos/count} : count all the integracaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/integracaos/count")
    public ResponseEntity<Long> countIntegracaos(IntegracaoCriteria criteria) {
        log.debug("REST request to count Integracaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(integracaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /integracaos/:id} : get the "id" integracao.
     *
     * @param id the id of the integracao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the integracao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/integracaos/{id}")
    public ResponseEntity<Integracao> getIntegracao(@PathVariable Long id) {
        log.debug("REST request to get Integracao : {}", id);
        Optional<Integracao> integracao = integracaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(integracao);
    }

    /**
     * {@code DELETE  /integracaos/:id} : delete the "id" integracao.
     *
     * @param id the id of the integracao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/integracaos/{id}")
    public ResponseEntity<Void> deleteIntegracao(@PathVariable Long id) {
        log.debug("REST request to delete Integracao : {}", id);
        integracaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
