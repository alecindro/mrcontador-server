package br.com.mrcontador.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.file.extrato.PdfParserExtrato;

@Service
public class FunctionService {
	private final Logger log = LoggerFactory.getLogger(FunctionService.class);
	
	@Autowired 
	private NotafiscalService notafiscalService;
	@Autowired
	private ComprovanteService comprovanteService; 
	@Autowired ExtratoService extratoService;
	
	@Async("taskExecutor")
	public void callProcessaNotafiscal(List<Notafiscal> notaFiscals, String tenant) {
		TenantContext.setTenantSchema(tenant);
		notaFiscals.forEach(notaFiscal -> {
			try {
				notafiscalService.callProcessaNotafiscal(notaFiscal.getId());
			}catch(Exception e) {
				log.error(e.getMessage(),e);
			}
		});
	}
	
	  @Async("taskExecutor")
	    public void callComprovante(List<Comprovante> comprovantes, String tenant) {
	    	TenantContext.setTenantSchema(tenant);
	    	for(Comprovante comprovante : comprovantes) {
	    		try {
	    			comprovanteService.callComprovante(comprovante.getId());
	    		}catch(Exception e) {
	    			log.error(e.getMessage(),e);
	    		}
	    	}
	    }
	    
	    @Async("taskExecutor")
	    public void callComprovanteGeral(Long parceiroId, String tenant) {
	    	TenantContext.setTenantSchema(tenant);
	    	try {
	    		comprovanteService.callComprovanteGeral(parceiroId);
	    	}catch(Exception e) {
				log.error(e.getMessage(),e);
			}
	    }
	    
		@Async("taskExecutor")
		public void callExtrato(PdfParserExtrato pdfParser, List<Extrato> extratos, Set<String> periodos, Long parceiroId,  Long agenciaId, String tenant) {
			TenantContext.setTenantSchema(tenant);
			try {
			extratoService.callExtrato(pdfParser, extratos, periodos, parceiroId, agenciaId);
			}catch(Exception e) {
				log.error(e.getMessage(),e);
			}
		}

}
