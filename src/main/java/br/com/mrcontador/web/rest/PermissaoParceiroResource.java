package br.com.mrcontador.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mrcontador.domain.PermissaoParceiro;
import br.com.mrcontador.service.PermissaoParceiroService;

@RestController
@RequestMapping("/api")
public class PermissaoParceiroResource {

	private final Logger log = LoggerFactory.getLogger(PermissaoParceiroResource.class);
	private final PermissaoParceiroService permissaoParceiroService;
	
	public PermissaoParceiroResource(PermissaoParceiroService permissaoParceiroService) {
		this.permissaoParceiroService = permissaoParceiroService;
	}
	
	@GetMapping("/permissaoparceiros")
	public ResponseEntity<List<PermissaoParceiro>> getAllByUsuario(String usuario) {
		log.debug("REST request to get PermissaoParceiro by usuario: {}", usuario);
		List<PermissaoParceiro> result = permissaoParceiroService.findByUsuario(usuario);
		return ResponseEntity.ok().body(result);
	}
}
