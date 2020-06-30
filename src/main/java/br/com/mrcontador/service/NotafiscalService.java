package br.com.mrcontador.service;

import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.repository.NotafiscalRepository;
import br.com.mrcontador.service.dto.NotafiscalDTO;
import br.com.mrcontador.service.mapper.NotafiscalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Notafiscal}.
 */
@Service
@Transactional
public class NotafiscalService {

    private final Logger log = LoggerFactory.getLogger(NotafiscalService.class);

    private final NotafiscalRepository notafiscalRepository;

    private final NotafiscalMapper notafiscalMapper;

    public NotafiscalService(NotafiscalRepository notafiscalRepository, NotafiscalMapper notafiscalMapper) {
        this.notafiscalRepository = notafiscalRepository;
        this.notafiscalMapper = notafiscalMapper;
    }

    /**
     * Save a notafiscal.
     *
     * @param notafiscalDTO the entity to save.
     * @return the persisted entity.
     */
    public NotafiscalDTO save(NotafiscalDTO notafiscalDTO) {
        log.debug("Request to save Notafiscal : {}", notafiscalDTO);
        Notafiscal notafiscal = notafiscalMapper.toEntity(notafiscalDTO);
        notafiscal = notafiscalRepository.save(notafiscal);
        return notafiscalMapper.toDto(notafiscal);
    }

    /**
     * Get all the notafiscals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotafiscalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notafiscals");
        return notafiscalRepository.findAll(pageable)
            .map(notafiscalMapper::toDto);
    }


    /**
     * Get one notafiscal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotafiscalDTO> findOne(Long id) {
        log.debug("Request to get Notafiscal : {}", id);
        return notafiscalRepository.findById(id)
            .map(notafiscalMapper::toDto);
    }

    /**
     * Delete the notafiscal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notafiscal : {}", id);
        notafiscalRepository.deleteById(id);
    }
}
