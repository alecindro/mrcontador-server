package br.com.mrcontador.file;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.domain.TipoAgencia;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.comprovante.ParserComprovanteDefault;
import br.com.mrcontador.file.extrato.OfxParserDefault;
import br.com.mrcontador.file.extrato.PdfParserDefault;
import br.com.mrcontador.file.notafiscal.XmlParserDefault;
import br.com.mrcontador.file.planoconta.PdfParserPlanoConta;
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
import br.com.mrcontador.service.file.S3Service;

@Service
public class FileService {

	@Autowired
	private PdfParserPlanoConta pdfParserPlanoConta;
	@Autowired
	private XmlParserDefault xmlParserDefault;
	@Autowired
	private OfxParserDefault ofxParserDefault;
	@Autowired
	private PdfParserDefault pdfParserDefault;
	@Autowired
	private S3Service s3Service;
	@Autowired
	private ParserComprovanteDefault parserComprovante;

	public Parceiro processPlanoConta(FileDTO dto, Long parceiroId, SistemaPlanoConta sistemaPlanoConta) {
		return pdfParserPlanoConta.process(dto, sistemaPlanoConta, parceiroId);
	}

	public void updatePlanoConta(FileDTO dto, Long idParceiro, SistemaPlanoConta sistemaPlanoConta) {
		pdfParserPlanoConta.update(dto, sistemaPlanoConta, idParceiro);
	}

	public FileDTO getFileDTO(String contentType, String originalFilename, Long size, InputStream stream, Optional<String> usuario, String contador, Parceiro parceiro) {
		FileDTO fileDTO = new FileDTO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			stream.transferTo(baos);
			String media = com.google.common.net.MediaType.parse(contentType).subtype();
			switch (media) {
			case "pdf":
				break;
			case "xml":
				break;
			case "octet-stream":
				break;
			default:
				throw new MrContadorException("file.no.implemented.error", fileDTO.getContentType());
			}
			fileDTO.setParceiro(parceiro);
			fileDTO.setContentType(contentType);
			fileDTO.setOriginalFilename(originalFilename);
			fileDTO.setSize(size);
			fileDTO.setUsuario(usuario.isPresent() ? usuario.get() : "");
			fileDTO.setContador(contador);
			fileDTO.setOutputStream(baos);
			return fileDTO;
		} catch (MrContadorException e) {
			throw e;
		} catch (Exception e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			fileDTO.setOutputStream(baos);
			s3Service.uploadErro(fileDTO);
			throw new MrContadorException("file.process.error", originalFilename);
		}
	}

	public void processNFE(FileDTO fileDTO) throws Exception {
		fileDTO.setTipoDocumento(TipoDocumento.NOTA);
		xmlParserDefault.process(fileDTO);

	}

	public void processExtrato(FileDTO fileDTO, Agenciabancaria agenciabancaria, String contentType) {
		validateConta(agenciabancaria);
		fileDTO.setTipoDocumento(TipoDocumento.EXTRATO);
		String media = com.google.common.net.MediaType.parse(contentType).subtype();
		if (media != "pdf" && media != "octet-stream") {
			throw new MrContadorException("ofx.notextrato");
		}
		if (media == "pdf") {
			pdfParserDefault.process(fileDTO, agenciabancaria);
		} else {
			ofxParserDefault.process(fileDTO, agenciabancaria);
		}
	}

	public List<FileS3> processComprovante(FileDTO fileDTO, Agenciabancaria agenciabancaria) {
		validateConta(agenciabancaria);
		return parserComprovante.process(fileDTO, agenciabancaria);
	}
	
	private void validateConta(Agenciabancaria agencia) {
		if(!agencia.getTipoAgencia().equals(TipoAgencia.CONTA)) {
			throw new MrContadorException("agencia.not.valida");
		}
	}

}
