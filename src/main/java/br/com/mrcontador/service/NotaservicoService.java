package br.com.mrcontador.service;

import br.com.mrcontador.domain.Notaservico;
import br.com.mrcontador.repository.NotaservicoRepository;
import br.com.mrcontador.service.dto.NotaservicoDTO;
import br.com.mrcontador.service.mapper.NotaservicoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Notaservico}.
 */
@Service
@Transactional
public class NotaservicoService {

    private final Logger log = LoggerFactory.getLogger(NotaservicoService.class);

    private final NotaservicoRepository notaservicoRepository;

    private final NotaservicoMapper notaservicoMapper;

    public NotaservicoService(NotaservicoRepository notaservicoRepository, NotaservicoMapper notaservicoMapper) {
        this.notaservicoRepository = notaservicoRepository;
        this.notaservicoMapper = notaservicoMapper;
    }

    /**
     * Save a notaservico.
     *
     * @param notaservicoDTO the entity to save.
     * @return the persisted entity.
     */
    public NotaservicoDTO save(NotaservicoDTO notaservicoDTO) {
        log.debug("Request to save Notaservico : {}", notaservicoDTO);
        Notaservico notaservico = notaservicoMapper.toEntity(notaservicoDTO);
        notaservico = notaservicoRepository.save(notaservico);
        return notaservicoMapper.toDto(notaservico);
    }

    /**
     * Get all the notaservicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotaservicoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notaservicos");
        return notaservicoRepository.findAll(pageable)
            .map(notaservicoMapper::toDto);
    }


    /**
     * Get one notaservico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotaservicoDTO> findOne(Long id) {
        log.debug("Request to get Notaservico : {}", id);
        return notaservicoRepository.findById(id)
            .map(notaservicoMapper::toDto);
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
