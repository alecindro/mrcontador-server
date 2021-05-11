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

import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.service.ComprovanteQueryService;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.dto.ComprovanteCriteria;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Comprovante}.
 */
@RestController
@RequestMapping("/api")
public class ComprovanteResource {

    private final Logger log = LoggerFactory.getLogger(ComprovanteResource.class);

    private static final String ENTITY_NAME = "comprovante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComprovanteService comprovanteService;

    private final ComprovanteQueryService comprovanteQueryService;

    public ComprovanteResource(ComprovanteService comprovanteService, ComprovanteQueryService comprovanteQueryService) {
        this.comprovanteService = comprovanteService;
        this.comprovanteQueryService = comprovanteQueryService;
    }

    /**
     * {@code POST  /comprovantes} : Create a new comprovante.
     *
     * @param comprovanteDTO the comprovanteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comprovanteDTO, or with status {@code 400 (Bad Request)} if the comprovante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comprovantes")
    public ResponseEntity<Comprovante> createComprovante(@RequestBody Comprovante comprovante) throws URISyntaxException {
        log.debug("REST request to save Comprovante : {}", comprovante);
        if (comprovante.getId() != null) {
            throw new BadRequestAlertException("A new comprovante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Comprovante result = comprovanteService.save(comprovante);
        return ResponseEntity.created(new URI("/api/comprovantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comprovantes} : Updates an existing comprovante.
     *
     * @param comprovanteDTO the comprovanteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comprovanteDTO,
     * or with status {@code 400 (Bad Request)} if the comprovanteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comprovanteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comprovantes")
    public ResponseEntity<Comprovante> updateComprovante(@RequestBody Comprovante comprovante) throws URISyntaxException {
        log.debug("REST request to update Comprovante : {}", comprovante);
        if (comprovante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Comprovante result = comprovanteService.save(comprovante);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comprovante.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /comprovantes} : get all the comprovantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comprovantes in body.
     */
    @GetMapping("/comprovantes")
    public ResponseEntity<List<Comprovante>> getAllComprovantes(ComprovanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Comprovantes by criteria: {}", criteria);
        List<Comprovante> page = comprovanteQueryService.findByCriteria(criteria, pageable.getSort());
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /comprovantes/count} : count all the comprovantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/comprovantes/count")
    public ResponseEntity<Long> countComprovantes(ComprovanteCriteria criteria) {
        log.debug("REST request to count Comprovantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(comprovanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /comprovantes/:id} : get the "id" comprovante.
     *
     * @param id the id of the comprovanteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comprovanteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comprovantes/{id}")
    public ResponseEntity<Comprovante> getComprovante(@PathVariable Long id) {
        log.debug("REST request to get Comprovante : {}", id);
        Optional<Comprovante> comprovante = comprovanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comprovante);
    }

    /**
     * {@code DELETE  /comprovantes/:id} : delete the "id" comprovante.
     *
     * @param id the id of the comprovanteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comprovantes/{id}")
    public ResponseEntity<Void> deleteComprovante(@PathVariable Long id) {
        log.debug("REST request to delete Comprovante : {}", id);
        comprovanteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
