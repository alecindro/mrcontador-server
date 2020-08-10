package br.com.mrcontador.file.comprovante;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBradesco;
import br.com.mrcontador.file.comprovante.banco.ComprovanteCredCrea;
import br.com.mrcontador.file.comprovante.banco.ComprovanteItau;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSantander;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSicoob;
import br.com.mrcontador.file.comprovante.banco.ComprovanteUnicred;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;

@Service
public class ParserComprovanteDefault {

	@Autowired
	private ComprovanteService service;
	@Autowired
	S3Service s3Service;

	private static Logger log = LoggerFactory.getLogger(ParserComprovanteDefault.class);

	public void process(FileDTO fileDTO, Agenciabancaria agencia) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream first = null;
		InputStream second = null;
		InputStream third = null;
		try {
			fileDTO.getInputStream().transferTo(baos);
			first = new ByteArrayInputStream(baos.toByteArray());
			second = new ByteArrayInputStream(baos.toByteArray());
			third = new ByteArrayInputStream(baos.toByteArray());
			List<String> textComprovantes = parseComprovante(third);
			ParserComprovante parser = getParser(agencia.getBanCodigobancario());
			List<Comprovante> comprovantes = new ArrayList<>();
			for (String comprovante : textComprovantes) {
					List<Comprovante> _comprovantes = parser.parse(comprovante, agencia, fileDTO.getParceiro());
					if (!comprovantes.isEmpty()) {
						comprovantes.addAll(_comprovantes);
					}
			}
			 fileDTO.setInputStream(second);
			 Arquivo arquivo = s3Service.uploadComprovante(fileDTO);
			 comprovantes.forEach(comprovante -> comprovante.setArquivo(arquivo));
			 service.saveAll(comprovantes);
		} catch (Exception e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			log.error(e.getMessage(), e);
			fileDTO.setInputStream(first);
			s3Service.uploadErro(fileDTO);
			throw new MrContadorException("comprovante.process", e.getMessage());
		} finally {
			try {
				third.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

	}

	private ParserComprovante getParser(String codigoBancario) {
		BancoCodigoBancario bcb = BancoCodigoBancario.find(codigoBancario);
		switch (bcb) {
		case BB:
			return null;
		case BRADESCO:
			return new ComprovanteBradesco();
		case CAIXA:
			return null;
		case CREDCREA:
			return new ComprovanteCredCrea();
		case ITAU:
			return new ComprovanteItau();
		case SANTANDER:
			return new ComprovanteSantander();
		case SICOOB:
			return new ComprovanteSicoob();
		case UNICRED:
			return new ComprovanteUnicred();
		default:
			throw new MrContadorException("parsercomprovante.notfound", codigoBancario);

		}
	}

	private List<String> parseComprovante(InputStream stream) throws IOException {
		PDDocument document = PDDocument.load(stream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		List<String> comprovantes = new ArrayList<String>();
		for (PDDocument page : pages) {
			comprovantes.add(stripper.getText(page));
		}
		document.close();
		return comprovantes;
	}

}
