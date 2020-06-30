package br.com.mrcontador.web.rest;

import br.com.mrcontador.service.NotaservicoService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.NotaservicoDTO;
import br.com.mrcontador.service.dto.NotaservicoCriteria;
import br.com.mrcontador.service.NotaservicoQueryService;

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
 * REST controller for managing {@link br.com.mrcontador.domain.Notaservico}.
 */
@RestController
@RequestMapping("/api")
public class NotaservicoResource {

    private final Logger log = LoggerFactory.getLogger(NotaservicoResource.class);

    private static final String ENTITY_NAME = "notaservico";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotaservicoService notaservicoService;

    private final NotaservicoQueryService notaservicoQueryService;

    public NotaservicoResource(NotaservicoService notaservicoService, NotaservicoQueryService notaservicoQueryService) {
        this.notaservicoService = notaservicoService;
        this.notaservicoQueryService = notaservicoQueryService;
    }

    /**
     * {@code POST  /notaservicos} : Create a new notaservico.
     *
     * @param notaservicoDTO the notaservicoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notaservicoDTO, or with status {@code 400 (Bad Request)} if the notaservico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notaservicos")
    public ResponseEntity<NotaservicoDTO> createNotaservico(@Valid @RequestBody NotaservicoDTO notaservicoDTO) throws URISyntaxException {
        log.debug("REST request to save Notaservico : {}", notaservicoDTO);
        if (notaservicoDTO.getId() != null) {
            throw new BadRequestAlertException("A new notaservico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotaservicoDTO result = notaservicoService.save(notaservicoDTO);
        return ResponseEntity.created(new URI("/api/notaservicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notaservicos} : Updates an existing notaservico.
     *
     * @param notaservicoDTO the notaservicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notaservicoDTO,
     * or with status {@code 400 (Bad Request)} if the notaservicoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notaservicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notaservicos")
    public ResponseEntity<NotaservicoDTO> updateNotaservico(@Valid @RequestBody NotaservicoDTO notaservicoDTO) throws URISyntaxException {
        log.debug("REST request to update Notaservico : {}", notaservicoDTO);
        if (notaservicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotaservicoDTO result = notaservicoService.save(notaservicoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notaservicoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notaservicos} : get all the notaservicos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notaservicos in body.
     */
    @GetMapping("/notaservicos")
    public ResponseEntity<List<NotaservicoDTO>> getAllNotaservicos(NotaservicoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Notaservicos by criteria: {}", criteria);
        Page<NotaservicoDTO> page = notaservicoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notaservicos/count} : count all the notaservicos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/notaservicos/count")
    public ResponseEntity<Long> countNotaservicos(NotaservicoCriteria criteria) {
        log.debug("REST request to count Notaservicos by criteria: {}", criteria);
        return ResponseEntity.ok().body(notaservicoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notaservicos/:id} : get the "id" notaservico.
     *
     * @param id the id of the notaservicoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notaservicoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notaservicos/{id}")
    public ResponseEntity<NotaservicoDTO> getNotaservico(@PathVariable Long id) {
        log.debug("REST request to get Notaservico : {}", id);
        Optional<NotaservicoDTO> notaservicoDTO = notaservicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notaservicoDTO);
    }

    /**
     * {@code DELETE  /notaservicos/:id} : delete the "id" notaservico.
     *
     * @param id the id of the notaservicoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notaservicos/{id}")
    public ResponseEntity<Void> deleteNotaservico(@PathVariable Long id) {
        log.debug("REST request to delete Notaservico : {}", id);
        notaservicoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
