package br.com.mrcontador.service;

import java.time.Instant;
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
    	log.debug("total de contas: {}", contas.size());
    	contas.forEach(conta -> {
    		conta.setDataCadastro(new Date());
    	});
    	contas = contaRepository.saveAll(contas);
    	log.debug("Contas salvas ");
    	return contas;
    	
    }
    
    public List<Conta> update(List<Conta> contas, String user, Parceiro parceiro ){
    	Date dataCadastro = new Date();
    	Instant created = Instant.now();
    	contas.forEach(conta -> {
    		try {
    		contaRepository.createConta(dataCadastro, conta.getConClassificacao(), conta.getConTipo(), conta.getConDescricao(), conta.getConCnpj(), conta.getConGrau(), conta.getConConta(), created, user,parceiro.getId());
    		contaRepository.updateConta(conta.getConClassificacao(), conta.getConTipo(), conta.getConDescricao(), conta.getConCnpj(), conta.getConGrau(),  created, user, conta.getConConta(), parceiro.getId());
    		} catch(Exception e) {
    			log.error(e.getMessage(),e);
    			throw new RuntimeException();
    		}
    	});
    	contaRepository.callContaUpdateFunction(parceiro.getId());
    	contas = contaRepository.findByParceiro(parceiro);
    	
    	return contas;
    	
    }
    
    public Optional<Conta> findFirstByParceiro(Parceiro parceiro){
    	return contaRepository.findFirstByParceiro(parceiro);
    }
    
    public Optional<Conta> findByConConta(Integer conConta){
    	return contaRepository.findByConConta(conConta);
    }
    
    public Optional<Conta> findById(Long id){
    	return contaRepository.findById(id);
    }
    
    public void updateArquivo( Long id, Long arquivoId) {
    	contaRepository.updateArquivo(id, arquivoId);
    }
    
    public int callContaFunction(Long parceiroId, String periodo) {
    	return contaRepository.callContaFunction(parceiroId, periodo);
    }
    
    public int callContaFunctionInteligent(Long inteligentId) {
    	return contaRepository.callContaFunctionInteligent(inteligentId);
    }
    
}
