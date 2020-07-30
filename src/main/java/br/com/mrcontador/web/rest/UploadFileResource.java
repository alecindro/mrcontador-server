package br.com.mrcontador.web.rest;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileService;
import br.com.mrcontador.file.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import io.github.jhipster.web.util.HeaderUtil;


@RestController
@RequestMapping("/api")
public class UploadFileResource {

	private final Logger log = LoggerFactory.getLogger(UploadFileResource.class);

	private final FileService fileService;
	
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

	public UploadFileResource(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/upload/planoconta")
	public ResponseEntity<Parceiro> uploadPlanoConta(@RequestParam("file") MultipartFile file, @RequestParam(required = false,name = "parceiroCNPJ") String parceiroCnpj) throws Exception{
		log.info("Processando arquivo: {}. Cliente: {}",file.getName(), SecurityUtils.getCurrentTenantHeader());
		try {
			Parceiro parceiro = fileService.processPlanoConta(file, SecurityUtils.getCurrentUserLogin(), SecurityUtils.getCurrentTenantHeader(),parceiroCnpj,SistemaPlanoConta.DOMINIO_SISTEMAS);
		 return ResponseEntity.created(new URI("/api/uploadplanoconta/"))
	                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "uploadPlanoConta", file.getName())).body(parceiro);
		} catch (MrContadorException e) {
			throw e;
			//throw new CustomParameterizedException(e.getErrorKey(),e.getDefaultMessasge());

			//  throw new BadRequestAlertException(e.getDefaultMessasge(), "arquivo", e.getErrorKey());
	    	   //return ResponseEntity.badRequest().headers(MessageUtil.generate(applicationName, e)).build();
	       }
	}

}
