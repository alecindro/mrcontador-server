package br.com.mrcontador.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.repository.NotafiscalRepository;
import br.com.mrcontador.service.mapper.NotafiscalNfe310Mapper;
import br.com.mrcontador.service.mapper.NotafiscalNfe400Mapper;

/**
 * Service Implementation for managing {@link Notafiscal}.
 */
@Service
@Transactional
public class NotafiscalService {

    private final Logger log = LoggerFactory.getLogger(NotafiscalService.class);

    private final NotafiscalRepository notafiscalRepository;

    public NotafiscalService(NotafiscalRepository notafiscalRepository) {
        this.notafiscalRepository = notafiscalRepository;;
    }

    /**
     * Save a notafiscal.
     *
     * @param notafiscalDTO the entity to save.
     * @return the persisted entity.
     */
    public Notafiscal save(Notafiscal notafiscal) {
        log.debug("Request to save Notafiscal : {}", notafiscal);
        return notafiscalRepository.save(notafiscal);
    }
    public List<Notafiscal> saveAll(List<Notafiscal> notas) {
        log.debug("Request to saveAll Notafiscal : {}", notas);
        return notafiscalRepository.saveAll(notas);
    }

    /**
     * Get all the notafiscals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Notafiscal> findAll(Pageable pageable) {
        log.debug("Request to get all Notafiscals");
        return notafiscalRepository.findAll(pageable);
    }


    /**
     * Get one notafiscal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Notafiscal> findOne(Long id) {
        log.debug("Request to get Notafiscal : {}", id);
        return notafiscalRepository.findById(id);
    }

    /**
     * Delete the notafiscal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notafiscal : {}", id);
        notafiscalRepository.deleteById(id);
    }
    
    public List<Notafiscal>  process(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfNotaProcessada, Parceiro parceiro, boolean isEmitente,Arquivo pdf, Arquivo xml) {
    	NotafiscalNfe400Mapper mapper = new NotafiscalNfe400Mapper();
    	List<Notafiscal> list = mapper.toEntity(nfNotaProcessada, parceiro, isEmitente,pdf,xml);    
    	return notafiscalRepository.saveAll(list);
    	
    }
    public List<Notafiscal>  process(com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfNotaProcessada, Parceiro parceiro, boolean isEmitente,Arquivo pdf, Arquivo xml) {
    	NotafiscalNfe310Mapper mapper = new NotafiscalNfe310Mapper();
    	List<Notafiscal> list = mapper.toEntity(nfNotaProcessada, parceiro, isEmitente);
    	list.forEach(nota ->{
    		nota.setArquivo(xml);
    		nota.setArquivoPDF(pdf);
    	});
    	return notafiscalRepository.saveAll(list);
    }
    
   
}
