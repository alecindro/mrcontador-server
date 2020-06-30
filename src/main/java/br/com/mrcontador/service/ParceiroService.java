package br.com.mrcontador.service;

import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.ParceiroRepository;
import br.com.mrcontador.service.dto.ParceiroDTO;
import br.com.mrcontador.service.mapper.ParceiroMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Parceiro}.
 */
@Service
@Transactional
public class ParceiroService {

    private final Logger log = LoggerFactory.getLogger(ParceiroService.class);

    private final ParceiroRepository parceiroRepository;

    private final ParceiroMapper parceiroMapper;

    public ParceiroService(ParceiroRepository parceiroRepository, ParceiroMapper parceiroMapper) {
        this.parceiroRepository = parceiroRepository;
        this.parceiroMapper = parceiroMapper;
    }

    /**
     * Save a parceiro.
     *
     * @param parceiroDTO the entity to save.
     * @return the persisted entity.
     */
    public ParceiroDTO save(ParceiroDTO parceiroDTO) {
        log.debug("Request to save Parceiro : {}", parceiroDTO);
        Parceiro parceiro = parceiroMapper.toEntity(parceiroDTO);
        parceiro = parceiroRepository.save(parceiro);
        return parceiroMapper.toDto(parceiro);
    }

    /**
     * Get all the parceiros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParceiroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parceiros");
        return parceiroRepository.findAll(pageable)
            .map(parceiroMapper::toDto);
    }


    /**
     * Get one parceiro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParceiroDTO> findOne(Long id) {
        log.debug("Request to get Parceiro : {}", id);
        return parceiroRepository.findById(id)
            .map(parceiroMapper::toDto);
    }

    /**
     * Delete the parceiro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parceiro : {}", id);
        parceiroRepository.deleteById(id);
    }
}
