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
import br.com.mrcontador.repository.BancoRepository;
import br.com.mrcontador.service.mapper.BancoMapper;

/**
 * Service Implementation for managing {@link Banco}.
 */
@Service
@Transactional
public class BancoService {

    private final Logger log = LoggerFactory.getLogger(BancoService.class);

    private final BancoRepository bancoRepository;

      public BancoService(BancoRepository bancoRepository, BancoMapper bancoMapper) {
        this.bancoRepository = bancoRepository;
    }

    /**
     * Save a banco.
     *
     * @param bancoDTO the entity to save.
     * @return the persisted entity.
     */
    public Banco save(Banco banco) {
        log.debug("Request to save Banco : {}", banco);
        return  bancoRepository.save(banco);
    }

    /**
     * Get all the bancos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Banco> findAll(Pageable pageable) {
        log.debug("Request to get all Bancos");
        return bancoRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public List<Banco> findAll() {
        log.debug("Request to get all Bancos");
        return bancoRepository.findAll();
    }


    /**
     * Get one banco by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Banco> findOne(Long id) {
        log.debug("Request to get Banco : {}", id);
        return bancoRepository.findById(id);
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
