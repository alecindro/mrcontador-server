package br.com.mrcontador.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.ContaRepository;

/**
 * Service Implementation for managing {@link Conta}.
 */
@Service
@Transactional
public class ContaService {

    private final Logger log = LoggerFactory.getLogger(ContaService.class);

    private final ContaRepository contaRepository;


    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }


    /**
     * Delete the conta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Conta : {}", id);
        contaRepository.deleteById(id);
    }
    
    public List<Conta> save(List<Conta> contas){
    	contas.forEach(conta -> {
    		conta.setDataCadastro(new Date());
    	});
    	return contaRepository.saveAll(contas);
    }
    
    public Optional<Conta> findFirstByParceiro(Parceiro parceiro){
    	return contaRepository.findFirstByParceiro(parceiro);
    }
    
    public Optional<Conta> findByConConta(Integer conConta){
    	return contaRepository.findByConConta(conConta);
    }
    
}
