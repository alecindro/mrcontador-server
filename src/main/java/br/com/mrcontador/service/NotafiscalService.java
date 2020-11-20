package br.com.mrcontador.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.repository.NotafiscalRepository;

/**
 * Service Implementation for managing {@link Notafiscal}.
 */
@Service
@Transactional
public class NotafiscalService {

	private final Logger log = LoggerFactory.getLogger(NotafiscalService.class);

	private final NotafiscalRepository notafiscalRepository;

	public NotafiscalService(NotafiscalRepository notafiscalRepository) {
		this.notafiscalRepository = notafiscalRepository;
		;
	}

	/**
	 * Save a notafiscal.
	 *
	 * @param notafiscalDTO the entity to save.
	 * @return the persisted entity.
	 */
	@Transactional
	public Notafiscal save(Notafiscal notafiscal) {
		log.debug("Request to save Notafiscal : {}", notafiscal);
		return notafiscalRepository.save(notafiscal);
	}

	@Transactional
	public List<Notafiscal> saveAll(List<Notafiscal> notas) {
		log.debug("Request to saveAll Notafiscal : {}", notas);
		return notafiscalRepository.saveAll(notas);
	}

	/**
	 * Get all the notafiscals.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<Notafiscal> findAll(Pageable pageable) {
		log.debug("Request to get all Notafiscals");
		return notafiscalRepository.findAll(pageable);
	}

	/**
	 * Get one notafiscal by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<Notafiscal> findOne(Long id) {
		log.debug("Request to get Notafiscal : {}", id);
		return notafiscalRepository.findById(id);
	}

	/**
	 * Delete the notafiscal by id.
	 *
	 * @param id the id of the entity.
	 */
	@Transactional
	public void delete(Long id) {
		log.debug("Request to delete Notafiscal : {}", id);
		notafiscalRepository.deleteById(id);
	}


	public void callProcessaNotafiscal(Long notaId) {
			notafiscalRepository.callProcessaNotafiscal(notaId);
	}


	public List<Notafiscal> find(String cnpj, BigDecimal valor, LocalDate data) {
		valor = valor.negate();
		BigDecimal valorFinal = valor.multiply(new BigDecimal(1.3));
		LocalDate datainicial = data.minusMonths(2);
		LocalDate datafinal = data.plusMonths(1);
		return notafiscalRepository.find(cnpj, valor, valorFinal, datainicial, datafinal);
	}

	public void updateArquivo(Long id, Long arquivoId, Long arquivoPdfId) {
		notafiscalRepository.updateArquivo(id, arquivoId, arquivoPdfId);
	}

}
