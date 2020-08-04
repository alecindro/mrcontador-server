package br.com.mrcontador.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.repository.AgenciabancariaRepository;

/**
 * Service Implementation for managing {@link Agenciabancaria}.
 */
@Service
@Transactional
public class AgenciabancariaService {

    private final Logger log = LoggerFactory.getLogger(AgenciabancariaService.class);

    private final AgenciabancariaRepository agenciabancariaRepository;


    public AgenciabancariaService(AgenciabancariaRepository agenciabancariaRepository) {
        this.agenciabancariaRepository = agenciabancariaRepository;
    }

    /**
     * Save a agenciabancaria.
     *
     * @param agenciabancariaDTO the entity to save.
     * @return the persisted entity.
     */
    
    //@CacheEvict(value = {"br.com.mrcontador.domain.Parceiro","br.com.mrcontador.domain.Parceiro.agenciabancarias","br.com.mrcontador.domain.Parceiro.atividades","br.com.mrcontador.domain.Parceiro.socios"})
    public Agenciabancaria save(Agenciabancaria agenciabancaria) {
        log.debug("Request to save Agenciabancaria : {}", agenciabancaria);
        return  agenciabancariaRepository.save(agenciabancaria);
    }

    /**
     * Get all the agenciabancarias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Agenciabancaria> findAll(Pageable pageable) {
        log.debug("Request to get all Agenciabancarias");
        return agenciabancariaRepository.findAll(pageable);
    }


    /**
     * Get one agenciabancaria by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Agenciabancaria> findOne(Long id) {
        log.debug("Request to get Agenciabancaria : {}", id);
        return agenciabancariaRepository.findById(id);
    }

    /**
     * Delete the agenciabancaria by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Agenciabancaria : {}", id);
        agenciabancariaRepository.deleteById(id);
    }
}
