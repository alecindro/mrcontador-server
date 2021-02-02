package br.com.mrcontador.service;

import java.util.List;
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
import br.com.mrcontador.service.mapper.ParceiroPJMapper;
import br.com.mrcontador.util.MrContadorUtil;

/**
 * Service Implementation for managing {@link Parceiro}.
 */
@Service
@Transactional
public class ParceiroService {

	private final Logger log = LoggerFactory.getLogger(ParceiroService.class);

	private final ParceiroRepository parceiroRepository;

	private final CnpjClient cnpjClient;

	public ParceiroService(ParceiroRepository parceiroRepository, CnpjClient cnpjClient) {
		this.parceiroRepository = parceiroRepository;
		this.cnpjClient = cnpjClient;
	}

	/**
	 * Save a parceiro.
	 *
	 * @param parceiroDTO the entity to save.
	 * @return the persisted entity.
	 */

	public Parceiro save(Parceiro parceiro) {
		if (parceiro.getCadastroStatus() == null || parceiro.getCadastroStatus() < 4) {
			parceiro.setCadastroStatus(parceiro.getCadastroStatus() == null ? 0 : parceiro.getCadastroStatus() + 1);
		}
		return parceiroRepository.save(parceiro);
	}

	public Parceiro update(Parceiro parceiro) {
		//Parceiro parceiroOld = parceiroRepository.findById(parceiro.getId()).get();
		save(parceiro);
		
		/*if (!parceiro.getDescontosAtivos().equals(parceiroOld.getDescontosAtivos())) {

		}
		if (!parceiro.getCaixaConta().equals(parceiroOld.getCaixaConta())) {

		}
		if (!parceiro.getDespesaIof().equals(parceiroOld.getDespesaIof())) {

		}
		if (!parceiro.getDespesaJuros().equals(parceiroOld.getDespesaJuros())) {

		}
		if (!parceiro.getDespesasBancarias().equals(parceiroOld.getDespesasBancarias())) {

		}
		if (!parceiro.getDespesaTarifa().equals(parceiroOld.getDespesaTarifa())) {

		}
		if (!parceiro.getJurosAtivos().equals(parceiroOld.getJurosAtivos())) {

		}		*/
		return parceiro;
	}

	/**
	 * Get all the parceiros.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<Parceiro> findAll(Pageable pageable) {
		log.debug("Request to get all Parceiros");
		return parceiroRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public List<Parceiro> findAll() {
		log.debug("Request to get all Parceiros");
		return parceiroRepository.findAll();
	}

	/**
	 * Get one parceiro by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<Parceiro> findOne(Long id) {
		log.debug("Request to get Parceiro : {}", id);
		return parceiroRepository.findById(id);
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
		if (parceiro.getCadastroStatus() == null || parceiro.getCadastroStatus() < 4) {
			parceiro.setCadastroStatus(parceiro.getCadastroStatus() == null ? 0 : parceiro.getCadastroStatus() + 1);
		}
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

	public Parceiro saveByServiceCnpj(String cnpj) {
		Optional<Parceiro> oParceiro = findByParCnpjcpf(cnpj);
		Parceiro parceiro = null;
		if (oParceiro.isPresent()) {
			parceiro = oParceiro.get();
			parceiro = save(parceiro);
		} else {
			PessoaJuridica pessoaJuridica = getPessoa(cnpj);
			parceiro = saveParceiro(pessoaJuridica);
		}

		return parceiro;
	}
}
