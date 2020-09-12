package br.com.mrcontador.file.notafiscal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fincatto.documentofiscal.utils.DFPersister;

import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileParser;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.NotafiscalService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.MrContadorUtil;

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
		InputStream doc = null;
		try {
			dto.getInputStream().transferTo(baos);
			doc = new ByteArrayInputStream(baos.toByteArray());
			NotaDefault notaDefault = new DFPersister().read(NotaDefault.class, doc, false);
			switch (notaDefault.getProtocolo().getVersao()) {
			case "4.00":
				processNFE40(baos, dto);
				break;
			case "3.10":
				processNFE301(baos, dto);
				break;
			default:
				break;
			}
		} catch (IOException e) {
			throw new MrContadorException("notafiscal.notparsed", e.getCause());
		} finally {
			try {
				baos.close();
			} catch (IOException e) {

			}
		}

	}

	private void processNFE40(ByteArrayOutputStream baos, FileDTO dto) throws Exception {
		InputStream nota = new ByteArrayInputStream(baos.toByteArray());
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
		List<Notafiscal> notas = notafiscalService.process(nfNotaProcessada, parceiro, isEmitente);
		FileS3 fileS3 = new FileS3();
		fileS3.setTipoDocumento(TipoDocumento.NOTA);
		fileS3.setNotas(notas);
		fileS3.setFileDTO(dto);
		fileS3.setOutputStream(baos);
		s3Service.uploadNota(fileS3, nfNotaProcessada, SecurityUtils.getCurrentTenantHeader());
	}

	private void processNFE301(ByteArrayOutputStream baos, FileDTO dto) throws Exception {
		InputStream nota = new ByteArrayInputStream(baos.toByteArray());
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
		List<Notafiscal> notas = notafiscalService.process(nfNotaProcessada, parceiro, isEmitente);
		FileS3 fileS3 = new FileS3();
		fileS3.setTipoDocumento(TipoDocumento.NOTA);
		fileS3.setNotas(notas);
		fileS3.setFileDTO(dto);
		fileS3.setOutputStream(baos);
		s3Service.uploadNota(fileS3, nfNotaProcessada, SecurityUtils.getCurrentTenantHeader());
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
