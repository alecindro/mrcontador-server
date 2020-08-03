package br.com.mrcontador.file.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fincatto.documentofiscal.utils.DFPersister;

import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileParser;
import br.com.mrcontador.file.xml.nfe.NotaDefault;
import br.com.mrcontador.service.NotafiscalService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;

@Service
public class XmlParserDefault implements FileParser {
	
	@Autowired
	private NotafiscalService notafiscalService; 
	@Autowired 
	private S3Service s3Service;
	@Autowired
    private ParceiroService parceiroService;

	@Override
	public void process(FileDTO dto) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream header = null;
		InputStream doc = null;
		InputStream nota = null;
		try {
			dto.getInputStream().transferTo(baos);
			header = new ByteArrayInputStream(baos.toByteArray());
			doc = new ByteArrayInputStream(baos.toByteArray());
			nota = new ByteArrayInputStream(baos.toByteArray());
			NotaDefault notaDefault = new DFPersister().read(NotaDefault.class, header, false);
			switch (notaDefault.getProtocolo().getVersao()) {
			case "4.00":
				processNFE40(doc,nota,dto);				
				break;
			case "3.10":
				processNFE301(doc,nota,dto);				
				break;
			default:
				break;
			}
		} catch (IOException e) {
			throw new MrContadorException("notafiscal.notparsed",e.getCause());
		} finally {
			try {
				baos.close();
				if(header != null) {
					header.close();
				}
				if(nota != null) {
					doc.close();
				}
			} catch (IOException e) {
				
			}
		}

	}

	private void processNFE40(InputStream doc,InputStream nota,FileDTO dto) throws Exception {
		com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfNotaProcessada = new DFPersister()
				.read(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada.class, nota, false);
		Parceiro parceiro = findParceiro(nfNotaProcessada.getNota().getInfo().getDestinatario().getCnpj() != null
				? nfNotaProcessada.getNota().getInfo().getDestinatario().getCnpj()
				: nfNotaProcessada.getNota().getInfo().getDestinatario().getCpf());
		validateParceiro(dto.getParceiro(), parceiro);
		dto.setInputStream(doc);
		Arquivo arquivo = s3Service.uploadNota(dto);
		notafiscalService.process(nfNotaProcessada, parceiro, arquivo);
	}

	private void processNFE301(InputStream doc,InputStream nota,FileDTO dto) throws Exception {
		com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfNotaProcessada = new DFPersister()
				.read(com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada.class, nota, false);
		Parceiro parceiro = findParceiro(nfNotaProcessada.getNota().getInfo().getDestinatario().getCnpj() != null
				? nfNotaProcessada.getNota().getInfo().getDestinatario().getCnpj()
				: nfNotaProcessada.getNota().getInfo().getDestinatario().getCpf());
		validateParceiro(dto.getParceiro(), parceiro);
		Arquivo arquivo = s3Service.uploadNota(dto);
		dto.setInputStream(doc);
		notafiscalService.process(nfNotaProcessada, parceiro, arquivo);
	}
	
	 private Parceiro findParceiro(String cnpj) {
	    	Optional<Parceiro> parceiro = parceiroService.findByParCnpjcpf(cnpj);
	    	if(parceiro.isPresent()) {
	    		return parceiro.get();
	    	}
	    	throw new MrContadorException("error.parceiro.not.found", cnpj);
  }
	 private void validateParceiro(Parceiro parameter, Parceiro nota) {
		 if(!parameter.getId().equals(nota.getId())) {
			 throw new MrContadorException("parceiro.notfromnota", nota.getParCnpjcpf());
		 }
	 }

}
