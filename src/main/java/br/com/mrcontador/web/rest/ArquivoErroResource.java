package br.com.mrcontador.web.rest;

import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.service.ArquivoErroService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.ArquivoErroCriteria;
import br.com.mrcontador.service.ArquivoErroQueryService;

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
 * REST controller for managing {@link br.com.mrcontador.domain.ArquivoErro}.
 */
@RestController
@RequestMapping("/api")
public class ArquivoErroResource {

    private final Logger log = LoggerFactory.getLogger(ArquivoErroResource.class);

    private static final String ENTITY_NAME = "arquivoErro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArquivoErroService arquivoErroService;

    private final ArquivoErroQueryService arquivoErroQueryService;

    public ArquivoErroResource(ArquivoErroService arquivoErroService, ArquivoErroQueryService arquivoErroQueryService) {
        this.arquivoErroService = arquivoErroService;
        this.arquivoErroQueryService = arquivoErroQueryService;
    }

    /**
     * {@code POST  /arquivo-erros} : Create a new arquivoErro.
     *
     * @param arquivoErro the arquivoErro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arquivoErro, or with status {@code 400 (Bad Request)} if the arquivoErro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arquivo-erros")
    public ResponseEntity<ArquivoErro> createArquivoErro(@RequestBody ArquivoErro arquivoErro) throws URISyntaxException {
        log.debug("REST request to save ArquivoErro : {}", arquivoErro);
        if (arquivoErro.getId() != null) {
            throw new BadRequestAlertException("A new arquivoErro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArquivoErro result = arquivoErroService.save(arquivoErro);
        return ResponseEntity.created(new URI("/api/arquivo-erros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arquivo-erros} : Updates an existing arquivoErro.
     *
     * @param arquivoErro the arquivoErro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoErro,
     * or with status {@code 400 (Bad Request)} if the arquivoErro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arquivoErro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arquivo-erros")
    public ResponseEntity<ArquivoErro> updateArquivoErro(@RequestBody ArquivoErro arquivoErro) throws URISyntaxException {
        log.debug("REST request to update ArquivoErro : {}", arquivoErro);
        if (arquivoErro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArquivoErro result = arquivoErroService.save(arquivoErro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoErro.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arquivo-erros} : get all the arquivoErros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arquivoErros in body.
     */
    @GetMapping("/arquivo-erros")
    public ResponseEntity<List<ArquivoErro>> getAllArquivoErros(ArquivoErroCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ArquivoErros by criteria: {}", criteria);
        Page<ArquivoErro> page = arquivoErroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arquivo-erros/count} : count all the arquivoErros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/arquivo-erros/count")
    public ResponseEntity<Long> countArquivoErros(ArquivoErroCriteria criteria) {
        log.debug("REST request to count ArquivoErros by criteria: {}", criteria);
        return ResponseEntity.ok().body(arquivoErroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arquivo-erros/:id} : get the "id" arquivoErro.
     *
     * @param id the id of the arquivoErro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arquivoErro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arquivo-erros/{id}")
    public ResponseEntity<ArquivoErro> getArquivoErro(@PathVariable Long id) {
        log.debug("REST request to get ArquivoErro : {}", id);
        Optional<ArquivoErro> arquivoErro = arquivoErroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arquivoErro);
    }

    /**
     * {@code DELETE  /arquivo-erros/:id} : delete the "id" arquivoErro.
     *
     * @param id the id of the arquivoErro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arquivo-erros/{id}")
    public ResponseEntity<Void> deleteArquivoErro(@PathVariable Long id) {
        log.debug("REST request to delete ArquivoErro : {}", id);
        arquivoErroService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
