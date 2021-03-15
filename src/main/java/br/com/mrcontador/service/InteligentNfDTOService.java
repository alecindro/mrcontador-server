package br.com.mrcontador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.service.dto.InteligentNfDTO;
import liquibase.pro.packaged.in;

@Service
public class InteligentNfDTOService {

	
	@Autowired
	private NotafiscalService notafiscalService; 
	@Autowired
	private InteligentService inteligentService;
	@Autowired
	private ContaService contaService;
	
	@Transactional
	public void save(InteligentNfDTO inteligentNfDTO) {
		notafiscalService.saveAll(inteligentNfDTO.getNotafiscals());
		for(Inteligent inteligent : inteligentNfDTO.getInteligents()) {
			if(inteligent.getNotafiscal().getId() == null) {
			notafiscalService.save(inteligent.getNotafiscal());
			}
		}
		inteligentService.saveAll(inteligentNfDTO.getInteligents());
		if(!inteligentNfDTO.getInteligents().isEmpty()) {
			Inteligent inteligent = inteligentNfDTO.getInteligents().get(0);
			Long parceiroId = inteligent.getParceiro().getId();
			String periodo = inteligent.getPeriodo();
			contaService.callContaFunction(parceiroId, periodo);
		}
	}
	
	@Transactional
	public void removeNF(Inteligent inteligent) {
		Notafiscal nf = inteligent.getNotafiscal();
		nf.processado(false);
		notafiscalService.save(nf);
		inteligent.setAssociado(false);
		inteligent.setNotafiscal(null);
		inteligent.setConta(null);
		inteligent.setHistoricofinal(null);
		inteligentService.save(inteligent);
	}
}
