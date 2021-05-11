package br.com.mrcontador.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.service.ExtratoQueryService;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.ExtratoCriteria;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Extrato}.
 */
@RestController
@RequestMapping("/api")
public class ExtratoResource {

    private final Logger log = LoggerFactory.getLogger(ExtratoResource.class);

    private static final String ENTITY_NAME = "extrato";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtratoService extratoService;

    private final ExtratoQueryService extratoQueryService;

    public ExtratoResource(ExtratoService extratoService, ExtratoQueryService extratoQueryService) {
        this.extratoService = extratoService;
        this.extratoQueryService = extratoQueryService;
    }

    /**
     * {@code POST  /extratoes} : Create a new extrato.
     *
     * @param extratoDTO the extratoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extratoDTO, or with status {@code 400 (Bad Request)} if the extrato has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extratoes")
    public ResponseEntity<Extrato> createExtrato(@Valid @RequestBody Extrato extrato) throws URISyntaxException {
        log.debug("REST request to save Extrato : {}", extrato);
        if (extrato.getId() != null) {
            throw new BadRequestAlertException("A new extrato cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Extrato result = extratoService.save(extrato);
        return ResponseEntity.created(new URI("/api/extratoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extratoes} : Updates an existing extrato.
     *
     * @param extratoDTO the extratoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extratoDTO,
     * or with status {@code 400 (Bad Request)} if the extratoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extratoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extratoes")
    public ResponseEntity<Extrato> updateExtrato(@Valid @RequestBody Extrato extrato) throws URISyntaxException {
        log.debug("REST request to update Extrato : {}", extrato);
        if (extrato.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Extrato result = extratoService.save(extrato);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extrato.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /extratoes} : get all the extratoes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extratoes in body.
     */
    @GetMapping("/extratoes")
    public ResponseEntity<List<Extrato>> getAllExtratoes(ExtratoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Extratoes by criteria: {}", criteria);
        List<Extrato> page = extratoQueryService.findByCriteria(criteria, pageable.getSort());
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /extratoes/count} : count all the extratoes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/extratoes/count")
    public ResponseEntity<Long> countExtratoes(ExtratoCriteria criteria) {
        log.debug("REST request to count Extratoes by criteria: {}", criteria);
        return ResponseEntity.ok().body(extratoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /extratoes/:id} : get the "id" extrato.
     *
     * @param id the id of the extratoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extratoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extratoes/{id}")
    public ResponseEntity<Extrato> getExtrato(@PathVariable Long id) {
        log.debug("REST request to get Extrato : {}", id);
        Optional<Extrato> extrato = extratoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extrato);
    }

    /**
     * {@code DELETE  /extratoes/:id} : delete the "id" extrato.
     *
     * @param id the id of the extratoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extratoes/{id}")
    public ResponseEntity<Void> deleteExtrato(@PathVariable Long id) {
        log.debug("REST request to delete Extrato : {}", id);
        extratoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
