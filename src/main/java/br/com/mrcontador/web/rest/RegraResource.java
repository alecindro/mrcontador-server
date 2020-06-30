package br.com.mrcontador.web.rest;

import br.com.mrcontador.service.RegraService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.RegraDTO;
import br.com.mrcontador.service.dto.RegraCriteria;
import br.com.mrcontador.service.RegraQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
     * @param regraDTO the regraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new regraDTO, or with status {@code 400 (Bad Request)} if the regra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/regras")
    public ResponseEntity<RegraDTO> createRegra(@RequestBody RegraDTO regraDTO) throws URISyntaxException {
        log.debug("REST request to save Regra : {}", regraDTO);
        if (regraDTO.getId() != null) {
            throw new BadRequestAlertException("A new regra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegraDTO result = regraService.save(regraDTO);
        return ResponseEntity.created(new URI("/api/regras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /regras} : Updates an existing regra.
     *
     * @param regraDTO the regraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regraDTO,
     * or with status {@code 400 (Bad Request)} if the regraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/regras")
    public ResponseEntity<RegraDTO> updateRegra(@RequestBody RegraDTO regraDTO) throws URISyntaxException {
        log.debug("REST request to update Regra : {}", regraDTO);
        if (regraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegraDTO result = regraService.save(regraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, regraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /regras} : get all the regras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regras in body.
     */
    @GetMapping("/regras")
    public ResponseEntity<List<RegraDTO>> getAllRegras(RegraCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Regras by criteria: {}", criteria);
        Page<RegraDTO> page = regraQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the regraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the regraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/regras/{id}")
    public ResponseEntity<RegraDTO> getRegra(@PathVariable Long id) {
        log.debug("REST request to get Regra : {}", id);
        Optional<RegraDTO> regraDTO = regraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regraDTO);
    }

    /**
     * {@code DELETE  /regras/:id} : delete the "id" regra.
     *
     * @param id the id of the regraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/regras/{id}")
    public ResponseEntity<Void> deleteRegra(@PathVariable Long id) {
        log.debug("REST request to delete Regra : {}", id);
        regraService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
