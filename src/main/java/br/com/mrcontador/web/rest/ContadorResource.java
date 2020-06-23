package br.com.mrcontador.web.rest;

import br.com.mrcontador.service.ContadorService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.ContadorDTO;
import br.com.mrcontador.service.dto.ContadorCriteria;
import br.com.mrcontador.service.ContadorQueryService;

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
 * REST controller for managing {@link br.com.mrcontador.domain.Contador}.
 */
@RestController
@RequestMapping("/api")
public class ContadorResource {

    private final Logger log = LoggerFactory.getLogger(ContadorResource.class);

    private static final String ENTITY_NAME = "contador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContadorService contadorService;

    private final ContadorQueryService contadorQueryService;

    public ContadorResource(ContadorService contadorService, ContadorQueryService contadorQueryService) {
        this.contadorService = contadorService;
        this.contadorQueryService = contadorQueryService;
    }

    /**
     * {@code POST  /contadors} : Create a new contador.
     *
     * @param contadorDTO the contadorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contadorDTO, or with status {@code 400 (Bad Request)} if the contador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contadors")
    public ResponseEntity<ContadorDTO> createContador(@RequestBody ContadorDTO contadorDTO) throws URISyntaxException {
        log.debug("REST request to save Contador : {}", contadorDTO);
        if (contadorDTO.getId() != null) {
            throw new BadRequestAlertException("A new contador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContadorDTO result = null;
		try {
			result = contadorService.save(contadorDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ResponseEntity.created(new URI("/api/contadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contadors} : Updates an existing contador.
     *
     * @param contadorDTO the contadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contadorDTO,
     * or with status {@code 400 (Bad Request)} if the contadorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contadors")
    public ResponseEntity<ContadorDTO> updateContador(@RequestBody ContadorDTO contadorDTO) throws URISyntaxException {
        log.debug("REST request to update Contador : {}", contadorDTO);
        if (contadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContadorDTO result = contadorService.update(contadorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contadorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contadors} : get all the contadors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contadors in body.
     */
    @GetMapping("/contadors")
    public ResponseEntity<List<ContadorDTO>> getAllContadors(ContadorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Contadors by criteria: {}", criteria);
        Page<ContadorDTO> page = contadorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contadors/count} : count all the contadors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contadors/count")
    public ResponseEntity<Long> countContadors(ContadorCriteria criteria) {
        log.debug("REST request to count Contadors by criteria: {}", criteria);
        return ResponseEntity.ok().body(contadorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contadors/:id} : get the "id" contador.
     *
     * @param id the id of the contadorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contadorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contadors/{id}")
    public ResponseEntity<ContadorDTO> getContador(@PathVariable Long id) {
        log.debug("REST request to get Contador : {}", id);
        Optional<ContadorDTO> contadorDTO = contadorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contadorDTO);
    }

    /**
     * {@code DELETE  /contadors/:id} : delete the "id" contador.
     *
     * @param id the id of the contadorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contadors/{id}")
    public ResponseEntity<Void> deleteContador(@PathVariable Long id) {
        log.debug("REST request to delete Contador : {}", id);
        contadorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
