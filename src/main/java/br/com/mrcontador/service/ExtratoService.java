package br.com.mrcontador.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.repository.ExtratoRepository;

/**
 * Service Implementation for managing {@link Extrato}.
 */
@Service
public class ExtratoService {

	private final Logger log = LoggerFactory.getLogger(ExtratoService.class);

	private final ExtratoRepository extratoRepository;
	private final NotafiscalService notafiscalService;

	public ExtratoService(ExtratoRepository extratoRepository, NotafiscalService notafiscalService) {
		this.extratoRepository = extratoRepository;
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
	public void callExtratoBB(List<Extrato> extratos, Long parceiroId) {
		log.info("call extrato");
		if (!extratos.isEmpty()) {
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			Long agenciaBancariaId = extratos.stream().findFirst().get().getAgenciabancaria().getId();
			periodos.forEach(periodo -> {
				try {
					extratoRepository.callExtratoBB(parceiroId, agenciaBancariaId, periodo);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}
	}

	@Transactional
	public void callExtratoCEF(List<Extrato> extratos, Long parceiroId) {
		log.info("call extrato");
		if (!extratos.isEmpty()) {
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			Long agenciaBancariaId = extratos.stream().findFirst().get().getAgenciabancaria().getId();
			periodos.forEach(periodo -> {
				try {
					extratoRepository.callExtratoCef(parceiroId, agenciaBancariaId, periodo);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}
	}

	@Transactional
	public void callExtratoSantander(List<Extrato> extratos, Long parceiroId) {
		log.info("call extrato");
		if (!extratos.isEmpty()) {
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			Long agenciaBancariaId = extratos.stream().findFirst().get().getAgenciabancaria().getId();
			periodos.forEach(periodo -> {
				try {
					extratoRepository.callExtratoSantander(parceiroId, agenciaBancariaId, periodo);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}
	}

	@Transactional
	public void callExtratoSafra(List<Extrato> extratos, Long parceiroId) {
		log.info("call extrato");
		if (!extratos.isEmpty()) {
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			Long agenciaBancariaId = extratos.stream().findFirst().get().getAgenciabancaria().getId();
			periodos.forEach(periodo -> {
				try {
					extratoRepository.callExtratoSafra(parceiroId, agenciaBancariaId, periodo);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}
	}

	@Transactional
	public void callExtratoBradesco(List<Extrato> extratos, Long parceiroId) {
		log.info("call extrato");
		if (!extratos.isEmpty()) {
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			Long agenciaBancariaId = extratos.stream().findFirst().get().getAgenciabancaria().getId();
			periodos.forEach(periodo -> {
				try {
					extratoRepository.callExtratoBradesco(parceiroId, agenciaBancariaId, periodo);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}
	}

	@Transactional
	public void callExtratoCredicrea(List<Extrato> extratos, Long parceiroId) {
		log.info("call extrato");
		if (!extratos.isEmpty()) {
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			Long agenciaBancariaId = extratos.stream().findFirst().get().getAgenciabancaria().getId();
			periodos.forEach(periodo -> {
				try {
					extratoRepository.callExtratoCredicrea(parceiroId, agenciaBancariaId, periodo);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}
	}

	@Transactional
	public void callExtratoItau(List<Extrato> extratos, Long parceiroId) {
		log.info("call extrato");
		if (!extratos.isEmpty()) {
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			Long agenciaBancariaId = extratos.stream().findFirst().get().getAgenciabancaria().getId();
			periodos.forEach(periodo -> {
				try {
					extratoRepository.callExtratoItau(parceiroId, agenciaBancariaId, periodo);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}
	}

	@Transactional
	public void callExtratoUnicred(List<Extrato> extratos, Long parceiroId) {
		log.info("call extrato");
		if (!extratos.isEmpty()) {
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			Long agenciaBancariaId = extratos.stream().findFirst().get().getAgenciabancaria().getId();
			periodos.forEach(periodo -> {
				try {
					extratoRepository.callExtratoUnicred(parceiroId, agenciaBancariaId, periodo);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}
	}

	@Transactional
	public void callRegraInteligent(Long parceiroId, Set<String> periodos) {
		periodos.forEach(periodo -> {
			try {
				extratoRepository.regraInteligent(parceiroId, periodo);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		});

	}

	@Transactional
	public void callProcessaNotafiscalGeral(Long parceiroId, LocalDate date) {
		notafiscalService.callProcessaNotafiscalGeral(parceiroId, date);
	}

	@Transactional
	public void callExtraFunctionsSantander(Long extratoId) {
		try {
			extratoRepository.callExtraFunctionsSantander(extratoId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Transactional
	public void callExtraFunctionsBradesco(Long extratoId) {
		try {
			extratoRepository.callExtraFunctionsBradesco(extratoId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Transactional
	public void callExtraFunctionsBB(Long extratoId) {
		try {
			extratoRepository.callExtraFunctionsBB(extratoId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
