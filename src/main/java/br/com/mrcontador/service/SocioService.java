package br.com.mrcontador.service;

import br.com.mrcontador.domain.Socio;
import br.com.mrcontador.repository.SocioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Socio}.
 */
@Service
@Transactional
public class SocioService {

    private final Logger log = LoggerFactory.getLogger(SocioService.class);

    private final SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    /**
     * Save a socio.
     *
     * @param socio the entity to save.
     * @return the persisted entity.
     */
    public Socio save(Socio socio) {
        log.debug("Request to save Socio : {}", socio);
        return socioRepository.save(socio);
    }

    /**
     * Get all the socios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Socio> findAll(Pageable pageable) {
        log.debug("Request to get all Socios");
        return socioRepository.findAll(pageable);
    }


    /**
     * Get one socio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Socio> findOne(Long id) {
        log.debug("Request to get Socio : {}", id);
        return socioRepository.findById(id);
    }

    /**
     * Delete the socio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Socio : {}", id);
        socioRepository.deleteById(id);
    }
}
