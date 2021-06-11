package br.com.mrcontador.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mrcontador.domain.PermissaoParceiro;
import br.com.mrcontador.service.PermissaoParceiroQueryService;
import br.com.mrcontador.service.PermissaoParceiroService;
import br.com.mrcontador.service.dto.PermissaoParceiroCriteria;
import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class PermissaoParceiroResource {

	private final Logger log = LoggerFactory.getLogger(PermissaoParceiroResource.class);
	private final PermissaoParceiroQueryService permissaoParceiroQueryService;
	private final PermissaoParceiroService permissaoParceiroService;
	private static final String ENTITY_NAME = "permissaoParceiro";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	
	public PermissaoParceiroResource(PermissaoParceiroQueryService permissaoParceiroQueryService,
			PermissaoParceiroService permissaoParceiroService) {
		this.permissaoParceiroQueryService = permissaoParceiroQueryService;
		this.permissaoParceiroService = permissaoParceiroService;
	}
	
	@GetMapping("/permissaoparceiros")
	public ResponseEntity<List<PermissaoParceiro>> getAllByUsuario(PermissaoParceiroCriteria criteria) {
		log.debug("REST request to get PermissaoParceiro by criteria: {}", criteria);
		List<PermissaoParceiro> result = permissaoParceiroQueryService.findByCriteria(criteria);
		return ResponseEntity.ok().body(result);
	}
	@PostMapping("/permissaoparceiros")
	public ResponseEntity<PermissaoParceiro> createParceiro(@Valid @RequestBody PermissaoParceiro permissaoParceiro)
			throws URISyntaxException {
		log.debug("REST request to save Parceiro : {}", permissaoParceiro);
		PermissaoParceiro result = permissaoParceiroService.save(permissaoParceiro);
		return ResponseEntity
				.created(new URI("/api/parceiros/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}
}
