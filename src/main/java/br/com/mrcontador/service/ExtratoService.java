package br.com.mrcontador.service;

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
import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.TipoEntrada;
import br.com.mrcontador.file.ofx.dto.ListOfxDto;
import br.com.mrcontador.file.ofx.dto.OfxDTO;
import br.com.mrcontador.file.ofx.dto.OfxData;
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.service.dto.AgenciabancariaCriteria;
import br.com.mrcontador.service.dto.BancoCriteria;
import br.com.mrcontador.service.dto.ExtratoDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.service.mapper.ExtratoMapper;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Service Implementation for managing {@link Extrato}.
 */
@Service
@Transactional
public class ExtratoService {

	private final Logger log = LoggerFactory.getLogger(ExtratoService.class);

	private final ExtratoRepository extratoRepository;

	private final AgenciabancariaQueryService agenciaBancariaService;

	private final AgenciabancariaService agenciaService;

	private final ExtratoMapper extratoMapper;

	private final S3Service s3Service;
	
	private final BancoQueryService bancoService;

	public ExtratoService(ExtratoRepository extratoRepository, ExtratoMapper extratoMapper,
			AgenciabancariaQueryService agenciaBancariaService, AgenciabancariaService agenciaService,
			S3Service s3Service,
			BancoQueryService bancoService) {
		this.extratoRepository = extratoRepository;
		this.extratoMapper = extratoMapper;
		this.agenciaBancariaService = agenciaBancariaService;
		this.agenciaService = agenciaService;
		this.s3Service = s3Service;
		this.bancoService = bancoService;
	}

	/**
	 * Save a extrato.
	 *
	 * @param extratoDTO the entity to save.
	 * @return the persisted entity.
	 */
	public ExtratoDTO save(ExtratoDTO extratoDTO) {
		log.debug("Request to save Extrato : {}", extratoDTO);
		Extrato extrato = extratoMapper.toEntity(extratoDTO);
		extrato = extratoRepository.save(extrato);
		return extratoMapper.toDto(extrato);
	}
	
	public Extrato save(Extrato extrato) {
		return extratoRepository.save(extrato);
	}

	/**
	 * Get all the extratoes.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<ExtratoDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Extratoes");
		return extratoRepository.findAll(pageable).map(extratoMapper::toDto);
	}

	/**
	 * Get one extrato by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<ExtratoDTO> findOne(Long id) {
		log.debug("Request to get Extrato : {}", id);
		return extratoRepository.findById(id).map(extratoMapper::toDto);
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

	public void save(ListOfxDto listOfxDto) {
		if (listOfxDto.getOfxDTOs().isEmpty()) {
			return;
		}
		Arquivo arquivo = s3Service.uploadExtrato(listOfxDto.getFileDTO());
		OfxDTO ofxDto = listOfxDto.getOfxDTOs().get(0);
		Agenciabancaria agencia = find(ofxDto.getBanco(), ofxDto.getAgencia(),ofxDto.getConta(), listOfxDto.getFileDTO().getParceiro());
		List<Extrato> extratos = new ArrayList<Extrato>();
		for (OfxDTO _ofxDto : listOfxDto.getOfxDTOs()) {
			for (OfxData ofxData : _ofxDto.getDataList()) {
				Extrato extrato = new Extrato();
				extrato.setAgenciabancaria(agencia);
				extrato.setArquivo(arquivo);
				extrato.setExtDatalancamento(new java.sql.Date(ofxData.getLancamento().getTime()).toLocalDate());
				extrato.setExtHistorico(ofxData.getHistorico());
				extrato.setExtDescricao(ofxData.getTipoEntrada().toString());
				extrato.setExtNumerocontrole(ofxData.getControle());
				extrato.setExtNumerodocumento(ofxData.getDocumento());
				extrato.setParceiro(listOfxDto.getFileDTO().getParceiro());
				if (ofxData.getTipoEntrada().equals(TipoEntrada.CREDIT)) {
					extrato.setExtCredito(ofxData.getValor());
				}else {
					extrato.setExtDebito(ofxData.getValor());
				}
				extratos.add(extrato);
			}
		}
		extratoRepository.saveAll(extratos);
	}

	private Agenciabancaria find(String banco, String agencia, String conta, Parceiro parceiro) {		
		if (agencia != null) {
			AgenciabancariaCriteria agenciaCriteria = new AgenciabancariaCriteria();			
			StringFilter bancoFilter = new StringFilter();
			bancoFilter.setEquals(banco);			
			agenciaCriteria.setBanCodigobancario(bancoFilter);			
			StringFilter contaFilter = new StringFilter();
			contaFilter.setEquals(conta);
			agenciaCriteria.setAgeNumero(contaFilter);			
			LongFilter parceiroFilter = new LongFilter();
			parceiroFilter.setEquals(parceiro.getId());
			agenciaCriteria.setParceiroId(parceiroFilter);			
			StringFilter agenciaFilter = new StringFilter();
			agenciaFilter.setEquals(agencia);
			agenciaCriteria.setAgeAgencia(agenciaFilter);
			List<Agenciabancaria> agencias = agenciaBancariaService.findAgenciaByCriteria(agenciaCriteria);
			if (agencias != null && !agencias.isEmpty()) {
				return agencias.get(0);
			}
		}		
		Agenciabancaria agenciaBancaria = new Agenciabancaria();
		agenciaBancaria.setParceiro(parceiro);
		agenciaBancaria.setAgeNumero(conta);
		agenciaBancaria.setBanCodigobancario(banco);
		agenciaBancaria.setAgeAgencia(agencia);
		agenciaBancaria.setBanco(findBanco(banco));
		return agenciaService.save(agenciaBancaria);
	}
	
	private Banco findBanco(String banco) {
		BancoCriteria criteria = new BancoCriteria();
		StringFilter filter = new StringFilter();
		filter.setEquals(banco);
		criteria.setBan_codigobancario(filter);
		List<Banco> list = bancoService.findBancoByCriteria(criteria);
		if(list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
}
