package br.com.mrcontador.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.client.CnpjClient;
import br.com.mrcontador.client.dto.PessoaJuridica;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.repository.ParceiroRepository;
import br.com.mrcontador.service.dto.ParceiroDTO;
import br.com.mrcontador.service.mapper.ParceiroMapper;
import br.com.mrcontador.service.mapper.ParceiroPJMapper;
import br.com.mrcontador.util.MrContadorUtil;
import br.com.mrcontador.web.rest.errors.CnpjAlreadyExistException;

/**
 * Service Implementation for managing {@link Parceiro}.
 */
@Service
@Transactional
public class ParceiroService {

	private final Logger log = LoggerFactory.getLogger(ParceiroService.class);

	private final ParceiroRepository parceiroRepository;

	private final ParceiroMapper parceiroMapper;

	private final CnpjClient cnpjClient;

	public ParceiroService(ParceiroRepository parceiroRepository, ParceiroMapper parceiroMapper,
			CnpjClient cnpjClient) {
		this.parceiroRepository = parceiroRepository;
		this.parceiroMapper = parceiroMapper;
		this.cnpjClient = cnpjClient;
	}

	/**
	 * Save a parceiro.
	 *
	 * @param parceiroDTO the entity to save.
	 * @return the persisted entity.
	 */
	public ParceiroDTO save(ParceiroDTO parceiroDTO) {
		log.debug("Request to save Parceiro : {}", parceiroDTO);
		Parceiro parceiro = parceiroMapper.toEntity(parceiroDTO);
		parceiro = parceiroRepository.save(parceiro);
		return parceiroMapper.toDto(parceiro);
	}

	public Parceiro save(Parceiro parceiro) {
		return parceiroRepository.save(parceiro);
	}

	/**
	 * Get all the parceiros.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<ParceiroDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Parceiros");
		return parceiroRepository.findAll(pageable).map(parceiroMapper::toDto);
	}

	/**
	 * Get one parceiro by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<ParceiroDTO> findOne(Long id) {
		log.debug("Request to get Parceiro : {}", id);
		return parceiroRepository.findById(id).map(parceiroMapper::toDto);
	}

	/**
	 * Delete the parceiro by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete Parceiro : {}", id);
		parceiroRepository.deleteById(id);
	}

	public Optional<Parceiro> findByParCnpjcpf(String parCnpjcpf) {
		parCnpjcpf = MrContadorUtil.onlyNumbers(parCnpjcpf);
		return parceiroRepository.findByParCnpjcpf(parCnpjcpf);
	}

	private Parceiro saveParceiro(PessoaJuridica pessoaJuridica) {
		ParceiroPJMapper mapper = new ParceiroPJMapper();
		Parceiro parceiro = mapper.toEntity(pessoaJuridica);
		parceiro.setEnabled(true);
		return parceiroRepository.save(parceiro);
	}

	private PessoaJuridica getPessoa(String cnpj) {
		if (cnpj == null) {
			throw new MrContadorException("planoconta.cnpjnull.error");
		}
		cnpj = MrContadorUtil.onlyNumbers(cnpj);
		try {
			return cnpjClient.fromRecitaWs(cnpj);
		} catch (Exception e) {
			log.error(e.getMessage());
			PessoaJuridica pessoaJuridica = new PessoaJuridica();
			pessoaJuridica.setCnpj(cnpj);
			return pessoaJuridica;
		}
	}

	public Parceiro saveByServiceCnpj(String cnpj, ContaService contaService) {
		Optional<Parceiro> oParceiro = findByParCnpjcpf(cnpj);
		Parceiro parceiro = null;
		if (oParceiro.isPresent()) {
			parceiro = oParceiro.get();
			if (contaService.findFirstByParceiro(parceiro).isPresent()) {
				throw new CnpjAlreadyExistException();
			}
		} else {
			PessoaJuridica pessoaJuridica = getPessoa(cnpj);
			parceiro = saveParceiro(pessoaJuridica);
		}
		return parceiro;
	}
}
