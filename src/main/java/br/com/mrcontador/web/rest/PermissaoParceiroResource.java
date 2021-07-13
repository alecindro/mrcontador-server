package br.com.mrcontador.web.rest;

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
import br.com.mrcontador.service.dto.PermissaoParceiroDTO;

@RestController
@RequestMapping("/api")
public class PermissaoParceiroResource {

	private final Logger log = LoggerFactory.getLogger(PermissaoParceiroResource.class);
	private final PermissaoParceiroQueryService permissaoParceiroQueryService;
	private final PermissaoParceiroService permissaoParceiroService;

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
	public ResponseEntity<List<PermissaoParceiro>> createPermissaoParceiro(@Valid @RequestBody PermissaoParceiroDTO permissaos)
			throws URISyntaxException {
		log.debug("REST request to save PermissaoParceiro");
		List<PermissaoParceiro> result = permissaoParceiroService.save(permissaos.getPermissaos());
		return ResponseEntity.ok().body(result);
	}
}
