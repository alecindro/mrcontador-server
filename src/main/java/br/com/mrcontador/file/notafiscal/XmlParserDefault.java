package br.com.mrcontador.file.notafiscal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fincatto.documentofiscal.utils.DFPersister;

import br.com.mrcontador.config.S3Properties;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileParser;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ArquivoService;
import br.com.mrcontador.service.FunctionService;
import br.com.mrcontador.service.NotafiscalService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.service.mapper.ArquivoMapper;
import br.com.mrcontador.service.mapper.NotafiscalNfe310Mapper;
import br.com.mrcontador.service.mapper.NotafiscalNfe400Mapper;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class XmlParserDefault implements FileParser {

	@Autowired
	private NotafiscalService notafiscalService;
	@Autowired FunctionService functionService;
	@Autowired
	private S3Service s3Service;
	@Autowired
	private ParceiroService parceiroService;
	@Autowired
	private S3Properties properties;
	@Autowired
	private ArquivoService arquivoService;

	@Override
	public String process(FileDTO dto) throws Exception {
		InputStream doc = null;
		try {
			doc = new ByteArrayInputStream(dto.getOutputStream().toByteArray());
			NotaDefault notaDefault = new DFPersister().read(NotaDefault.class, doc, false);
			String periodo = "";
			switch (notaDefault.getProtocolo().getVersao()) {
			case "4.00":
				periodo = processNFE40(dto);
				break;
			case "3.10":
				periodo = processNFE301(dto);
				break;
			default:
				break;
			}
			return periodo;
		} catch (IOException e) {
			throw new MrContadorException("notafiscal.notparsed", e.getCause());
		} finally {
			try {
				if (doc != null) {
					doc.close();
				}
			} catch (IOException e) {

			}
		}

	}

	private String processNFE40(FileDTO dto) throws Exception {
		InputStream nota = new ByteArrayInputStream(dto.getOutputStream().toByteArray());
		com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfNotaProcessada = new DFPersister()
				.read(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada.class, nota, false);
		nota.close();
		Parceiro parceiro = null;
		String cnpjDestinatario = nfNotaProcessada.getNota().getInfo().getDestinatario().getCnpj() != null
				? nfNotaProcessada.getNota().getInfo().getDestinatario().getCnpj()
				: nfNotaProcessada.getNota().getInfo().getDestinatario().getCpf();
		String cnpjEmitente = nfNotaProcessada.getNota().getInfo().getEmitente().getCnpj() != null
				? nfNotaProcessada.getNota().getInfo().getEmitente().getCnpj()
				: nfNotaProcessada.getNota().getInfo().getEmitente().getCpf();
		boolean isEmitente = isEmitente(dto.getParceiro().getParCnpjcpf(), cnpjDestinatario, cnpjEmitente);
		if (isEmitente) {
			parceiro = findParceiro(cnpjEmitente);
		} else {
			parceiro = findParceiro(cnpjDestinatario);
		}
		validateParceiro(dto.getParceiro(), parceiro);
		FileS3 fileS3 = new FileS3();
		fileS3.setTipoDocumento(TipoDocumento.NOTA);
		fileS3.setFileDTO(dto);
		fileS3.setOutputStream(dto.getOutputStream());
		Arquivo xml = processXml(fileS3);
		Arquivo pdf = processPDF(fileS3);
		NotafiscalNfe400Mapper mapper = new NotafiscalNfe400Mapper();
		List<Notafiscal> list = mapper.toEntity(nfNotaProcessada, parceiro, isEmitente);
		list.forEach(_nota->{
			_nota.setArquivo(xml);
			_nota.setArquivoPDF(pdf);
    		_nota = notafiscalService.save(_nota);
    		
    	});
		functionService.callProcessaNotafiscal(list, SecurityUtils.getCurrentTenantHeader());
		s3Service.uploadNota(notafiscalService, pdf, xml, nfNotaProcessada, dto.getOutputStream(),list);
		return MrContadorUtil.periodo(list.stream().findFirst().get().getNotDatasaida());
	}

	private Arquivo processXml(FileS3 fileS3) {
		ArquivoMapper mapper = new ArquivoMapper();
		String dir = MrContadorUtil.getFolder(fileS3.getFileDTO().getContador(),
				String.valueOf(fileS3.getFileDTO().getParceiro().getId()), properties.getNotaFolder());
		String filename = MrContadorUtil.genFileNameXML(fileS3.getTipoDocumento(),
				fileS3.getFileDTO().getParceiro().getId());
		fileS3.getFileDTO().setName(filename);
		fileS3.getFileDTO().setBucket(properties.getBucketName());
		fileS3.getFileDTO().setS3Dir(dir);
		fileS3.getFileDTO().setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
		fileS3.getFileDTO().setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
		return arquivoService.save(mapper.toEntity(fileS3.getFileDTO()));
	}

	private Arquivo processPDF(FileS3 fileS3) {
		ArquivoMapper mapper = new ArquivoMapper();
		String dir = MrContadorUtil.getFolder(fileS3.getFileDTO().getContador(),
				String.valueOf(fileS3.getFileDTO().getParceiro().getId()), properties.getNotaFolder());
		String filenamePDF = MrContadorUtil.genFileNamePDF(fileS3.getTipoDocumento(),
				fileS3.getFileDTO().getParceiro().getId());
		fileS3.getFileDTO().setName(filenamePDF);
		fileS3.getFileDTO().setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filenamePDF));
		fileS3.getFileDTO().setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filenamePDF));
		return arquivoService.save(mapper.toEntity(fileS3.getFileDTO()));
	}

	private String processNFE301(FileDTO dto) throws Exception {
		InputStream nota = new ByteArrayInputStream(dto.getOutputStream().toByteArray());
		com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfNotaProcessada = new DFPersister()
				.read(com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada.class, nota, false);
		nota.close();
		Parceiro parceiro = null;
		String cnpjDestinatario = nfNotaProcessada.getNota().getInfo().getDestinatario().getCnpj() != null
				? nfNotaProcessada.getNota().getInfo().getDestinatario().getCnpj()
				: nfNotaProcessada.getNota().getInfo().getDestinatario().getCpf();
		String cnpjEmitente = nfNotaProcessada.getNota().getInfo().getEmitente().getCnpj() != null
				? nfNotaProcessada.getNota().getInfo().getEmitente().getCnpj()
				: nfNotaProcessada.getNota().getInfo().getEmitente().getCpf();
		boolean isEmitente = isEmitente(dto.getParceiro().getParCnpjcpf(), cnpjDestinatario, cnpjEmitente);
		if (isEmitente) {
			parceiro = findParceiro(cnpjEmitente);
		} else {
			parceiro = findParceiro(cnpjDestinatario);
		}
		validateParceiro(dto.getParceiro(), parceiro);
		FileS3 fileS3 = new FileS3();
		fileS3.setTipoDocumento(TipoDocumento.NOTA);
		fileS3.setFileDTO(dto);
		Arquivo xml = processXml(fileS3);
		Arquivo pdf = processPDF(fileS3);
		NotafiscalNfe310Mapper mapper = new NotafiscalNfe310Mapper();
		List<Notafiscal> list = mapper.toEntity(nfNotaProcessada, parceiro, isEmitente);
		list.forEach(_nota->{
			_nota.setArquivo(xml);
			_nota.setArquivoPDF(pdf);
    		_nota = notafiscalService.save(_nota);
    		
    	});
		functionService.callProcessaNotafiscal(list, SecurityUtils.getCurrentTenantHeader());
		s3Service.uploadNota(notafiscalService, pdf, xml, nfNotaProcessada, dto.getOutputStream(), list);
		return MrContadorUtil.periodo(list.stream().findFirst().get().getNotDatasaida());
	}

	private Parceiro findParceiro(String cnpj) {
		Optional<Parceiro> parceiro = parceiroService.findByParCnpjcpf(cnpj);
		if (parceiro.isPresent()) {
			return parceiro.get();
		}
		throw new MrContadorException("parceiro.notfound", cnpj);
	}

	private void validateParceiro(Parceiro parceiroUpload, Parceiro parceiroDoc) throws ComprovanteException {
		if (parceiroUpload == null) {
			throw new ComprovanteException("parceiro.notfound");
		}

		if (!parceiroUpload.equals(parceiroDoc)) {
			throw new ComprovanteException("nota.parceironotequal");
		}

	}

	private boolean isEmitente(String cnpj, String cnpjDestinatario, String cnpjEmitente) {
		if (cnpjDestinatario != null
				&& MrContadorUtil.onlyNumbers(cnpj).equals(MrContadorUtil.onlyNumbers(cnpjDestinatario))) {
			return false;
		}
		if (cnpjEmitente != null && MrContadorUtil.onlyNumbers(cnpj).equals(MrContadorUtil.onlyNumbers(cnpjEmitente))) {
			return true;
		}
		throw new MrContadorException("nota.parceiro.notfromnota");

	}

}
