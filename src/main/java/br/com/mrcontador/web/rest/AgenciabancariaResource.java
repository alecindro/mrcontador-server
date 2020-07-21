package br.com.mrcontador.web.rest;

import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.AgenciabancariaDTO;
import br.com.mrcontador.service.dto.AgenciabancariaCriteria;
import br.com.mrcontador.service.AgenciabancariaQueryService;

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
     * @param agenciabancariaDTO the agenciabancariaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agenciabancariaDTO, or with status {@code 400 (Bad Request)} if the agenciabancaria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agenciabancarias")
    public ResponseEntity<AgenciabancariaDTO> createAgenciabancaria(@Valid @RequestBody AgenciabancariaDTO agenciabancariaDTO) throws URISyntaxException {
        log.debug("REST request to save Agenciabancaria : {}", agenciabancariaDTO);
        if (agenciabancariaDTO.getId() != null) {
            throw new BadRequestAlertException("A new agenciabancaria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgenciabancariaDTO result = agenciabancariaService.save(agenciabancariaDTO);
        return ResponseEntity.created(new URI("/api/agenciabancarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agenciabancarias} : Updates an existing agenciabancaria.
     *
     * @param agenciabancariaDTO the agenciabancariaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agenciabancariaDTO,
     * or with status {@code 400 (Bad Request)} if the agenciabancariaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agenciabancariaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agenciabancarias")
    public ResponseEntity<AgenciabancariaDTO> updateAgenciabancaria(@Valid @RequestBody AgenciabancariaDTO agenciabancariaDTO) throws URISyntaxException {
        log.debug("REST request to update Agenciabancaria : {}", agenciabancariaDTO);
        if (agenciabancariaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgenciabancariaDTO result = agenciabancariaService.save(agenciabancariaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agenciabancariaDTO.getId().toString()))
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
    public ResponseEntity<List<AgenciabancariaDTO>> getAllAgenciabancarias(AgenciabancariaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Agenciabancarias by criteria: {}", criteria);
        Page<AgenciabancariaDTO> page = agenciabancariaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the agenciabancariaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agenciabancariaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agenciabancarias/{id}")
    public ResponseEntity<AgenciabancariaDTO> getAgenciabancaria(@PathVariable Long id) {
        log.debug("REST request to get Agenciabancaria : {}", id);
        Optional<AgenciabancariaDTO> agenciabancariaDTO = agenciabancariaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agenciabancariaDTO);
    }

    /**
     * {@code DELETE  /agenciabancarias/:id} : delete the "id" agenciabancaria.
     *
     * @param id the id of the agenciabancariaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agenciabancarias/{id}")
    public ResponseEntity<Void> deleteAgenciabancaria(@PathVariable Long id) {
        log.debug("REST request to delete Agenciabancaria : {}", id);
        agenciabancariaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
