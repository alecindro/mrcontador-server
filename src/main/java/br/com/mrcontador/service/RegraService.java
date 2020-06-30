package br.com.mrcontador.service;

import br.com.mrcontador.domain.Regra;
import br.com.mrcontador.repository.RegraRepository;
import br.com.mrcontador.service.dto.RegraDTO;
import br.com.mrcontador.service.mapper.RegraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Regra}.
 */
@Service
@Transactional
public class RegraService {

    private final Logger log = LoggerFactory.getLogger(RegraService.class);

    private final RegraRepository regraRepository;

    private final RegraMapper regraMapper;

    public RegraService(RegraRepository regraRepository, RegraMapper regraMapper) {
        this.regraRepository = regraRepository;
        this.regraMapper = regraMapper;
    }

    /**
     * Save a regra.
     *
     * @param regraDTO the entity to save.
     * @return the persisted entity.
     */
    public RegraDTO save(RegraDTO regraDTO) {
        log.debug("Request to save Regra : {}", regraDTO);
        Regra regra = regraMapper.toEntity(regraDTO);
        regra = regraRepository.save(regra);
        return regraMapper.toDto(regra);
    }

    /**
     * Get all the regras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RegraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Regras");
        return regraRepository.findAll(pageable)
            .map(regraMapper::toDto);
    }


    /**
     * Get one regra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RegraDTO> findOne(Long id) {
        log.debug("Request to get Regra : {}", id);
        return regraRepository.findById(id)
            .map(regraMapper::toDto);
    }

    /**
     * Delete the regra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Regra : {}", id);
        regraRepository.deleteById(id);
    }
}
