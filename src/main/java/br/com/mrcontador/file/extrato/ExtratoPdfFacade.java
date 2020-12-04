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
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.erros.ExtratoException;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class ExtratoPdfFacade extends ExtratoFacade{

	private static Logger log = LoggerFactory.getLogger(ExtratoPdfFacade.class);

	public String process(FileDTO fileDTO, Agenciabancaria agenciaBancaria) {
		PdfParserExtrato pdfParser = PdfParserExtratoFactory.getParser(agenciaBancaria);
		OfxDTO ofxDTO = process(pdfParser, fileDTO);
		pdfParser.validate(ofxDTO.getBanco(), ofxDTO.getAgencia(), ofxDTO.getConta(), fileDTO.getParceiro(),
				agenciaBancaria);
		List<Extrato> extratos = save(fileDTO, ofxDTO, agenciaBancaria);
		Set<String> periodos = new HashSet<>();
		extratos.forEach(extrato -> {
			periodos.add(MrContadorUtil.periodo(extrato.getExtDatalancamento()));
		});
		pdfParser.callExtrato(extratoService,extratos,periodos,fileDTO.getParceiro().getId());
		return periodos.stream().findFirst().get();
	}

	private OfxDTO process(PdfParserExtrato pdfParser, FileDTO fileDTO) {
		InputStream first = null;
		PDDocument document = null;
		List<PDDocument> pages = null;
		try {
			first = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			document = PDDocument.load(first);
			Splitter splitter = new Splitter();
			pages = splitter.split(document);
			return pdfParser.process(pages);
		} catch (IOException e) {
			throw new ExtratoException("extrato.parser.error", fileDTO.getOriginalFilename(), e);
		} finally {
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
	}
	
	


}
