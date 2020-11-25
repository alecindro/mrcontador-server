package br.com.mrcontador.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.file.extrato.TipoEntrada;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.file.extrato.dto.OfxData;
import br.com.mrcontador.file.extrato.dto.PdfData;
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.MrContadorUtil;

/**
 * Service Implementation for managing {@link Extrato}.
 */
@Service
public class ExtratoService {

	private final Logger log = LoggerFactory.getLogger(ExtratoService.class);

	private final ExtratoRepository extratoRepository;
	private final NotafiscalService notafiscalService;

	private final S3Service s3Service;



	public ExtratoService(ExtratoRepository extratoRepository, S3Service s3Service, NotafiscalService notafiscalService) {
		this.extratoRepository = extratoRepository;
		this.s3Service = s3Service;
		this.notafiscalService = notafiscalService;
	}

	/**
	 * Save a extrato.
	 *
	 * @param extratoDTO the entity to save.
	 * @return the persisted entity.
	 */
	@Transactional
	public Extrato save(Extrato extrato) {
		log.debug("Request to save Extrato : {}", extrato);
		return extratoRepository.save(extrato);
	}

	/**
	 * Get all the extratoes.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<Extrato> findAll(Pageable pageable) {
		log.debug("Request to get all Extratoes");
		return extratoRepository.findAll(pageable);
	}

	/**
	 * Get one extrato by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<Extrato> findOne(Long id) {
		log.debug("Request to get Extrato : {}", id);
		return extratoRepository.findById(id);
	}

	/**
	 * Delete the extrato by id.
	 *
	 * @param id the id of the entity.
	 */
	@Transactional
	public void delete(Long id) {
		log.debug("Request to delete Extrato : {}", id);
		extratoRepository.deleteById(id);
	}
	@Transactional
	public List<Extrato> save(FileDTO fileDTO, OfxDTO ofxDTO, Agenciabancaria agenciaBancaria) {
		log.info("saving extratos");
		List<Extrato> extratos = new ArrayList<Extrato>();
		if (ofxDTO.getDataList().isEmpty()) {
			return extratos;
		}
		Arquivo arquivo = s3Service.uploadExtrato(fileDTO);
		for (OfxData ofxData : ofxDTO.getDataList()) {
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
				extrato = extratoRepository.save(extrato);
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


	@Transactional
	public void callExtrato(List<Extrato> extratos, Long parceiroId, Set<String> periodos) {
		log.info("call extrato");
		extratos.forEach(extrato ->{
			try {
				extratoRepository.callExtrato(extrato.getId(), extrato.getAgenciabancaria().getId());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		});
		try {
			log.info("callRegraInteligent");
		callRegraInteligent(parceiroId, periodos);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		try {
			log.info("callProcessaNotafiscalGeral");
		LocalDate min = extratos.stream().min(Comparator.comparing(Extrato::getExtDatalancamento)).get().getExtDatalancamento();
		min = min.minusMonths(4);
		callProcessaNotafiscalGeral(parceiroId, min);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Transactional
	private void callRegraInteligent(Long parceiroId, Set<String> periodos) {
		periodos.forEach(periodo ->{
			try {
				extratoRepository.regraInteligent(parceiroId, periodo);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		});
		
	}
	
	private void callProcessaNotafiscalGeral(Long parceiroId, LocalDate date) {
		notafiscalService.callProcessaNotafiscalGeral(parceiroId, date);
	}
	
	@Transactional
	public void callExtratoAplicacao(Long agenciaId) {
		try {
			extratoRepository.callExtratoAplicacao(agenciaId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
