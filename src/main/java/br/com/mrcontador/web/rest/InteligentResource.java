package br.com.mrcontador.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.repository.InteligentRepository.InteligentStats;
import br.com.mrcontador.service.InteligentNfDTOService;
import br.com.mrcontador.service.InteligentQueryService;
import br.com.mrcontador.service.InteligentService;
import br.com.mrcontador.service.ParceiroQueryService;
import br.com.mrcontador.service.StatsService;
import br.com.mrcontador.service.dto.InteligentCriteria;
import br.com.mrcontador.service.dto.InteligentNfDTO;
import br.com.mrcontador.service.dto.InteligentStatsDTO;
import br.com.mrcontador.service.dto.ParceiroCriteria;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.Inteligent}.
 */
@RestController
@RequestMapping("/api")
public class InteligentResource {

	private final Logger log = LoggerFactory.getLogger(InteligentResource.class);

	private final InteligentService inteligentService;

	private final InteligentQueryService inteligentQueryService;

	private final InteligentNfDTOService inteligentNfDTOService;

	private final ParceiroQueryService parceiroQueryService;
	
	private final StatsService statsService; 

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	public InteligentResource(InteligentService inteligentService, InteligentQueryService inteligentQueryService,
			InteligentNfDTOService inteligentNfDTOService, ParceiroQueryService parceiroQueryService,
			StatsService statsService) {
		this.inteligentService = inteligentService;
		this.inteligentQueryService = inteligentQueryService;
		this.inteligentNfDTOService = inteligentNfDTOService;
		this.parceiroQueryService = parceiroQueryService;
		this.statsService = statsService;
	}

	@PostMapping("/inteligents")
	public ResponseEntity<Inteligent> update(@RequestBody Inteligent inteligent) throws URISyntaxException {
		log.debug("REST save inteligent: {}", inteligent);
		if (inteligent.getId() != null) {
			throw new BadRequestAlertException("Invalid id", "inteligent", "idexists");
		}
		inteligent = inteligentService.save(inteligent);
		return ResponseEntity.created(new URI("/api/inteligents/"))
				.headers(HeaderUtil.createAlert(applicationName, "mrcontadorFrontApp.inteligent.created", ""))
				.body(inteligent);
	}

	@PostMapping("/inteligents/nf")
	public ResponseEntity<Void> associateNf(@RequestBody InteligentNfDTO inteligentNfDTO) throws URISyntaxException {
		log.debug("REST save InteligentNfDTO: {}", inteligentNfDTO);
		inteligentNfDTOService.save(inteligentNfDTO);
		return ResponseEntity.created(new URI("/api/inteligents/nf"))
				.headers(HeaderUtil.createAlert(applicationName, "mrcontadorFrontApp.inteligent.nfassociate", ""))
				.build();
	}

	@PutMapping("/inteligents/nf")
	public ResponseEntity<Void> removeNf(@RequestBody Inteligent inteligent) throws URISyntaxException {
		log.debug("REST removeNf Inteligent: {}", inteligent);
		inteligentNfDTOService.removeNF(inteligent);
		return ResponseEntity.created(new URI("/api/inteligents/nf"))
				.headers(HeaderUtil.createAlert(applicationName, "mrcontadorFrontApp.inteligent.nfdesassociate", ""))
				.build();
	}

	@PutMapping("/inteligents")
	public ResponseEntity<Inteligent> save(@RequestBody Inteligent inteligent) throws URISyntaxException {
		log.debug("REST save inteligent: {}", inteligent);
		if (inteligent.getId() == null) {
			throw new BadRequestAlertException("Invalid id", "inteligent", "idnull");
		}
		inteligent = inteligentService.save(inteligent);
		return ResponseEntity.created(new URI("/api/inteligents/"))
				.headers(HeaderUtil.createAlert(applicationName, "mrcontadorFrontApp.inteligent.created", ""))
				.body(inteligent);
	}

