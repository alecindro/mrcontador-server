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

import br.com.mrcontador.domain.Atividade;
import br.com.mrcontador.service.AtividadeQueryService;
import br.com.mrcontador.service.AtividadeService;
import br.com.mrcontador.service.dto.AtividadeCriteria;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Atividade}.
 */
@RestController
@RequestMapping("/api")
public class AtividadeResource {

    private final Logger log = LoggerFactory.getLogger(AtividadeResource.class);

    private static final String ENTITY_NAME = "atividade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AtividadeService atividadeService;

    private final AtividadeQueryService atividadeQueryService;

    public AtividadeResource(AtividadeService atividadeService, AtividadeQueryService atividadeQueryService) {
        this.atividadeService = atividadeService;
        this.atividadeQueryService = atividadeQueryService;
    }

    /**
     * {@code POST  /atividades} : Create a new atividade.
     *
     * @param atividade the atividade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new atividade, or with status {@code 400 (Bad Request)} if the atividade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/atividades")
    public ResponseEntity<Atividade> createAtividade(@Valid @RequestBody Atividade atividade) throws URISyntaxException {
        log.debug("REST request to save Atividade : {}", atividade);
        if (atividade.getId() != null) {
            throw new BadRequestAlertException("A new atividade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Atividade result = atividadeService.save(atividade);
        return ResponseEntity.created(new URI("/api/atividades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /atividades} : Updates an existing atividade.
     *
     * @param atividade the atividade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atividade,
     * or with status {@code 400 (Bad Request)} if the atividade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the atividade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/atividades")
    public ResponseEntity<Atividade> updateAtividade(@Valid @RequestBody Atividade atividade) throws URISyntaxException {
        log.debug("REST request to update Atividade : {}", atividade);
        if (atividade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Atividade result = atividadeService.save(atividade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, atividade.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /atividades} : get all the atividades.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of atividades in body.
     */
    @GetMapping("/atividades")
    public ResponseEntity<List<Atividade>> getAllAtividades(AtividadeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Atividades by criteria: {}", criteria);
        Page<Atividade> page = atividadeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /atividades/count} : count all the atividades.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/atividades/count")
    public ResponseEntity<Long> countAtividades(AtividadeCriteria criteria) {
        log.debug("REST request to count Atividades by criteria: {}", criteria);
        return ResponseEntity.ok().body(atividadeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /atividades/:id} : get the "id" atividade.
     *
     * @param id the id of the atividade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the atividade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/atividades/{id}")
    public ResponseEntity<Atividade> getAtividade(@PathVariable Long id) {
        log.debug("REST request to get Atividade : {}", id);
        Optional<Atividade> atividade = atividadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(atividade);
    }

    /**
     * {@code DELETE  /atividades/:id} : delete the "id" atividade.
     *
     * @param id the id of the atividade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/atividades/{id}")
    public ResponseEntity<Void> deleteAtividade(@PathVariable Long id) {
        log.debug("REST request to delete Atividade : {}", id);
        atividadeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
