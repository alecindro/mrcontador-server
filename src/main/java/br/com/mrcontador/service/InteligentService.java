package br.com.mrcontador.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.config.type.TipoRegra;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.domain.Regra;
import br.com.mrcontador.repository.InteligentRepository;
import br.com.mrcontador.util.MrContadorUtil;

/**
 * Service Implementation for managing {@link Inteligent}.
 */
@Service
@Transactional
public class InteligentService {

    private final Logger log = LoggerFactory.getLogger(InteligentService.class);

    private final InteligentRepository inteligentRepository;
    

    public InteligentService(InteligentRepository inteligentRepository) {
        this.inteligentRepository = inteligentRepository;
    }

    /**
     * Save a inteligent.
     *
     * @param inteligent the entity to save.
     * @return the persisted entity.
     */
    public Inteligent save(Inteligent inteligent) {
        log.debug("Request to save Inteligent : {}", inteligent);
        return inteligentRepository.save(inteligent);
    }

    /**
     * Get all the inteligents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Inteligent> findAll(Pageable pageable) {
        log.debug("Request to get all Inteligents");
        return inteligentRepository.findAll(pageable);
    }


    /**
     * Get one inteligent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Inteligent> findOne(Long id) {
        log.debug("Request to get Inteligent : {}", id);
        return inteligentRepository.findById(id);
    }

    /**
     * Delete the inteligent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Inteligent : {}", id);
        inteligentRepository.deleteById(id);
    }
    
    public int processInteligent(Long parceiroID, Long agenciaBancariaID, String periodo) {
    	LocalDate begin = MrContadorUtil.initalForPeriodo(periodo);
		LocalDate end = MrContadorUtil.lastForPeriodo(periodo);
		return inteligentRepository.callInteligent(parceiroID, agenciaBancariaID, Date.valueOf(begin), Date.valueOf(end));
    }
    
   
    public void createFromRegra(Regra regra) {
    	if(regra.getTipoRegra().equals(TipoRegra.HISTORICO)) {
    		inteligentRepository.updateFromHistoricoRegra(regra.getRegHistorico(), regra.getRegDescricao(), regra.getConta().getId(), regra.getParceiro().getId(), regra.getId());
    	}
    	if(regra.getTipoRegra().equals(TipoRegra.INFORMACAO_ADICIONAL)) {
    		inteligentRepository.updateFromInformacaoRegra(regra.getRegHistorico(), regra.getRegDescricao(), regra.getConta().getId(), regra.getParceiro().getId(),regra.getId());
    	}
    }
    public void updateFromRegra(Regra regra) {
    	inteligentRepository.updateRegra(regra.getRegHistorico(), regra.getParceiro().getId(), regra.getId(), regra.getConta().getId());
    }
    
    public void deleteRegra(Regra regra) {
    	inteligentRepository.deleteRegra(regra.getId(),regra.getParceiro().getId());
    }
}
