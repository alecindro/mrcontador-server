package br.com.mrcontador.web.rest;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.mrcontador.service.file.S3Service;

@RestController
@RequestMapping("/api")
public class UploadFileResource {
	
	private final Logger log = LoggerFactory.getLogger(UploadFileResource.class);
    
	private final S3Service fileService;
	
	public UploadFileResource(S3Service fileService) {
		this.fileService = fileService;
	}
    
    @PostMapping("/uploadFile")
    public void uploadFile(@RequestParam("file") MultipartFile file, HttpRequest request) {
    /*	try {
			fileService.save(file.getInputStream());
		} catch (IOException | SAXException | TikaException e) {
			log.error(e.getMessage(),e);
		}*/
    	//request.get
    	//file.getInputStream();
    }

}
