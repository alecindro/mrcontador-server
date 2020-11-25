package br.com.mrcontador.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Notafiscal;

@Service
public class FunctionService {
	private final Logger log = LoggerFactory.getLogger(FunctionService.class);
	
	@Autowired 
	private NotafiscalService notafiscalService;
	@Autowired
	private ComprovanteService comprovanteService; 
	/*
	//@Async("taskExecutor")
	public void callProcessaNotafiscal(List<Notafiscal> notaFiscals) {
		//TenantContext.setTenantSchema(tenant);
		log.info("Processando callProcessaNotafiscal");
		notaFiscals.forEach(notaFiscal -> {
			try {
				notafiscalService.callProcessaNotafiscal(notaFiscal.getId());
			}catch(Exception e) {
				log.error(e.getMessage(),e);
			}
		});
	}
	
	 // @Async("taskExecutor")
	    public void callComprovante(List<Comprovante> comprovantes) {
	    //	TenantContext.setTenantSchema(tenant);
	    	log.info("Processando callComprovante");
	    	comprovantes.forEach(comprovante ->{
	    		try {
	    			comprovanteService.callComprovante(comprovante.getId());
	    		}catch(Exception e) {
	    			log.error(e.getMessage(),e);
	    		}
	    	});
	    }
	 /*   
	 	//@Async("taskExecutor")
		public void callExtrato(PdfParserExtrato pdfParser, List<Extrato> extratos, Set<String> periodos, Long parceiroId,  Long agenciaId, String tenant) {
			//TenantContext.setTenantSchema(tenant);
			LocalDateTime inicio = LocalDateTime.now();
			log.info("Processando callExtrato");
			extratoService.callExtrato(extratos);
			extratoService.callRegraInteligent(parceiroId, periodos);
			pdfParser.extrasFunctions(extratoService,extratos);			
			LocalDateTime fim = LocalDateTime.now();
			long minutes = ChronoUnit.MINUTES.between(inicio, fim);
			long hours = ChronoUnit.HOURS.between(inicio, fim);
			long seconds = ChronoUnit.SECONDS.between(inicio, fim);
			log.info("Finalizou Processando callExtrato {}:{}:{}", hours, minutes, seconds);
			
		}
		
/*		private void comprovanteFromExtrato(Long parceiroId) {
			LocalDateTime inicio = LocalDateTime.now();
			ComprovanteCriteria criteria = new ComprovanteCriteria();
			LongFilter parceiroFilter = new LongFilter();
			parceiroFilter.setEquals(parceiroId);
			BooleanFilter processadoFilter = new BooleanFilter();
			processadoFilter.setEquals(false);
			criteria.setParceiroId(parceiroFilter);
			criteria.setProcessado(processadoFilter);
			List<Comprovante> comprovantes = comprovanteQueryService.findByCriteria(criteria);
			//String tenant = SecurityUtils.getCurrentTenantHeader();
			//comprovantes.forEach(c -> extratoService.callComprovante(c.getId(), tenant));
			callComprovante(comprovantes);
			LocalDateTime fim = LocalDateTime.now();
			long minutes = ChronoUnit.MINUTES.between(inicio, fim);
			long hours = ChronoUnit.HOURS.between(inicio, fim);
			long seconds = ChronoUnit.SECONDS.between(inicio, fim);
			log.info("Finalizou Processando comprovanteFromExtrato {}:{}:{}", hours, minutes, seconds);
			
		}*/

}
