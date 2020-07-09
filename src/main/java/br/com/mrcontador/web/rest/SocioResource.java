package br.com.mrcontador.web.rest;

import br.com.mrcontador.domain.Socio;
import br.com.mrcontador.service.SocioService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.SocioCriteria;
import br.com.mrcontador.service.SocioQueryService;

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
 * REST controller for managing {@link br.com.mrcontador.domain.Socio}.
 */
@RestController
@RequestMapping("/api")
public class SocioResource {

    private final Logger log = LoggerFactory.getLogger(SocioResource.class);

    private static final String ENTITY_NAME = "socio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocioService socioService;

    private final SocioQueryService socioQueryService;

    public SocioResource(SocioService socioService, SocioQueryService socioQueryService) {
        this.socioService = socioService;
        this.socioQueryService = socioQueryService;
    }

    /**
     * {@code POST  /socios} : Create a new socio.
     *
     * @param socio the socio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socio, or with status {@code 400 (Bad Request)} if the socio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/socios")
    public ResponseEntity<Socio> createSocio(@Valid @RequestBody Socio socio) throws URISyntaxException {
        log.debug("REST request to save Socio : {}", socio);
        if (socio.getId() != null) {
            throw new BadRequestAlertException("A new socio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Socio result = socioService.save(socio);
        return ResponseEntity.created(new URI("/api/socios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /socios} : Updates an existing socio.
     *
     * @param socio the socio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socio,
     * or with status {@code 400 (Bad Request)} if the socio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/socios")
    public ResponseEntity<Socio> updateSocio(@Valid @RequestBody Socio socio) throws URISyntaxException {
        log.debug("REST request to update Socio : {}", socio);
        if (socio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Socio result = socioService.save(socio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socio.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /socios} : get all the socios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socios in body.
     */
    @GetMapping("/socios")
    public ResponseEntity<List<Socio>> getAllSocios(SocioCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Socios by criteria: {}", criteria);
        Page<Socio> page = socioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /socios/count} : count all the socios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/socios/count")
    public ResponseEntity<Long> countSocios(SocioCriteria criteria) {
        log.debug("REST request to count Socios by criteria: {}", criteria);
        return ResponseEntity.ok().body(socioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /socios/:id} : get the "id" socio.
     *
     * @param id the id of the socio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/socios/{id}")
    public ResponseEntity<Socio> getSocio(@PathVariable Long id) {
        log.debug("REST request to get Socio : {}", id);
        Optional<Socio> socio = socioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socio);
    }

    /**
     * {@code DELETE  /socios/:id} : delete the "id" socio.
     *
     * @param id the id of the socio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/socios/{id}")
    public ResponseEntity<Void> deleteSocio(@PathVariable Long id) {
        log.debug("REST request to delete Socio : {}", id);
        socioService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
