package br.com.mrcontador.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mrcontador.domain.Sieg;
import br.com.mrcontador.service.SiegService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.sieg}.
 */
@RestController
@RequestMapping("/api")
public class SiegResource {

    private final Logger log = LoggerFactory.getLogger(SiegResource.class);

    private static final String ENTITY_NAME = "sieg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiegService siegService;

    
    public SiegResource(SiegService siegService) {
        this.siegService = siegService;
    }

    /**
     * {@code POST  /siegs} : Create a new sieg.
     *
     * @param sieg the sieg to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sieg, or with status {@code 400 (Bad Request)} if the sieg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/siegs")
    public ResponseEntity<Sieg> createSieg(@Valid @RequestBody Sieg sieg) throws URISyntaxException {
        log.debug("REST request to save Sieg : {}", sieg);
        if (sieg.getId() != null) {
            throw new BadRequestAlertException("A new sieg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sieg result = siegService.save(sieg);
        return ResponseEntity.created(new URI("/api/siegs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /siegs} : Updates an existing sieg.
     *
     * @param sieg the sieg to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sieg,
     * or with status {@code 400 (Bad Request)} if the sieg is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sieg couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/siegs")
    public ResponseEntity<Sieg> updateSieg(@Valid @RequestBody Sieg sieg) throws URISyntaxException {
        log.debug("REST request to update Sieg : {}", sieg);
        if (sieg.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sieg result = siegService.save(sieg);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /siegs} : get all the siegs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siegs in body.
     */
    @GetMapping("/sieg")
    public ResponseEntity<Sieg> getAllSiegs() {
        log.debug("REST request to get sieg");
        Optional<Sieg> result = siegService.get();
        if(result.isEmpty()) {
        	 throw new BadRequestAlertException("Not found", ENTITY_NAME, "not found");
        }
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.get().getId().toString()))
                .body(result.get());
    }
    
    @GetMapping("/sieg/actives")
    public ResponseEntity<List<Sieg>> getSiegActives() {
        log.debug("REST request to get sieg");
        List<Sieg> result = siegService.getActives();
        if(result.isEmpty()) {
        	 throw new BadRequestAlertException("Not found", ENTITY_NAME, "not found");
        }
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, "actives"))
                .body(result);
    }

       /**
     * {@code DELETE  /siegs/:id} : delete the "id" sieg.
     *
     * @param id the id of the sieg to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/siegs/{id}")
    public ResponseEntity<Void> deleteSieg(@PathVariable Long id) {
        log.debug("REST request to delete Sieg: {}", id);
        siegService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
