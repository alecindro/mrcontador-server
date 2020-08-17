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

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.erros.ComprovanteErro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBB;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBradesco;
import br.com.mrcontador.file.comprovante.banco.ComprovanteCredCrea;
import br.com.mrcontador.file.comprovante.banco.ComprovanteItau;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSantander;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSicoob;
import br.com.mrcontador.file.comprovante.banco.ComprovanteUnicred;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
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

	public List<ComprovanteErro> process(FileDTO fileDTO, Agenciabancaria agencia) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream first = null;
		InputStream second = null;
		try {
			fileDTO.getInputStream().transferTo(baos);

			first = new ByteArrayInputStream(baos.toByteArray());
			second = new ByteArrayInputStream(baos.toByteArray());
			fileDTO.setInputStream(second);
			List<String> textComprovantes = parseComprovante(first);
			ParserComprovante parser = getParser(agencia.getBanCodigobancario());
			List<Comprovante> comprovantes = new ArrayList<>();
			List<ComprovanteErro> erros = new ArrayList<ComprovanteErro>();
			int page = 0;
			for (String comprovante : textComprovantes) {
				try {
					page = page + 1;
					List<Comprovante> _comprovantes = parser.parse(comprovante, agencia, fileDTO.getParceiro());
					if (_comprovantes != null && !_comprovantes.isEmpty()) {
						comprovantes.addAll(_comprovantes);
					}
				} catch (Exception e) {
					ComprovanteErro comprovanteErro = new ComprovanteErro();
					comprovanteErro.setAgencia(agencia);
					comprovanteErro.setComprovante(comprovante);
					comprovanteErro.setFileDTO(fileDTO);
					comprovanteErro.setPage(page);
					comprovanteErro.setErro(e.getMessage());
					erros.add(comprovanteErro);
				}
			}
			service.saveAll(comprovantes);
			log.info("Comprovantes salvos");
			return erros;
		} catch (IOException e1) {
			throw new MrContadorException("error.parsecomprovante", e1);
		} finally {
			if (first != null) {
				try {
					first.close();
				} catch (IOException e) {
					log.info("Erro ao fechar inputstream", e);
				}
			}
		}

	}

	private ParserComprovante getParser(String codigoBancario) {
		BancoCodigoBancario bcb = BancoCodigoBancario.find(codigoBancario);
		switch (bcb) {
		case BB:
			return new ComprovanteBB();
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
