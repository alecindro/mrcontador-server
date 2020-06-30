package br.com.mrcontador.web.rest;

import br.com.mrcontador.service.BancoService;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.service.dto.BancoDTO;
import br.com.mrcontador.service.dto.BancoCriteria;
import br.com.mrcontador.service.BancoQueryService;

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
 * REST controller for managing {@link br.com.mrcontador.domain.Banco}.
 */
@RestController
@RequestMapping("/api")
public class BancoResource {

    private final Logger log = LoggerFactory.getLogger(BancoResource.class);

    private static final String ENTITY_NAME = "banco";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BancoService bancoService;

    private final BancoQueryService bancoQueryService;

    public BancoResource(BancoService bancoService, BancoQueryService bancoQueryService) {
        this.bancoService = bancoService;
        this.bancoQueryService = bancoQueryService;
    }

    /**
     * {@code POST  /bancos} : Create a new banco.
     *
     * @param bancoDTO the bancoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bancoDTO, or with status {@code 400 (Bad Request)} if the banco has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bancos")
    public ResponseEntity<BancoDTO> createBanco(@Valid @RequestBody BancoDTO bancoDTO) throws URISyntaxException {
        log.debug("REST request to save Banco : {}", bancoDTO);
        if (bancoDTO.getId() != null) {
            throw new BadRequestAlertException("A new banco cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BancoDTO result = bancoService.save(bancoDTO);
        return ResponseEntity.created(new URI("/api/bancos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bancos} : Updates an existing banco.
     *
     * @param bancoDTO the bancoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bancoDTO,
     * or with status {@code 400 (Bad Request)} if the bancoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bancoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bancos")
    public ResponseEntity<BancoDTO> updateBanco(@Valid @RequestBody BancoDTO bancoDTO) throws URISyntaxException {
        log.debug("REST request to update Banco : {}", bancoDTO);
        if (bancoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BancoDTO result = bancoService.save(bancoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bancoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bancos} : get all the bancos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bancos in body.
     */
    @GetMapping("/bancos")
    public ResponseEntity<List<BancoDTO>> getAllBancos(BancoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Bancos by criteria: {}", criteria);
        Page<BancoDTO> page = bancoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bancos/count} : count all the bancos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bancos/count")
    public ResponseEntity<Long> countBancos(BancoCriteria criteria) {
        log.debug("REST request to count Bancos by criteria: {}", criteria);
        return ResponseEntity.ok().body(bancoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bancos/:id} : get the "id" banco.
     *
     * @param id the id of the bancoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bancoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bancos/{id}")
    public ResponseEntity<BancoDTO> getBanco(@PathVariable Long id) {
        log.debug("REST request to get Banco : {}", id);
        Optional<BancoDTO> bancoDTO = bancoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bancoDTO);
    }

    /**
     * {@code DELETE  /bancos/:id} : delete the "id" banco.
     *
     * @param id the id of the bancoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bancos/{id}")
    public ResponseEntity<Void> deleteBanco(@PathVariable Long id) {
        log.debug("REST request to delete Banco : {}", id);
        bancoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
