package br.com.mrcontador.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.pdf.PdfParserDefault;
import br.com.mrcontador.file.xml.XmlParserDefault;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;

@Service
public class FileService {

	@Autowired
	private PdfParserDefault pdfParserDefault;
	@Autowired
	private XmlParserDefault xmlParserDefault;
	@Autowired
	private S3Service s3Service;

	public void process(MultipartFile file, Optional<String> usuario, String contador) {
		FileDTO fileDTO = new FileDTO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			fileDTO.setContentType(file.getContentType());
			fileDTO.setOriginalFilename(file.getOriginalFilename());
			fileDTO.setSize(file.getSize());
			fileDTO.setUsuario(usuario.isPresent()?usuario.get():"");
			fileDTO.setContador(contador);
			fileDTO.setInputStream(file.getInputStream());
			file.getInputStream().transferTo(baos);
			String media = com.google.common.net.MediaType.parse(fileDTO.getContentType()).subtype();
			switch (media) {
			case "pdf":
				pdfParserDefault.process(fileDTO);
				break;
			case "xml": {
				xmlParserDefault.process(fileDTO);
				break;
			}
			default:
				throw new MrContadorException("error.file.no.implemented", fileDTO.getContentType());
			}
		}catch (MrContadorException e){
			throw e;
		}
		catch (Exception e) {
        	TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			fileDTO.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
			s3Service.uploadErro(fileDTO);
			throw new MrContadorException("error.file.process", file.getOriginalFilename());
		}
	}

	public void process(FileDTO fileDTO) {
			String media = com.google.common.net.MediaType.parse(fileDTO.getContentType()).subtype();
			switch (media) {
			case "pdf":
				pdfParserDefault.process(fileDTO);
				break;
			case "xml": {
				xmlParserDefault.process(fileDTO);
				break;
			}
			default:
				throw new MrContadorException("error.file.no.implemented", fileDTO.getContentType());
			}
		
	}

}
