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

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.service.AgenciabancariaQueryService;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.dto.AgenciabancariaAplicacao;
import br.com.mrcontador.service.dto.AgenciabancariaCriteria;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Agenciabancaria}.
 */
@RestController
@RequestMapping("/api")
public class AgenciabancariaResource {

    private final Logger log = LoggerFactory.getLogger(AgenciabancariaResource.class);

    private static final String ENTITY_NAME = "agenciabancaria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgenciabancariaService agenciabancariaService;

    private final AgenciabancariaQueryService agenciabancariaQueryService;

    public AgenciabancariaResource(AgenciabancariaService agenciabancariaService, AgenciabancariaQueryService agenciabancariaQueryService) {
        this.agenciabancariaService = agenciabancariaService;
        this.agenciabancariaQueryService = agenciabancariaQueryService;
    }

    /**
     * {@code POST  /agenciabancarias} : Create a new agenciabancaria.
     *
     * @param agenciabancaria the agenciabancaria to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agenciabancaria, or with status {@code 400 (Bad Request)} if the agenciabancaria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agenciabancarias")
    public ResponseEntity<Agenciabancaria> createAgenciabancaria(@Valid @RequestBody Agenciabancaria agenciabancaria) throws URISyntaxException {
        log.debug("REST request to save Agenciabancaria : {}", agenciabancaria);
        if (agenciabancaria.getId() != null) {
            throw new BadRequestAlertException("A new agenciabancaria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agenciabancaria.setAgeSituacao(true);
        Agenciabancaria result = agenciabancariaService.save(agenciabancaria);
        return ResponseEntity.created(new URI("/api/agenciabancarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    @PostMapping("/agenciabancarias/aplicacao")
    public ResponseEntity<Agenciabancaria> createAgenciaAplicacao(@Valid @RequestBody AgenciabancariaAplicacao agenciabancaria) throws URISyntaxException {
        log.debug("REST request to save Agenciabancaria : {}", agenciabancaria);
        Agenciabancaria result = agenciabancariaService.createAplicacao(agenciabancaria);
        return ResponseEntity.created(new URI("/api/agenciabancarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * {@code PUT  /agenciabancarias} : Updates an existing agenciabancaria.
     *
     * @param agenciabancaria the agenciabancaria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agenciabancaria,
     * or with status {@code 400 (Bad Request)} if the agenciabancaria is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agenciabancaria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agenciabancarias")
    public ResponseEntity<Agenciabancaria> updateAgenciabancaria(@Valid @RequestBody Agenciabancaria agenciabancaria) throws URISyntaxException {
        log.debug("REST request to update Agenciabancaria : {}", agenciabancaria);
        if (agenciabancaria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Agenciabancaria result = agenciabancariaService.update(agenciabancaria);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agenciabancaria.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agenciabancarias} : get all the agenciabancarias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agenciabancarias in body.
     */
    @GetMapping("/agenciabancarias")
    public ResponseEntity<List<Agenciabancaria>> getAllAgenciabancarias(AgenciabancariaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Agenciabancarias by criteria: {}", criteria);
        Page<Agenciabancaria> page = agenciabancariaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agenciabancarias/count} : count all the agenciabancarias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/agenciabancarias/count")
    public ResponseEntity<Long> countAgenciabancarias(AgenciabancariaCriteria criteria) {
        log.debug("REST request to count Agenciabancarias by criteria: {}", criteria);
        return ResponseEntity.ok().body(agenciabancariaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agenciabancarias/:id} : get the "id" agenciabancaria.
     *
     * @param id the id of the agenciabancaria to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agenciabancaria, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agenciabancarias/{id}")
    public ResponseEntity<Agenciabancaria> getAgenciabancaria(@PathVariable Long id) {
        log.debug("REST request to get Agenciabancaria : {}", id);
        Optional<Agenciabancaria> agenciabancaria = agenciabancariaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agenciabancaria);
    }

    /**
     * {@code DELETE  /agenciabancarias/:id} : delete the "id" agenciabancaria.
     *
     * @param id the id of the agenciabancaria to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agenciabancarias/{id}")
    public ResponseEntity<Void> deleteAgenciabancaria(@PathVariable Long id) {
        log.debug("REST request to delete Agenciabancaria : {}", id);
        agenciabancariaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
