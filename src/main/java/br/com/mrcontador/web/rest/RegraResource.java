package br.com.mrcontador.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mrcontador.domain.Regra;
import br.com.mrcontador.service.RegraQueryService;
import br.com.mrcontador.service.RegraService;
import br.com.mrcontador.service.dto.RegraCriteria;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Regra}.
 */
@RestController
@RequestMapping("/api")
public class RegraResource {

    private final Logger log = LoggerFactory.getLogger(RegraResource.class);

    private static final String ENTITY_NAME = "regra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegraService regraService;

    private final RegraQueryService regraQueryService;

    public RegraResource(RegraService regraService, RegraQueryService regraQueryService) {
        this.regraService = regraService;
        this.regraQueryService = regraQueryService;
    }

    /**
     * {@code POST  /regras} : Create a new regra.
     *
     * @param Regra the Regra to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new Regra, or with status {@code 400 (Bad Request)} if the regra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/regras")
    public ResponseEntity<Regra> createRegra(@RequestBody Regra regra) throws URISyntaxException {
        log.debug("REST request to save Regra : {}", regra);
        if (regra.getId() != null) {
            throw new BadRequestAlertException("A new regra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        regra = regraService.save(regra);
        return ResponseEntity.created(new URI("/api/regras/" + regra.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, regra.getId().toString()))
            .body(regra);
    }

    /**
     * {@code PUT  /regras} : Updates an existing regra.
     *
     * @param Regra the Regra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated Regra,
     * or with status {@code 400 (Bad Request)} if the Regra is not valid,
     * or with status {@code 500 (Internal Server Error)} if the Regra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/regras")
    public ResponseEntity<Regra> updateRegra(@RequestBody Regra regra) throws URISyntaxException {
        log.debug("REST request to update Regra : {}", regra);
        if (regra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        regra = regraService.save(regra);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, regra.getId().toString()))
            .body(regra);
    }

    /**
     * {@code GET  /regras} : get all the regras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regras in body.
     */
    @GetMapping("/regras")
    public ResponseEntity<List<Regra>> getAllRegras(RegraCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Regras by criteria: {}", criteria);
        Page<Regra> page = regraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /regras/count} : count all the regras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/regras/count")
    public ResponseEntity<Long> countRegras(RegraCriteria criteria) {
        log.debug("REST request to count Regras by criteria: {}", criteria);
        return ResponseEntity.ok().body(regraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /regras/:id} : get the "id" regra.
     *
     * @param id the id of the Regra to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Regra, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/regras/{id}")
    public ResponseEntity<Regra> getRegra(@PathVariable Long id) {
        log.debug("REST request to get Regra : {}", id);
        Optional<Regra> Regra = regraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Regra);
    }

    /**
     * {@code DELETE  /regras/:id} : delete the "id" regra.
     *
     * @param id the id of the Regra to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/regras/{id}")
    public ResponseEntity<Void> deleteRegra(@PathVariable Long id) {
        log.debug("REST request to delete Regra : {}", id);
        regraService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
