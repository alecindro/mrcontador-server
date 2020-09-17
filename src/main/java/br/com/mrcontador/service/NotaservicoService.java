package br.com.mrcontador.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Notaservico;
import br.com.mrcontador.repository.NotaservicoRepository;

/**
 * Service Implementation for managing {@link Notaservico}.
 */
@Service
@Transactional
public class NotaservicoService {

    private final Logger log = LoggerFactory.getLogger(NotaservicoService.class);

    private final NotaservicoRepository notaservicoRepository;


    public NotaservicoService(NotaservicoRepository notaservicoRepository) {
        this.notaservicoRepository = notaservicoRepository;
    }

    /**
     * Save a notaservico.
     *
     * @param notaservicoDTO the entity to save.
     * @return the persisted entity.
     */
    public Notaservico save(Notaservico notaservico) {
        log.debug("Request to save Notaservico : {}", notaservico);
        return notaservicoRepository.save(notaservico);
    }

    /**
     * Get all the notaservicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Notaservico> findAll(Pageable pageable) {
        log.debug("Request to get all Notaservicos");
        return notaservicoRepository.findAll(pageable);
    }


    /**
     * Get one notaservico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Notaservico> findOne(Long id) {
        log.debug("Request to get Notaservico : {}", id);
        return notaservicoRepository.findById(id);
    }

    /**
     * Delete the notaservico by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notaservico : {}", id);
        notaservicoRepository.deleteById(id);
    }
}
