package br.com.mrcontador.service;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.repository.AgenciabancariaRepository;
import br.com.mrcontador.service.dto.AgenciabancariaDTO;
import br.com.mrcontador.service.mapper.AgenciabancariaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Agenciabancaria}.
 */
@Service
@Transactional
public class AgenciabancariaService {

    private final Logger log = LoggerFactory.getLogger(AgenciabancariaService.class);

    private final AgenciabancariaRepository agenciabancariaRepository;

    private final AgenciabancariaMapper agenciabancariaMapper;

    public AgenciabancariaService(AgenciabancariaRepository agenciabancariaRepository, AgenciabancariaMapper agenciabancariaMapper) {
        this.agenciabancariaRepository = agenciabancariaRepository;
        this.agenciabancariaMapper = agenciabancariaMapper;
    }

    /**
     * Save a agenciabancaria.
     *
     * @param agenciabancariaDTO the entity to save.
     * @return the persisted entity.
     */
    public AgenciabancariaDTO save(AgenciabancariaDTO agenciabancariaDTO) {
        log.debug("Request to save Agenciabancaria : {}", agenciabancariaDTO);
        Agenciabancaria agenciabancaria = agenciabancariaMapper.toEntity(agenciabancariaDTO);
        agenciabancaria = agenciabancariaRepository.save(agenciabancaria);
        return agenciabancariaMapper.toDto(agenciabancaria);
    }

    /**
     * Get all the agenciabancarias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AgenciabancariaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agenciabancarias");
        return agenciabancariaRepository.findAll(pageable)
            .map(agenciabancariaMapper::toDto);
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
