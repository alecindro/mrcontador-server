package br.com.mrcontador.web.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.StatsService;
import br.com.mrcontador.service.dto.StatsDTO;

@RestController
@RequestMapping("/api")
public class StatsResource {

	private final StatsService statsService;
	private final ParceiroService parceiroService;
	 private final Logger log = LoggerFactory.getLogger(StatsResource.class);
	
	public StatsResource(StatsService statsService,ParceiroService parceiroService) {
		this.statsService = statsService;
		this.parceiroService = parceiroService;
	}
	
	  @GetMapping("/stats/{parceiroId}")
	    public ResponseEntity<StatsDTO> getStatsDTO(@PathVariable Long parceiroId) {
	        log.debug("REST request to get Stats by parceiro: {}", parceiroId);
	        Optional<Parceiro> parceiro = parceiroService.findOne(parceiroId);
	        StatsDTO statsDTO = statsService.getStats(parceiro.get());
	        return ResponseEntity.ok().body(statsDTO);
	    }
}
