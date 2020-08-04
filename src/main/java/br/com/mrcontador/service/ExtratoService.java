package br.com.mrcontador.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.TipoEntrada;
import br.com.mrcontador.file.ofx.dto.ListOfxDto;
import br.com.mrcontador.file.ofx.dto.OfxDTO;
import br.com.mrcontador.file.ofx.dto.OfxData;
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.MrContadorUtil;

/**
 * Service Implementation for managing {@link Extrato}.
 */
@Service
@Transactional
public class ExtratoService {

	private final Logger log = LoggerFactory.getLogger(ExtratoService.class);

	private final ExtratoRepository extratoRepository;

	private final S3Service s3Service;


	public ExtratoService(ExtratoRepository extratoRepository,
			S3Service s3Service) {
		this.extratoRepository = extratoRepository;
		this.s3Service = s3Service;
	}

	/**
	 * Save a extrato.
	 *
	 * @param extratoDTO the entity to save.
	 * @return the persisted entity.
	 */
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
	public void delete(Long id) {
		log.debug("Request to delete Extrato : {}", id);
		extratoRepository.deleteById(id);
	}

	public void save(ListOfxDto listOfxDto,  Agenciabancaria agenciaBancaria) {
		if (listOfxDto.getOfxDTOs().isEmpty()) {
			return;
		}
		Arquivo arquivo = s3Service.uploadExtrato(listOfxDto.getFileDTO());
		OfxDTO ofxDto = listOfxDto.getOfxDTOs().get(0);
		validate(ofxDto.getBanco(), ofxDto.getAgencia(),ofxDto.getConta(), listOfxDto.getFileDTO().getParceiro(),agenciaBancaria);
		List<Extrato> extratos = new ArrayList<Extrato>();
		for (OfxDTO _ofxDto : listOfxDto.getOfxDTOs()) {
			for (OfxData ofxData : _ofxDto.getDataList()) {
				Extrato extrato = new Extrato();
				extrato.setAgenciabancaria(agenciaBancaria);
				extrato.setArquivo(arquivo);
				extrato.setExtDatalancamento(new java.sql.Date(ofxData.getLancamento().getTime()).toLocalDate());
				extrato.setExtHistorico(ofxData.getHistorico());
				extrato.setExtDescricao(ofxData.getTipoEntrada().toString());
				extrato.setExtNumerocontrole(ofxData.getControle());
				extrato.setExtNumerodocumento(ofxData.getDocumento());
				extrato.setParceiro(listOfxDto.getFileDTO().getParceiro());
				if (ofxData.getTipoEntrada().equals(TipoEntrada.CREDIT) || ofxData.getValor().compareTo(BigDecimal.ZERO)>0) {
					extrato.setExtCredito(ofxData.getValor());
				}else {
					extrato.setExtDebito(ofxData.getValor());
				}
				extratos.add(extrato);
			}
		}
		extratoRepository.saveAll(extratos);
	}

	private void validate(String banco, String agencia, String conta, Parceiro parceiro, Agenciabancaria agenciaBancaria) {		
		if (agencia != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeAgencia(), agencia)) {
				throw new MrContadorException("agencia.notequals");
			}
		}
		if(banco != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getBanCodigobancario(),banco)) {
				throw new MrContadorException("banco.notequals");
			}
		}
		if(conta != null) {
			if(!MrContadorUtil.compareWithoutDigit(agenciaBancaria.getAgeNumero(), conta)) {
				throw new MrContadorException("conta.notequals");
			}
		}
		if(!parceiro.getId().equals(agenciaBancaria.getParceiro().getId())) {
			throw new MrContadorException("parceiro.notequals");
		}

	}
	
	
}
