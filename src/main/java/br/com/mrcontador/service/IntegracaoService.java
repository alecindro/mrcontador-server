package br.com.mrcontador.service;

import br.com.mrcontador.domain.Integracao;
import br.com.mrcontador.repository.IntegracaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Integracao}.
 */
@Service
@Transactional
public class IntegracaoService {

    private final Logger log = LoggerFactory.getLogger(IntegracaoService.class);

    private final IntegracaoRepository integracaoRepository;

    public IntegracaoService(IntegracaoRepository integracaoRepository) {
        this.integracaoRepository = integracaoRepository;
    }

    /**
     * Save a integracao.
     *
     * @param integracao the entity to save.
     * @return the persisted entity.
     */
    public Integracao save(Integracao integracao) {
        log.debug("Request to save Integracao : {}", integracao);
        return integracaoRepository.save(integracao);
    }

    /**
     * Get all the integracaos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Integracao> findAll() {
        log.debug("Request to get all Integracaos");
        return integracaoRepository.findAll();
    }


    /**
     * Get one integracao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Integracao> findOne(Long id) {
        log.debug("Request to get Integracao : {}", id);
        return integracaoRepository.findById(id);
    }

    /**
     * Delete the integracao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Integracao : {}", id);
        integracaoRepository.deleteById(id);
    }
}
