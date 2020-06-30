package br.com.mrcontador.service;

import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.repository.BancoRepository;
import br.com.mrcontador.service.dto.BancoDTO;
import br.com.mrcontador.service.mapper.BancoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Banco}.
 */
@Service
@Transactional
public class BancoService {

    private final Logger log = LoggerFactory.getLogger(BancoService.class);

    private final BancoRepository bancoRepository;

    private final BancoMapper bancoMapper;

    public BancoService(BancoRepository bancoRepository, BancoMapper bancoMapper) {
        this.bancoRepository = bancoRepository;
        this.bancoMapper = bancoMapper;
    }

    /**
     * Save a banco.
     *
     * @param bancoDTO the entity to save.
     * @return the persisted entity.
     */
    public BancoDTO save(BancoDTO bancoDTO) {
        log.debug("Request to save Banco : {}", bancoDTO);
        Banco banco = bancoMapper.toEntity(bancoDTO);
        banco = bancoRepository.save(banco);
        return bancoMapper.toDto(banco);
    }

    /**
     * Get all the bancos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BancoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bancos");
        return bancoRepository.findAll(pageable)
            .map(bancoMapper::toDto);
    }


    /**
     * Get one banco by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BancoDTO> findOne(Long id) {
        log.debug("Request to get Banco : {}", id);
        return bancoRepository.findById(id)
            .map(bancoMapper::toDto);
    }

    /**
     * Delete the banco by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Banco : {}", id);
        bancoRepository.deleteById(id);
    }
}
