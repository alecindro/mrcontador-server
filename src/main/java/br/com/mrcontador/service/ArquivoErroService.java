package br.com.mrcontador.service;

import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.repository.ArquivoErroRepository;
import br.com.mrcontador.service.file.S3Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ArquivoErro}.
 */
@Service
@Transactional
public class ArquivoErroService {

    private final Logger log = LoggerFactory.getLogger(ArquivoErroService.class);

    private final ArquivoErroRepository arquivoErroRepository;
    
    private final S3Service s3Service;

    public ArquivoErroService(ArquivoErroRepository arquivoErroRepository,S3Service s3Service) {
        this.arquivoErroRepository = arquivoErroRepository;
        this.s3Service = s3Service;
    }
    
    

    /**
     * Save a arquivoErro.
     *
     * @param arquivoErro the entity to save.
     * @return the persisted entity.
     */
    public ArquivoErro save(ArquivoErro arquivoErro) {
        log.debug("Request to save ArquivoErro : {}", arquivoErro);
        return arquivoErroRepository.save(arquivoErro);
    }

    /**
     * Get all the arquivoErros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArquivoErro> findAll(Pageable pageable) {
        log.debug("Request to get all ArquivoErros");
        return arquivoErroRepository.findAll(pageable);
    }


    /**
     * Get one arquivoErro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArquivoErro> findOne(Long id) {
        log.debug("Request to get ArquivoErro : {}", id);
        return arquivoErroRepository.findById(id);
    }

    /**
     * Delete the arquivoErro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArquivoErro : {}", id);
        arquivoErroRepository.deleteById(id);
    }
}
