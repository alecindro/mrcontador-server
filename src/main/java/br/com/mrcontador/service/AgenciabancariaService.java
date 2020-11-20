package br.com.mrcontador.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.TipoAgencia;
import br.com.mrcontador.repository.AgenciabancariaRepository;
import br.com.mrcontador.service.dto.AgenciabancariaAplicacao;

/**
 * Service Implementation for managing {@link Agenciabancaria}.
 */
@Service
@Transactional
public class AgenciabancariaService {

    private final Logger log = LoggerFactory.getLogger(AgenciabancariaService.class);

    private final AgenciabancariaRepository agenciabancariaRepository;
    
    private final ExtratoService extratoService;


    public AgenciabancariaService(AgenciabancariaRepository agenciabancariaRepository, ExtratoService extratoService) {
        this.agenciabancariaRepository = agenciabancariaRepository;
        this.extratoService = extratoService;
    }

    /**
     * Save a agenciabancaria.
     *
     * @param agenciabancariaDTO the entity to save.
     * @return the persisted entity.
     */
    
    //@CacheEvict(value = {"br.com.mrcontador.domain.Parceiro","br.com.mrcontador.domain.Parceiro.agenciabancarias","br.com.mrcontador.domain.Parceiro.atividades","br.com.mrcontador.domain.Parceiro.socios"})
    public Agenciabancaria save(Agenciabancaria agenciabancaria) {
        log.debug("Request to save Agenciabancaria : {}", agenciabancaria);
        return  agenciabancariaRepository.save(agenciabancaria);
    }
    
    public Agenciabancaria update(Agenciabancaria agenciabancaria) {
        log.debug("Request to save Agenciabancaria : {}", agenciabancaria);
        if(!agenciabancaria.isAgeSituacao()) {
        	Optional<Agenciabancaria> aplicacao = agenciabancariaRepository.findByParceiroAndTipoAgencia(agenciabancaria.getParceiro(), TipoAgencia.APLICACAO);
        	if(aplicacao.isPresent()) {
        		aplicacao.get().setAgeSituacao(false);
        		agenciabancariaRepository.save(aplicacao.get());
        	}
        }
        return  agenciabancariaRepository.save(agenciabancaria);
    }

    /**
     * Get all the agenciabancarias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Agenciabancaria> findAll(Pageable pageable) {
        log.debug("Request to get all Agenciabancarias");
        return agenciabancariaRepository.findAll(pageable);
    }


    /**
     * Get one agenciabancaria by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Agenciabancaria> findOne(Long id) {
        log.debug("Request to get Agenciabancaria : {}", id);
        return agenciabancariaRepository.findById(id);
    }

    /**
     * Delete the agenciabancaria by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Agenciabancaria : {}", id);
        Agenciabancaria agencia = agenciabancariaRepository.findById(id).get();
        agencia.setAgeSituacao(false);
        agenciabancariaRepository.save(agencia);
    }
    
    public Agenciabancaria createCaixa(Conta conta) {
    	Optional<Agenciabancaria> _agencia = agenciabancariaRepository.findByParceiroAndTipoAgencia(conta.getParceiro(), TipoAgencia.CAIXA);
    	if(!_agencia.isEmpty()) {
    		return _agencia.get();
    	}
    	Agenciabancaria agencia = new Agenciabancaria();
    	agencia.setAgeAgencia(TipoAgencia.CAIXA.name());
    	agencia.setAgeDescricao(TipoAgencia.CAIXA.name());
    	agencia.setAgeSituacao(false);
    	agencia.setParceiro(conta.getParceiro());
    	agencia.setTipoAgencia(TipoAgencia.CAIXA);
    	agencia.setConta(conta);
    	return agenciabancariaRepository.save(agencia);
    }
    public Agenciabancaria createAplicacao(AgenciabancariaAplicacao aplicacao) {
    	Agenciabancaria agenciabancaria = aplicacao.getAgenciaBancaria();
    	agenciabancaria.setPossueAplicacao(true);
    	agenciabancaria.setAgeSituacao(true);
    	agenciabancaria.setTipoAgencia(TipoAgencia.CONTA);
    	save(agenciabancaria);
    	Agenciabancaria agencia = new Agenciabancaria();
    	agencia.setAgeAgencia(agenciabancaria.getAgeAgencia());
    	agencia.setAgeDescricao(TipoAgencia.APLICACAO.name());
    	agencia.setAgeDigito(agenciabancaria.getAgeDigito());
    	agencia.setAgeNumero(agenciabancaria.getAgeNumero());
    	agencia.setBanCodigobancario(agenciabancaria.getBanCodigobancario());
    	agencia.setAgeSituacao(true);
    	agencia.setBanco(agenciabancaria.getBanco());
    	agencia.setParceiro(agenciabancaria.getParceiro());
    	agencia.setTipoAgencia(TipoAgencia.APLICACAO);
    	agencia.setConta(aplicacao.getConta());
    	agencia = save(agencia);
    	extratoService.callExtratoAplicacao(agenciabancaria.getId());
    	return agencia;
    }
}
