package br.com.mrcontador.file.extrato;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.extrato.dto.OfxData;
import br.com.mrcontador.file.extrato.dto.PdfData;
import br.com.mrcontador.service.ArquivoService;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.MrContadorUtil;

public abstract class ExtratoFacade {
	
	@Autowired
	protected ExtratoService extratoService;
	@Autowired
	protected S3Service s3Service;
	@Autowired 
	protected ArquivoService arquivoService;
	
	private static Logger log = LoggerFactory.getLogger(ExtratoFacade.class);
	
	protected abstract String process(FileDTO fileDTO, Agenciabancaria agenciaBancaria);
	
	protected List<Extrato> save(FileDTO fileDTO, OfxDTO ofxDTO, Agenciabancaria agenciaBancaria) {
		log.info("saving extratos");
		List<Extrato> extratos = new ArrayList<Extrato>();
		if (ofxDTO.getDataList().isEmpty()) {
			return extratos;
		}
		Arquivo arquivo = s3Service.uploadExtrato(fileDTO);
		arquivo = arquivoService.save(arquivo);
		for (OfxData ofxData : ofxDTO.getDataList()) {
			if (ofxData.getTipoEntrada().equals(TipoEntrada.INVALID)){
				continue;
			}
			Extrato extrato = new Extrato();
			extrato.setAgenciabancaria(agenciaBancaria);
			extrato.setArquivo(arquivo);
			extrato.setExtDatalancamento(new java.sql.Date(ofxData.getLancamento().getTime()).toLocalDate());
			extrato.setExtHistorico(ofxData.getHistorico());
			extrato.setExtDescricao(ofxData.getTipoEntrada().toString());
			extrato.setExtNumerocontrole(ofxData.getControle());
			extrato.setExtNumerodocumento(ofxData.getDocumento());
			extrato.setAgenciaOrigem(ofxData.getAgenciaOrigem());
			extrato.setPeriodo(MrContadorUtil.periodo(extrato.getExtDatalancamento()));
			if (ofxData instanceof PdfData) {
				extrato.setInfoAdicional(((PdfData) ofxData).getInfAdicional());
			}
			extrato.setParceiro(fileDTO.getParceiro());
			
			if (ofxData.getTipoEntrada().equals(TipoEntrada.CREDIT)
					|| ofxData.getValor().compareTo(BigDecimal.ZERO) > 0) {
				extrato.setExtCredito(ofxData.getValor());
			} else {
				extrato.setExtDebito(ofxData.getValor());
			}
			try {
				extrato = extratoService.save(extrato);
				extratos.add(extrato);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		if (extratos.isEmpty()) {
			throw new org.springframework.dao.DataIntegrityViolationException("extrato já importado");
		}
		return extratos;
	}


}
