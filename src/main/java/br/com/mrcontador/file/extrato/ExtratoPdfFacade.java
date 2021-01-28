package br.com.mrcontador.file.extrato;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.erros.AgenciaException;
import br.com.mrcontador.erros.ExtratoException;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class ExtratoPdfFacade extends ExtratoFacade{

	@Autowired
	private S3Service s3Service;
	private static Logger log = LoggerFactory.getLogger(ExtratoPdfFacade.class);

	public String process(FileDTO fileDTO, Agenciabancaria agenciaBancaria) {
		PdfParserExtrato pdfParser = PdfParserExtratoFactory.getParser(agenciaBancaria);
		OfxDTO ofxDTO = process(pdfParser, fileDTO, agenciaBancaria);
		List<Extrato> extratos = save(fileDTO, ofxDTO, agenciaBancaria);
		Set<String> periodos = new HashSet<>();
		extratos.forEach(extrato -> {
			periodos.add(MrContadorUtil.periodo(extrato.getExtDatalancamento()));
		});
		pdfParser.callExtrato(extratoService,extratos,fileDTO.getParceiro().getId());
		pdfParser.callRegraInteligent(extratoService, fileDTO.getParceiro().getId(), periodos);		
		pdfParser.extrasFunctions(extratoService, extratos,agenciaBancaria);
		pdfParser.callProcessaNotafiscalGeral(extratoService, fileDTO.getParceiro().getId(), extratos);
		return periodos.stream().findFirst().get();
	}

	private OfxDTO process(PdfParserExtrato pdfParser, FileDTO fileDTO, Agenciabancaria agenciaBancaria) {
		InputStream first = null;
		PDDocument document = null;
		List<PDDocument> pages = null;
		OfxDTO ofxDTO = null;
		try {
			first = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			document = PDDocument.load(first);
			Splitter splitter = new Splitter();
			pages = splitter.split(document);
			ofxDTO =  pdfParser.process(pages, fileDTO.getParceiro(), agenciaBancaria);
		} catch (ExtratoException e) {
			throw e;
		} catch (AgenciaException e) {
			throw e;
		} catch(Exception e1){
			s3Service.uploadErro(fileDTO);
			throw new ExtratoException("extrato.parser.error", fileDTO.getOriginalFilename());
		}
		finally {
			try {
				if(document != null) {
					document.close();
				}
				if (first != null) {
					first.close();
				}
				

			} catch (IOException e) {
				log.error(e.getMessage());
			}
			if(pages != null) {
				pages.forEach(page -> {
					try {
						page.close();
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				});
			}
		}
		if(ofxDTO != null) {
			pdfParser.validate(ofxDTO.getBanco(), ofxDTO.getAgencia(), ofxDTO.getConta(), fileDTO.getParceiro(),
					agenciaBancaria);
		}
		return ofxDTO;
	}
}
