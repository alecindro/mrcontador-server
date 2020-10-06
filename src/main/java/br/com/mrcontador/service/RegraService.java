package br.com.mrcontador.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Regra;
import br.com.mrcontador.repository.RegraRepository;

/**
 * Service Implementation for managing {@link Regra}.
 */
@Service
@Transactional
public class RegraService {

	private final Logger log = LoggerFactory.getLogger(RegraService.class);

	private final RegraRepository regraRepository;
	private final InteligentService inteligentService;

	public RegraService(RegraRepository regraRepository, InteligentService inteligentService) {
		this.regraRepository = regraRepository;
		this.inteligentService = inteligentService;
	}

	/**
	 * Save a regra.
	 *
	 * @param regraDTO the entity to save.
	 * @return the persisted entity.
	 */
	public Regra save(Regra regra) {
		log.debug("Request to save Regra : {}", regra);
		boolean create = regra.getId() == null ? true : false;
		regra = regraRepository.save(regra);
		if(create) {
		inteligentService.createFromRegra(regra);
		}else {
			inteligentService.updateFromRegra(regra);
		}
		return regra;
	}

	/**
	 * Get all the regras.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Transactional(readOnly = true)
	public Page<Regra> findAll(Pageable pageable) {
		log.debug("Request to get all Regras");
		return regraRepository.findAll(pageable);
	}

	/**
	 * Get one regra by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Transactional(readOnly = true)
	public Optional<Regra> findOne(Long id) {
		log.debug("Request to get Regra : {}", id);
		return regraRepository.findById(id);
	}

	/**
	 * Delete the regra by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		log.debug("Request to delete Regra : {}", id);
		Optional<Regra> _regra = regraRepository.findById(id);
		if(_regra.isEmpty()) {
			throw new RuntimeException("não encontrao a regra");
		}
		inteligentService.deleteRegra(_regra.get());
		regraRepository.deleteById(id);
	}
}
