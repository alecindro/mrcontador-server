package br.com.mrcontador.file;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.pdf.PdfParserDefault;
import br.com.mrcontador.file.xml.XmlParserDefault;
import br.com.mrcontador.service.dto.FileDTO;

@Service
public class FileService {

	@Autowired
	private PdfParserDefault pdfParserDefault;
	@Autowired
	private XmlParserDefault xmlParserDefault;

	public void process(MultipartFile file, String usuario) {
		FileDTO fileDTO = new FileDTO();
		try {
			fileDTO.setContentType(file.getContentType());
			fileDTO.setOriginalFilename(file.getOriginalFilename());
			fileDTO.setSize(file.getSize());
			fileDTO.setUsuario(usuario);
			fileDTO.setInputStream(file.getInputStream());
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
		} catch (IOException e) {
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
