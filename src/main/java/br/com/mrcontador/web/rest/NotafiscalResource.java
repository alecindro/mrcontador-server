package br.com.mrcontador.web.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.service.NotafiscalQueryService;
import br.com.mrcontador.service.NotafiscalService;
import br.com.mrcontador.service.dto.NotafiscalCriteria;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Notafiscal}.
 */
@RestController
@RequestMapping("/api")
public class NotafiscalResource {

    private final Logger log = LoggerFactory.getLogger(NotafiscalResource.class);

    private static final String ENTITY_NAME = "notafiscal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotafiscalService notafiscalService;

    private final NotafiscalQueryService notafiscalQueryService;

    public NotafiscalResource(NotafiscalService notafiscalService, NotafiscalQueryService notafiscalQueryService) {
        this.notafiscalService = notafiscalService;
        this.notafiscalQueryService = notafiscalQueryService;
    }

    /**
     * {@code POST  /notafiscals} : Create a new notafiscal.
     *
     * @param notafiscalDTO the notafiscalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notafiscalDTO, or with status {@code 400 (Bad Request)} if the notafiscal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notafiscals")
    public ResponseEntity<Notafiscal> createNotafiscal(@Valid @RequestBody Notafiscal notafiscal) throws URISyntaxException {
        log.debug("REST request to save Notafiscal : {}", notafiscal);
        if (notafiscal.getId() != null) {
            throw new BadRequestAlertException("A new notafiscal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notafiscal result = notafiscalService.save(notafiscal);
        return ResponseEntity.created(new URI("/api/notafiscals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notafiscals} : Updates an existing notafiscal.
     *
     * @param notafiscalDTO the notafiscalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notafiscalDTO,
     * or with status {@code 400 (Bad Request)} if the notafiscalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notafiscalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notafiscals")
    public ResponseEntity<Notafiscal> updateNotafiscal(@Valid @RequestBody Notafiscal notafiscal) throws URISyntaxException {
        log.debug("REST request to update Notafiscal : {}", notafiscal);
        if (notafiscal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Notafiscal result = notafiscalService.save(notafiscal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notafiscal.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notafiscals} : get all the notafiscals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notafiscals in body.
     */
    @GetMapping("/notafiscals")
    public ResponseEntity<List<Notafiscal>> getAllNotafiscals(NotafiscalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Notafiscals by criteria: {}", criteria);
        Page<Notafiscal> page = notafiscalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    @GetMapping("/notafiscals/near")
    public ResponseEntity<List<Notafiscal>> findNotafiscals(@RequestParam("cnpj") Long cnpj, @RequestParam("valor") BigDecimal valor, @RequestParam("dataInicial") LocalDate dataInicial) {
    	List<Notafiscal> notas = notafiscalService.find(String.valueOf(cnpj), valor, dataInicial);
        return ResponseEntity.ok().body(notas);
    }

    /**
     * {@code GET  /notafiscals/count} : count all the notafiscals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/notafiscals/count")
    public ResponseEntity<Long> countNotafiscals(NotafiscalCriteria criteria) {
        log.debug("REST request to count Notafiscals by criteria: {}", criteria);
        return ResponseEntity.ok().body(notafiscalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notafiscals/:id} : get the "id" notafiscal.
     *
     * @param id the id of the notafiscalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notafiscalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notafiscals/{id}")
    public ResponseEntity<Notafiscal> getNotafiscal(@PathVariable Long id) {
        log.debug("REST request to get Notafiscal : {}", id);
        Optional<Notafiscal> notafiscal = notafiscalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notafiscal);
    }

    /**
     * {@code DELETE  /notafiscals/:id} : delete the "id" notafiscal.
     *
     * @param id the id of the notafiscalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notafiscals/{id}")
    public ResponseEntity<Void> deleteNotafiscal(@PathVariable Long id) {
        log.debug("REST request to delete Notafiscal : {}", id);
        notafiscalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
