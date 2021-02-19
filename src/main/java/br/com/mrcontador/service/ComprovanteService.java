package br.com.mrcontador.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final NotafiscalService notafiscalService;

    public ComprovanteService(ComprovanteRepository comprovanteRepository,
    		NotafiscalService notafiscalService) {
        this.comprovanteRepository = comprovanteRepository;
        this.notafiscalService = notafiscalService;
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
    
	@Transactional
	public void callProcessaNotafiscalGeral(Long parceiroId, LocalDate date) {
		notafiscalService.callProcessaNotafiscalGeral(parceiroId, date);
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
    
    public void callComprovante(Long comprovanteId) {
    	comprovanteRepository.callComprovante(comprovanteId);
    }
    
    public void callComprovanteBB(Long parceiroId, Long agenciabancariaId, String periodo) {
    	comprovanteRepository.callComprovanteBB(parceiroId,agenciabancariaId,periodo);
    }
    
    public void callComprovanteCEF(Long comprovanteId) {
    	comprovanteRepository.callComprovanteCEF(comprovanteId);
    }
    
    public void callComprovanteCredicrea(Long comprovanteId) {
    	comprovanteRepository.callComprovanteCredicrea(comprovanteId);
    }
    
    public void callComprovanteItau(Long comprovanteId) {
    	comprovanteRepository.callComprovanteItau(comprovanteId);
    }
    
    public void callComprovanteSafra(Long comprovanteId) {
    	comprovanteRepository.callComprovanteSafra(comprovanteId);
    }
    
    public void callComprovanteSantander(Long comprovanteId) {
    	comprovanteRepository.callComprovanteSantander(comprovanteId);
    }
    
    
    public void callComprovanteUnicred(Long comprovanteId) {
    	comprovanteRepository.callComprovanteUnicred(comprovanteId);
    }
    
    public void callComprovanteBradesco(Long parceiroId, Long agenciabancariaId, String periodo) {
    	comprovanteRepository.callComprovanteBradesco(parceiroId,agenciabancariaId,periodo);
    }
    
    
	public void updateArquivo(Long id, Long arquivoId) {
		comprovanteRepository.updateArquivo(id, arquivoId);
	}
}