	/**
	 * {@code GET  /inteligents} : get all the inteligents.
	 *
	 * @param pageable the pagination information.
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of inteligents in body.
	 */

	@GetMapping("/inteligents")
	public ResponseEntity<List<Inteligent>> getAllInteligents(InteligentCriteria criteria, Sort sort) {
		log.debug("REST request to get Inteligents by criteria: {}", criteria);
		List<Inteligent> page = inteligentQueryService.findByCriteria(criteria, sort);
		// HttpHeaders headers =
		// PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
		// page);
		return ResponseEntity.ok().body(page);
	}

	@GetMapping("/inteligents/stats")
	public ResponseEntity<List<InteligentStatsDTO>> getInteligentStats(ParceiroCriteria criteria, Pageable pageable) {
		log.debug("REST request to get InteligentStats by criteria: {}", criteria);
		Page<Parceiro> page = parceiroQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		List<InteligentStatsDTO> list = new ArrayList<>();
		if (page.hasContent()) {
			for (Parceiro parceiro : page.getContent()) {
				InteligentStatsDTO dto = new InteligentStatsDTO();
				dto.setParceiro(parceiro);
				List<InteligentStats> stats = inteligentService.getInteligentStats(parceiro.getId());
				dto.setCountPeriodos(inteligentService.getCountPeriodos(parceiro.getId()));
				dto.setInteligentStats(stats);
				list.add(dto);
			}
		}
		return ResponseEntity.ok().headers(headers).body(list);
	}

	@GetMapping("/inteligents/periodo")
	public ResponseEntity<List<String>> getPeriodos(@RequestParam("parceiroId") Long parceiroId,
			@RequestParam("agenciabancariaId") Long agenciabancariaId) {
		log.debug("REST request to get Periodos by paceiro: {} and angencia {} ", parceiroId, agenciabancariaId);
		List<String> periodos = inteligentService.periodos(parceiroId, agenciabancariaId);
		return ResponseEntity.ok().body(periodos);
	}	

	/**
	 * {@code GET  /inteligents/count} : count all the inteligents.
	 *
	 * @param criteria the criteria which the requested entities should match.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
	 *         in body.
	 */
	@GetMapping("/inteligents/count")
	public ResponseEntity<Long> countInteligents(InteligentCriteria criteria) {
		log.debug("REST request to count Inteligents by criteria: {}", criteria);
		return ResponseEntity.ok().body(inteligentQueryService.countByCriteria(criteria));
	}

	/**
	 * {@code GET  /inteligents/:id} : get the "id" inteligent.
	 *
	 * @param id the id of the inteligent to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the inteligent, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/inteligents/{id}")
	public ResponseEntity<Inteligent> getInteligent(@PathVariable Long id) {
		log.debug("REST request to get Inteligent : {}", id);
		Optional<Inteligent> inteligent = inteligentService.findOne(id);
		return ResponseUtil.wrapOrNotFound(inteligent);
	}

	@GetMapping("/inteligents/function")
	public ResponseEntity<Void> processInteligent(@RequestParam("periodo") String periodo,
			@RequestParam("parceiroId") Long parceiroId, @RequestParam("agenciabancariaId") Long agenciabancariaId)
			throws Exception {
		log.debug("processando funcao. Periodo: {}, Parceiro: {}, Agencia:{}", periodo, parceiroId, agenciabancariaId);
		try {
			inteligentService.processInteligent(parceiroId, agenciabancariaId, periodo);
		} catch (org.springframework.dao.InvalidDataAccessResourceUsageException e) {
			log.error(e.getMessage(), e);
			throw new MrContadorException("inteligent.fail", e.getCause().getCause().getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new MrContadorException("inteligent.fail", e.getMessage());
		}
		return ResponseEntity.created(new URI("/api/inteligents/function/"))
				.headers(HeaderUtil.createAlert(applicationName, "mrcontadorFrontApp.inteligent.function.process", ""))
				.build();
	}
}
