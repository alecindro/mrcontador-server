package br.com.mrcontador.service;

import br.com.mrcontador.config.liquibase.LiquibaseMultiTenantcy;
import br.com.mrcontador.domain.Contador;
import br.com.mrcontador.repository.ContadorRepository;
import br.com.mrcontador.service.dto.ContadorDTO;
import br.com.mrcontador.service.mapper.ContadorMapper;
import liquibase.integration.spring.SpringLiquibase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import javax.sql.DataSource;

/**
 * Service Implementation for managing {@link Contador}.
 */
@Service
@Transactional
public class ContadorService {

    private final Logger log = LoggerFactory.getLogger(ContadorService.class);

    private final ContadorRepository contadorRepository;

    private final ContadorMapper contadorMapper;
    
    private final DataSource dataSource; 

    public ContadorService(ContadorRepository contadorRepository, ContadorMapper contadorMapper, DataSource dataSource) {
        this.contadorRepository = contadorRepository;
        this.contadorMapper = contadorMapper;
        this.dataSource = dataSource;
    }

    /**
     * Save a contador.
     *
     * @param contadorDTO the entity to save.
     * @return the persisted entity.
     * @throws Exception 
     */
    public ContadorDTO save(ContadorDTO contadorDTO) throws Exception {
        log.debug("Request to save Contador : {}", contadorDTO);
        Contador contador = contadorMapper.toEntity(contadorDTO);
        contador = contadorRepository.save(contador);
        LiquibaseMultiTenantcy liquibase = new LiquibaseMultiTenantcy();
        liquibase.createSchema(contador.getDatasource(), dataSource);
        return contadorMapper.toDto(contador);
    }
    
    public ContadorDTO update(ContadorDTO contadorDTO) {
        log.debug("Request to save Contador : {}", contadorDTO);
        Contador contador = contadorMapper.toEntity(contadorDTO);
        contador = contadorRepository.save(contador);
        return contadorMapper.toDto(contador);
    }

    /**
     * Get all the contadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContadorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contadors");
        return contadorRepository.findAll(pageable)
            .map(contadorMapper::toDto);
    }


    /**
     * Get one contador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContadorDTO> findOne(Long id) {
        log.debug("Request to get Contador : {}", id);
        return contadorRepository.findById(id)
            .map(contadorMapper::toDto);
    }

    /**
     * Delete the contador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contador : {}", id);
        contadorRepository.deleteById(id);
    }
}
