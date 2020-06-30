package br.com.mrcontador.service;

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.repository.ExtratoRepository;
import br.com.mrcontador.service.dto.ExtratoDTO;
import br.com.mrcontador.service.mapper.ExtratoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Extrato}.
 */
@Service
@Transactional
public class ExtratoService {

    private final Logger log = LoggerFactory.getLogger(ExtratoService.class);

    private final ExtratoRepository extratoRepository;

    private final ExtratoMapper extratoMapper;

    public ExtratoService(ExtratoRepository extratoRepository, ExtratoMapper extratoMapper) {
        this.extratoRepository = extratoRepository;
        this.extratoMapper = extratoMapper;
    }

    /**
     * Save a extrato.
     *
     * @param extratoDTO the entity to save.
     * @return the persisted entity.
     */
    public ExtratoDTO save(ExtratoDTO extratoDTO) {
        log.debug("Request to save Extrato : {}", extratoDTO);
        Extrato extrato = extratoMapper.toEntity(extratoDTO);
        extrato = extratoRepository.save(extrato);
        return extratoMapper.toDto(extrato);
    }

    /**
     * Get all the extratoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExtratoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Extratoes");
        return extratoRepository.findAll(pageable)
            .map(extratoMapper::toDto);
    }


    /**
     * Get one extrato by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExtratoDTO> findOne(Long id) {
        log.debug("Request to get Extrato : {}", id);
        return extratoRepository.findById(id)
            .map(extratoMapper::toDto);
    }

    /**
     * Delete the extrato by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Extrato : {}", id);
        extratoRepository.deleteById(id);
    }
}
