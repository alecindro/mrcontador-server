package br.com.mrcontador.service;

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.repository.ArquivoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Arquivo}.
 */
@Service
@Transactional
public class ArquivoService {

    private final Logger log = LoggerFactory.getLogger(ArquivoService.class);

    private final ArquivoRepository arquivoRepository;

    public ArquivoService(ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    /**
     * Save a arquivo.
     *
     * @param arquivo the entity to save.
     * @return the persisted entity.
     */
    public Arquivo save(Arquivo arquivo) {
        log.debug("Request to save Arquivo : {}", arquivo);
        return arquivoRepository.save(arquivo);
    }

    /**
     * Get all the arquivos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Arquivo> findAll(Pageable pageable) {
        log.debug("Request to get all Arquivos");
        return arquivoRepository.findAll(pageable);
    }


    /**
     * Get one arquivo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Arquivo> findOne(Long id) {
        log.debug("Request to get Arquivo : {}", id);
        return arquivoRepository.findById(id);
    }

    /**
     * Delete the arquivo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Arquivo : {}", id);
        arquivoRepository.deleteById(id);
    }
}
