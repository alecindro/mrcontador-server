package br.com.mrcontador.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
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

import br.com.mrcontador.client.CnpjClient;
import br.com.mrcontador.client.dto.PessoaJuridica;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.service.ParceiroQueryService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.ParceiroCriteria;
import br.com.mrcontador.service.mapper.ParceiroPJMapper;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Parceiro}.
 */
@RestController
@RequestMapping("/api")
public class ParceiroResource {

	private final Logger log = LoggerFactory.getLogger(ParceiroResource.class);

	private static final String ENTITY_NAME = "parceiro";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final ParceiroService parceiroService;

	private final CnpjClient cnpjClient;

	private final ParceiroQueryService parceiroQueryService;

	public ParceiroResource(ParceiroService parceiroService, ParceiroQueryService parceiroQueryService,
			CnpjClient cnpjClient) {
		this.parceiroService = parceiroService;
		this.parceiroQueryService = parceiroQueryService;
		this.cnpjClient = cnpjClient;
	}

	/**
	 * {@code POST  /parceiros} : Create a new parceiro.
	 *
	 * @param parceiroDTO the parceiroDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new parceiroDTO, or with status {@code 400 (Bad Request)} if
	 *         the parceiro has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/parceiros")
	public ResponseEntity<Parceiro> createParceiro(@Valid @RequestBody Parceiro parceiro)
			throws URISyntaxException {
		log.debug("REST request to save Parceiro : {}", parceiro);
		Parceiro result = parceiroService.save(parceiro);
		return ResponseEntity
				.created(new URI("/api/parceiros/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /parceiros} : Updates an existing parceiro.
	 *
	 * @param parceiroDTO the parceiroDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated parceiroDTO, or with status {@code 400 (Bad Request)} if
	 *         the parceiroDTO is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the parceiroDTO couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/parceiros")
	public ResponseEntity<Parceiro> updateParceiro(@Valid @RequestBody Parceiro parceiro)
			throws URISyntaxException {
		log.debug("REST request to update Parceiro : {}", parceiro);
		if (parceiro.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Parceiro result = parceiroService.update(parceiro);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parceiro.getParRazaosocial()))
				.body(result);
	}

	/**
	 * {@code GET  /parceiros} : get all the parceiros.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of parceiros in body.
	 */
	@GetMapping("/parceiros")
	public ResponseEntity<List<Parceiro>> getAllParceiros(ParceiroCriteria criteria, Pageable pageable) {
		log.debug("REST request to get Parceiros by criteria: {}", criteria);
		Page<Parceiro> page = parceiroQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}
	
	@GetMapping("/parceiros/all")
	public ResponseEntity<List<Parceiro>> getAllParceiros() {
		return ResponseEntity.ok().body(parceiroService.findAll());
	}
	
	@GetMapping("/parceiros/active/sieg")
	public ResponseEntity<List<Parceiro>> getActiveSiegParceiros() {
		return ResponseEntity.ok().body(parceiroService.findAll());
	}

	@GetMapping("/parceiros/cnpj")
	public ResponseEntity<Parceiro> getParceiroByCnpj(@RequestParam String cnpj) {
		Optional<Parceiro> _parceiro = parceiroService.findByParCnpjcpf(cnpj);
		Parceiro parceiro = null;
		if (_parceiro.isEmpty()) {
			PessoaJuridica pj = cnpjClient.fromRecitaWs(cnpj);
			ParceiroPJMapper mapper = new ParceiroPJMapper();
			parceiro = mapper.toEntity(pj);
		} else {
			if(_parceiro.get().isEnabled()) {
				throw new BadRequestAlertException("Parceiro já cadastrado", ENTITY_NAME, "parceiroexists");
			}
			parceiro = _parceiro.get();
			parceiro.setEnabled(true);
		}
		parceiro = parceiroService.save(parceiro);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, parceiro.getParRazaosocial().toString()))
				.body(parceiro);
	}

	/**
	 * {@code GET  /parceiros/count} : count all the parceiros.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/parceiros/count")
	public ResponseEntity<Long> countParceiros(ParceiroCriteria criteria) {
		log.debug("REST request to count Parceiros by criteria: {}", criteria);
		return ResponseEntity.ok().body(parceiroQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /parceiros/:id} : get the "id" parceiro.
	 *
	 * @param id the id of the parceiroDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the parceiroDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/parceiros/{id}")
	public ResponseEntity<Parceiro> getParceiro(@PathVariable Long id) {
		log.debug("REST request to get Parceiro : {}", id);
		//ParceiroCriteria criteria = new ParceiroCriteria();
		//LongFilter filter = new LongFilter();
		//filter.setEquals(id);
		//criteria.setId(filter);
		//Optional<Parceiro> parceiro = parceiroQueryService.findOneByCriteria(criteria);
		Optional<Parceiro> parceiro = parceiroService.findOne(id);
		return ResponseUtil.wrapOrNotFound(parceiro);
	}

	/**
	 * {@code DELETE  /parceiros/:id} : delete the "id" parceiro.
	 *
	 * @param id the id of the parceiroDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/parceiros/{id}")
	public ResponseEntity<Void> deleteParceiro(@PathVariable Long id) {
		log.debug("REST request to delete Parceiro : {}", id);
		parceiroService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}
}
