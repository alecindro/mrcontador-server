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
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class ParserComprovanteDefault {

	@Autowired
	private ComprovanteService service;
	@Autowired
	S3Service s3Service;

	private static Logger log = LoggerFactory.getLogger(ParserComprovanteDefault.class);

	public List<FileS3> process(FileDTO fileDTO, Agenciabancaria agencia) {

		try {
			List<PPDocumentDTO> textComprovantes = parseComprovante(fileDTO.getInputStream());
			ParserComprovante parser = getParser(agencia.getBanCodigobancario());
			List<FileS3> files = new ArrayList<>();
			List<FileS3> erros = new ArrayList<FileS3>();
			List<Comprovante> salvar = new ArrayList<Comprovante>();
			List<Comprovante> salvos = new ArrayList<Comprovante>();
			int page = 0;
			for (PPDocumentDTO pddComprovante : textComprovantes) {
				page = page + 1;
				try {
					List<Comprovante> _comprovantes = parser.parse(pddComprovante.getComprovante(), agencia, fileDTO.getParceiro());
					if (_comprovantes != null && !_comprovantes.isEmpty()) {
						salvar.addAll(_comprovantes);
						FileS3 fileS3 = new FileS3();
						fileS3.setComprovantes(_comprovantes);
						fileS3.setFileDTO(fileDTO);
						fileS3.setOutputStream(pddComprovante.getOutstream());
						fileS3.getFileDTO().setTipoDocumento(TipoDocumento.COMPROVANTE);
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
					fileS3.setOutputStream(pddComprovante.getOutstream());
					fileS3.setTipoDocumento(TipoDocumento.COMPROVANTE);
					fileS3.setPage(page);
					erros.add(fileS3);
				}
			}
			for(Comprovante c : salvar) {
				try {
					salvos.add(service.save(c));
				}catch(Exception e ) {
					log.error(e.getMessage());
				}
			}
			if(salvos.isEmpty()) {
				throw new org.springframework.dao.DataIntegrityViolationException("comprovante já importado");
			}
			s3Service.uploadComprovante(files, SecurityUtils.getCurrentTenantHeader());
			log.info("Comprovantes salvos");
			return erros;
		} catch (IOException e1) {
			throw new MrContadorException("parsecomprovante.error", e1);
		}

	}

	private ParserComprovante getParser(String codigoBancario) {
		codigoBancario = MrContadorUtil.removeZerosFromInital(codigoBancario);
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

	private List<PPDocumentDTO> parseComprovante(InputStream stream) throws IOException {
		PDDocument document = PDDocument.load(stream);
		List<PPDocumentDTO> list = new ArrayList<>();
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		for(PDDocument pdDocument : splitter.split(document)) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			String comprovante = stripper.getText(pdDocument);
			pdDocument.save(output);
			PPDocumentDTO pddocumentDTO = new PPDocumentDTO(comprovante,output);
			list.add(pddocumentDTO);
			pdDocument.close();
		}
		document.close();
		return list;

	}


}
