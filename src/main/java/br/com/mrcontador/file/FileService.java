package br.com.mrcontador.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.pdf.PdfParserPlanoConta;
import br.com.mrcontador.file.xml.XmlParserDefault;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;

@Service
public class FileService {

	@Autowired
	private PdfParserPlanoConta pdfParserPlanoConta;
	@Autowired
	private XmlParserDefault xmlParserDefault;
	@Autowired
	private S3Service s3Service;
	
	public Parceiro processPlanoConta(MultipartFile file, Optional<String> usuario, String contador,String cnpjParceiro,  SistemaPlanoConta sistemaPlanoConta) {
		FileDTO dto = getFileDTO(file, usuario, contador);
		return pdfParserPlanoConta.process(dto, sistemaPlanoConta, cnpjParceiro);
	}

	
	private FileDTO getFileDTO(MultipartFile file, Optional<String> usuario, String contador) {
		FileDTO fileDTO = new FileDTO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			file.getInputStream().transferTo(baos);
			String media = com.google.common.net.MediaType.parse(file.getContentType()).subtype();
			switch (media) {
			case "pdf":
				break;
			case "xml": {
				break;
			}
			default:
				throw new MrContadorException("file.no.implemented.error", fileDTO.getContentType());
			}
			fileDTO.setContentType(file.getContentType());
			fileDTO.setOriginalFilename(file.getOriginalFilename());
			fileDTO.setSize(file.getSize());
			fileDTO.setUsuario(usuario.isPresent()?usuario.get():"");
			fileDTO.setContador(contador);
			fileDTO.setInputStream(file.getInputStream());
			return fileDTO;
		}catch (MrContadorException e){
			throw e;
		}
		catch (Exception e) {
        	TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			fileDTO.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
			s3Service.uploadErro(fileDTO);
			throw new MrContadorException("file.process.error", file.getOriginalFilename());
		}
	}
	public void processNFE(FileDTO fileDTO) {
		xmlParserDefault.process(fileDTO);
		
	}
	
	public void processNFE(MultipartFile file, Optional<String> usuario, String contador) {
		FileDTO fileDTO = getFileDTO(file, usuario, contador);
		xmlParserDefault.process(fileDTO);
		
	}
	
	


}
