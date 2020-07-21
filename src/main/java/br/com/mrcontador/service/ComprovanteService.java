package br.com.mrcontador.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.comprovante.DiffPage;
import br.com.mrcontador.file.comprovante.DiffValue;
import br.com.mrcontador.repository.ComprovanteRepository;
import br.com.mrcontador.service.dto.ComprovanteDTO;
import br.com.mrcontador.service.mapper.ComprovanteMapper;

/**
 * Service Implementation for managing {@link Comprovante}.
 */
@Service
@Transactional
public class ComprovanteService {

    private final Logger log = LoggerFactory.getLogger(ComprovanteService.class);

    private final ComprovanteRepository comprovanteRepository;

    private final ComprovanteMapper comprovanteMapper;

    public ComprovanteService(ComprovanteRepository comprovanteRepository, ComprovanteMapper comprovanteMapper) {
        this.comprovanteRepository = comprovanteRepository;
        this.comprovanteMapper = comprovanteMapper;
    }

    /**
     * Save a comprovante.
     *
     * @param comprovanteDTO the entity to save.
     * @return the persisted entity.
     */
    public ComprovanteDTO save(ComprovanteDTO comprovanteDTO) {
        log.debug("Request to save Comprovante : {}", comprovanteDTO);
        Comprovante comprovante = comprovanteMapper.toEntity(comprovanteDTO);
        comprovante = comprovanteRepository.save(comprovante);
        return comprovanteMapper.toDto(comprovante);
    }

    /**
     * Get all the comprovantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ComprovanteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comprovantes");
        return comprovanteRepository.findAll(pageable)
            .map(comprovanteMapper::toDto);
    }


    /**
     * Get one comprovante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComprovanteDTO> findOne(Long id) {
        log.debug("Request to get Comprovante : {}", id);
        return comprovanteRepository.findById(id)
            .map(comprovanteMapper::toDto);
    }

    /**
     * Delete the comprovante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comprovante : {}", id);
        comprovanteRepository.deleteById(id);
    }
    
    public void save(List<DiffPage> diffPages,Banco banco, Parceiro parceiro) {
    	for(DiffPage diffPage  :diffPages) {
    			for(DiffValue diffValue : diffPage.getDiffValues()) {
    				System.out.println(diffValue.toString());
    			}
    	}
    }
}
