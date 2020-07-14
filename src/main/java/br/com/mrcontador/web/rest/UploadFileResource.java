package br.com.mrcontador.web.rest;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileService;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.util.MessageUtil;


@RestController
@RequestMapping("/api")
public class UploadFileResource {

	private final Logger log = LoggerFactory.getLogger(UploadFileResource.class);

	private final FileService fileService;

	public UploadFileResource(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file, HttpRequest request) throws Exception{
		log.info("Processando arquivo: {}. Cliente: {}",file.getName(), SecurityUtils.getCurrentTenantHeader());
		try {
		fileService.process(file, SecurityUtils.getCurrentUserLogin(), SecurityUtils.getCurrentTenantHeader());
		return ResponseEntity.ok().build();
		} catch (MrContadorException e) {
	    	   return ResponseEntity.badRequest().headers(MessageUtil.generate("UploadFileResource", e)).build();
	       }
	}

}
