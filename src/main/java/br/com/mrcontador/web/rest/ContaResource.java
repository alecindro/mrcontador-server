package br.com.mrcontador.web.rest;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mrcontador.service.ContaQueryService;
import br.com.mrcontador.service.ContaService;
import br.com.mrcontador.service.dto.ContaCriteria;
import br.com.mrcontador.service.dto.ContaDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Conta}.
 */
@RestController
@RequestMapping("/api")
public class ContaResource {

    private final Logger log = LoggerFactory.getLogger(ContaResource.class);

    private static final String ENTITY_NAME = "conta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContaService contaService;

    private final ContaQueryService contaQueryService;

    public ContaResource(ContaService contaService, ContaQueryService contaQueryService) {
        this.contaService = contaService;
        this.contaQueryService = contaQueryService;
    }


    /**
     * {@code GET  /contas} : get all the contas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contas in body.
     */
    @GetMapping("/contas")
    public ResponseEntity<List<ContaDTO>> getAllContas(ContaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Contas by criteria: {}", criteria);
        Page<ContaDTO> page = contaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contas/count} : count all the contas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contas/count")
    public ResponseEntity<Long> countContas(ContaCriteria criteria) {
        log.debug("REST request to count Contas by criteria: {}", criteria);
        return ResponseEntity.ok().body(contaQueryService.countByCriteria(criteria));
    }


    /**
     * {@code DELETE  /contas/:id} : delete the "id" conta.
     *
     * @param id the id of the contaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contas/{id}")
    public ResponseEntity<Void> deleteConta(@PathVariable Long id) {
        log.debug("REST request to delete Conta : {}", id);
        contaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
