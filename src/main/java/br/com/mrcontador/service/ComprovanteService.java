package br.com.mrcontador.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.comprovante.DiffPage;
import br.com.mrcontador.file.comprovante.DiffValue;
import br.com.mrcontador.repository.ComprovanteRepository;
import br.com.mrcontador.service.mapper.ComprovanteParseMapper;

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
    
    public void save(List<DiffPage> diffPages,Agenciabancaria agencia, Parceiro parceiro) {
    	ComprovanteParseMapper mapper = new ComprovanteParseMapper();
    	for(DiffPage diffPage  :diffPages) {
    		mapper.toEntity(diffPage.getDiffValues(), agencia, parceiro);
    		
    	}
    }
}
