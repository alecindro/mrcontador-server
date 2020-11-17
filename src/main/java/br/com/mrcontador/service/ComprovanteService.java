package br.com.mrcontador.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.repository.ComprovanteRepository;

/**
 * Service Implementation for managing {@link Comprovante}.
 */
@Service
@Transactional
public class ComprovanteService {

    private final Logger log = LoggerFactory.getLogger(ComprovanteService.class);

    private final ComprovanteRepository comprovanteRepository;

    public ComprovanteService(ComprovanteRepository comprovanteRepository) {
        this.comprovanteRepository = comprovanteRepository;
    }

    /**
     * Save a comprovante.
     *
     * @param comprovanteDTO the entity to save.
     * @return the persisted entity.
     */
    public Comprovante save(Comprovante comprovante) {
        log.debug("Request to save Comprovante : {}", comprovante);
        return comprovanteRepository.save(comprovante);
    }

    /**
     * Get all the comprovantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Comprovante> findAll(Pageable pageable) {
        log.debug("Request to get all Comprovantes");
        return comprovanteRepository.findAll(pageable);
    }


    /**
     * Get one comprovante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Comprovante> findOne(Long id) {
        log.debug("Request to get Comprovante : {}", id);
        return comprovanteRepository.findById(id);
    }

    /**
     * Delete the comprovante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comprovante : {}", id);
        comprovanteRepository.deleteById(id);
    }
    
    public List<Comprovante> saveAll(List<Comprovante> comprovantes) {
    	return comprovanteRepository.saveAll(comprovantes);
    }
    
    @Async("taskExecutor")
    public int callComprovante(Comprovante comprovante) {
    	return comprovanteRepository.callComprovante(comprovante.getId());
    }
    
    @Async("taskExecutor")
    public void callComprovanteGeral(Long parceiroId) {
    	comprovanteRepository.callComprovanteGeral(parceiroId);
    }
	
	
	public void processadoTrue(Comprovante comprovante) {
		comprovanteRepository.processadoTrue(comprovante.getId());
	}
	
	public void updateArquivo(Long id, Long arquivoId) {
		comprovanteRepository.updateArquivo(id, arquivoId);
	}
}
