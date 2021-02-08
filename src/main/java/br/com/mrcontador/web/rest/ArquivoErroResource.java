package br.com.mrcontador.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.domain.Contador;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileService;
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.ArquivoErroQueryService;
import br.com.mrcontador.service.ArquivoErroService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.ArquivoErroCriteria;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mrcontador.domain.ArquivoErro}.
 */
@RestController
@RequestMapping("/api")
public class ArquivoErroResource {

    private final Logger log = LoggerFactory.getLogger(ArquivoErroResource.class);

    private static final String ENTITY_NAME = "arquivoerro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArquivoErroService arquivoErroService;
    
    private final S3Service s3Service;

    private final ArquivoErroQueryService arquivoErroQueryService;
    
    private final ParceiroService parceiroService;
    
	private final FileService fileService;
	private final AgenciabancariaService agenciabancariaService;

    public ArquivoErroResource(ArquivoErroService arquivoErroService, 
    		ArquivoErroQueryService arquivoErroQueryService,
    		S3Service s3Service,
    		ParceiroService parceiroService,
    		FileService fileService,
    		AgenciabancariaService agenciabancariaService) {
        this.arquivoErroService = arquivoErroService;
        this.arquivoErroQueryService = arquivoErroQueryService;
        this.s3Service = s3Service;
        this.parceiroService = parceiroService;
    	this.fileService = fileService;
		this.agenciabancariaService = agenciabancariaService;
    }

    /**
     * {@code POST  /arquivo-erros} : Create a new arquivoErro.
     *
     * @param arquivoErro the arquivoErro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arquivoErro, or with status {@code 400 (Bad Request)} if the arquivoErro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arquivo-erros")
    public ResponseEntity<Void> processArquivoErro(@RequestBody ArquivoErro arquivoErro) throws URISyntaxException {
        log.debug("REST request to save ArquivoErro : {}", arquivoErro);
        if (arquivoErro.getId() == null) {
            throw new BadRequestAlertException("A new arquivoErro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        try {
			process(arquivoErro);
        }catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new MrContadorException("comprovante.imported");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new MrContadorException(e.getMessage());
		}
        
        return ResponseEntity.created(new URI("/api/arquivo-erros/" + arquivoErro.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, arquivoErro.getNome()))
            .build();
    }



    /**
     * {@code GET  /arquivo-erros} : get all the arquivoErros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arquivoErros in body.
     */
    @GetMapping("/arquivo-erros")
    public ResponseEntity<List<ArquivoErro>> getAllArquivoErros(ArquivoErroCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ArquivoErros by criteria: {}", criteria);
        Page<ArquivoErro> page = arquivoErroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arquivo-erros/count} : count all the arquivoErros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/arquivo-erros/count")
    public ResponseEntity<Long> countArquivoErros(ArquivoErroCriteria criteria) {
        log.debug("REST request to count ArquivoErros by criteria: {}", criteria);
        return ResponseEntity.ok().body(arquivoErroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arquivo-erros/:id} : get the "id" arquivoErro.
     *
     * @param id the id of the arquivoErro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arquivoErro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arquivo-erros/{id}")
    public ResponseEntity<ArquivoErro> getArquivoErro(@PathVariable Long id) {
        log.debug("REST request to get ArquivoErro : {}", id);
        Optional<ArquivoErro> arquivoErro = arquivoErroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arquivoErro);
    }

    /**
     * {@code DELETE  /arquivo-erros/:id} : delete the "id" arquivoErro.
     *
     * @param id the id of the arquivoErro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arquivo-erros/{id}")
    public ResponseEntity<Void> invalidArquivoErro(@PathVariable Long id) {
        log.debug("REST request to delete ArquivoErro : {}", id);
        Optional<ArquivoErro> oArquivo = arquivoErroService.findOne(id);
        if(oArquivo.isEmpty()) {
        	throw new MrContadorException("arquivo.notfound");
        }
        ArquivoErro arquivo = oArquivo.get().valido(false);
        arquivoErroService.save(arquivo);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
    
    private void process(ArquivoErro arquivoErro) throws Exception {
    	String tenantAtual = TenantContext.getTenantSchema();
		TenantContext.setTenantSchema(arquivoErro.getSchema());
		String key = arquivoErro.getS3Dir() + "/" + arquivoErro.getNome();
		InputStream stream = s3Service.downloadStream(key);
		Parceiro parceiro = parceiroService.findOne(arquivoErro.getParceiroId()).get();
		FileDTO fileDTO = new FileDTO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stream.transferTo(baos);
		fileDTO.setTipoDocumento(arquivoErro.getTipoDocumento());
		fileDTO.setParceiro(parceiro);
		fileDTO.setName(arquivoErro.getNome());
		fileDTO.setOriginalFilename(arquivoErro.getNomeOriginal());
		fileDTO.setUsuario(SecurityUtils.getCurrentUserLogin().get());
		fileDTO.setContador(arquivoErro.getSchema());
		fileDTO.setOutputStream(baos);
		fileDTO.setIdAgencia(arquivoErro.getIdAgencia());
		fileDTO.setContentType(arquivoErro.getContentType());
		switch (arquivoErro.getTipoDocumento()) {
		case COMPROVANTE:
			processComprovante(fileDTO);
			break;
		case EXTRATO:
			processExtrato(fileDTO, arquivoErro.getTipoArquivo());
			break;
		case NOTA:
			processNotaFiscal(fileDTO);
			break;
		case PLANO_DE_CONTA:
			processPlanoConta(fileDTO, arquivoErro.getContador());
			break;
		default:
			break;
		}
		TenantContext.setTenantSchema(tenantAtual);
		arquivoErro.setProcessado(true);
		arquivoErroService.save(arquivoErro);
	}

	private void processComprovante(FileDTO fileDTO) {
		Agenciabancaria agenciabancaria = agenciabancariaService.findOne(fileDTO.getIdAgencia()).get();
		fileService.processComprovante(fileDTO, agenciabancaria);
	}

	private void processExtrato(FileDTO fileDTO, String tipoArquivo) {
		Agenciabancaria agenciabancaria = agenciabancariaService.findOne(fileDTO.getIdAgencia()).get();
		fileService.processExtrato(fileDTO, agenciabancaria, tipoArquivo);
	}

	private void processNotaFiscal(FileDTO fileDTO) throws Exception {
		fileService.processNFE(fileDTO);
	}

	private void processPlanoConta(FileDTO fileDTO, Contador contador) {
		SistemaPlanoConta sistemaPlanoConta = SistemaPlanoConta.valueOf(contador.getSistema());
		fileService.processPlanoConta(fileDTO, sistemaPlanoConta);
	}

}
