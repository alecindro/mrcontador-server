package br.com.mrcontador.web.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.file.S3Service;

@RestController
@RequestMapping("/api")
public class DownloadFileResource {
	
	private final Logger log = LoggerFactory.getLogger(DownloadFileResource.class);
	
	private final S3Service s3Service;
	private final ComprovanteService comprovanteService;
	
	public DownloadFileResource(S3Service s3Service, ComprovanteService comprovanteService) {
		this.s3Service = s3Service;
		this.comprovanteService = comprovanteService;
	}



	@GetMapping("/downloadFile/comprovante/{id}")
    public ResponseEntity<Resource> downloadComprovante(@PathVariable Long id) {
		log.info("download comprovante de id {}",id);
		Optional<Comprovante> comprovante =  comprovanteService.findOne(id);
		if(comprovante.isEmpty()) {
			log.error("comprovante não encontrado. ID: {} ", id);
			throw new MrContadorException("comprovante.notfound");
		}
		Arquivo arquivo = comprovante.get().getArquivo();
		if(arquivo == null) {
			log.error("arquivo não encontrado. Comprovante ID: {} ", id);
			throw new MrContadorException("arquivo.processing");
		}
		String key = arquivo.gets3Dir()+"/"+arquivo.getNome();
		byte[] file = s3Service.downloadByteArray(key);
		Resource resource = new ByteArrayResource(file);
		return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getTipoArquivo()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeOriginal() + "\"")
                .body(resource);
	}
	
	
}
