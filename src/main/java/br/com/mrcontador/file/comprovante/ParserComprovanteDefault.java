package br.com.mrcontador.file.comprovante;

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
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBB;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBradesco;
import br.com.mrcontador.file.comprovante.banco.ComprovanteCaixa;
import br.com.mrcontador.file.comprovante.banco.ComprovanteCredCrea;
import br.com.mrcontador.file.comprovante.banco.ComprovanteItau;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSantander;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSicoob;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSicredi;
import br.com.mrcontador.file.comprovante.banco.ComprovanteUnicred;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
import br.com.mrcontador.service.file.S3Service;

@Service
public class ParserComprovanteDefault {

	@Autowired
	private ComprovanteService service;
	@Autowired
	S3Service s3Service;

	private static Logger log = LoggerFactory.getLogger(ParserComprovanteDefault.class);

	public List<FileS3> process(FileDTO fileDTO, Agenciabancaria agencia) {
		
		try {
			PDFTextStripper stripper = new PdfReaderPreserveSpace();
			List<PDDocument> textComprovantes = parseComprovante(fileDTO.getInputStream(),stripper);
			ParserComprovante parser = getParser(agencia.getBanCodigobancario());
			List<FileS3> files = new ArrayList<>();
			List<FileS3> erros = new ArrayList<FileS3>();
			int page = 0;
			for (PDDocument pddComprovante : textComprovantes) {
				page = page +1;
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				pddComprovante.save(output);
				String comprovante = stripper.getText(pddComprovante);
				try {
					List<Comprovante> _comprovantes = parser.parse(comprovante, agencia, fileDTO.getParceiro());
					if (_comprovantes != null && !_comprovantes.isEmpty()) {
						service.saveAll(_comprovantes);
						FileS3 fileS3 = new FileS3();
						fileS3.setComprovantes(_comprovantes);
						fileS3.setFileDTO(fileDTO);
						fileS3.setOutputStream(output);
						fileS3.setTipoDocumento(TipoDocumento.COMPROVANTE);
						fileS3.setPage(page);
						files.add(fileS3);
					}
				} catch (Exception e) {
					Comprovante comprovanteErro = new Comprovante();
					comprovanteErro.setAgenciabancaria(agencia);
					FileS3 fileS3 = new FileS3();
					fileS3.getComprovantes().add(comprovanteErro);
					fileS3.setFileDTO(fileDTO);
					fileS3.setOutputStream(output);
					fileS3.setTipoDocumento(TipoDocumento.COMPROVANTE);
					fileS3.setPage(page);
					erros.add(fileS3);
				}
			}
			s3Service.uploadComporvante(files);
			log.info("Comprovantes salvos");
			return erros;
		} catch (IOException e1) {
			throw new MrContadorException("parsecomprovante.error", e1);
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
			return new ComprovanteCaixa();
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
		case SICRED:
			return new ComprovanteSicredi();	
		default:
			throw new MrContadorException("parsercomprovante.notfound", codigoBancario);

		}
	}

	private List<PDDocument> parseComprovante(InputStream stream, PDFTextStripper stripper) throws IOException {
		PDDocument document = PDDocument.load(stream);
		Splitter splitter = new Splitter();
		return splitter.split(document);
		
	}

}
