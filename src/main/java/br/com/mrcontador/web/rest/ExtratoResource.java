package br.com.mrcontador.web.rest;

import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.ExtratoDTO;
import br.com.mrcontador.service.dto.ExtratoCriteria;
import br.com.mrcontador.service.ExtratoQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ExtratoDTO> createExtrato(@Valid @RequestBody ExtratoDTO extratoDTO) throws URISyntaxException {
        log.debug("REST request to save Extrato : {}", extratoDTO);
        if (extratoDTO.getId() != null) {
            throw new BadRequestAlertException("A new extrato cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtratoDTO result = extratoService.save(extratoDTO);
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
    public ResponseEntity<ExtratoDTO> updateExtrato(@Valid @RequestBody ExtratoDTO extratoDTO) throws URISyntaxException {
        log.debug("REST request to update Extrato : {}", extratoDTO);
        if (extratoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExtratoDTO result = extratoService.save(extratoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extratoDTO.getId().toString()))
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
    public ResponseEntity<List<ExtratoDTO>> getAllExtratoes(ExtratoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Extratoes by criteria: {}", criteria);
        Page<ExtratoDTO> page = extratoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
    public ResponseEntity<ExtratoDTO> getExtrato(@PathVariable Long id) {
        log.debug("REST request to get Extrato : {}", id);
        Optional<ExtratoDTO> extratoDTO = extratoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extratoDTO);
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
