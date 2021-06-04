package br.com.mrcontador.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mrcontador.service.StatsService;
import br.com.mrcontador.service.dto.StatsDTO;

@RestController
@RequestMapping("/api")
public class StatsResource {

	private final StatsService statsService;
	 private final Logger log = LoggerFactory.getLogger(StatsResource.class);
	
	public StatsResource(StatsService statsService) {
		this.statsService = statsService;
	}
	
	  @GetMapping("/stats")
	    public ResponseEntity<StatsDTO> getStatsDTO(Long parceiroId) {
	        log.debug("REST request to get Stats by parceiro: {}", parceiroId);
	        StatsDTO statsDTO = statsService.getStats(parceiroId);
	        return ResponseEntity.ok().body(statsDTO);
	    }
}
