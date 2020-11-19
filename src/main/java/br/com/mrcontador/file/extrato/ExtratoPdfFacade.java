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
import br.com.mrcontador.file.FileException;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class ExtratoPdfFacade {

	@Autowired
	private ExtratoService extratoService;
	@Autowired
	private ComprovanteService comprovanteService;

	private static Logger log = LoggerFactory.getLogger(ExtratoPdfFacade.class);

	public String process(FileDTO fileDTO, Agenciabancaria agenciaBancaria) {
		PdfParserExtrato pdfParser = PdfParserExtratoFactory.getParser(agenciaBancaria);
		OfxDTO ofxDTO = process(pdfParser, fileDTO);
		pdfParser.validate(ofxDTO.getBanco(), ofxDTO.getAgencia(), ofxDTO.getConta(), fileDTO.getParceiro(),
				agenciaBancaria);
		List<Extrato> extratos = extratoService.save(fileDTO, ofxDTO, agenciaBancaria);
		Set<String> periodos = new HashSet<>();
		String tenant = SecurityUtils.getCurrentTenantHeader();
		extratoService.callExtratoAplicacao(agenciaBancaria.getId(), tenant);
		extratoService.callExtrato(extratos, tenant);
		extratos.forEach(extrato -> {
			periodos.add(MrContadorUtil.periodo(extrato.getExtDatalancamento()));
		});
		extratoService.callRegraInteligent(fileDTO.getParceiro().getId(), periodos, tenant);
		comprovanteService.callComprovanteGeral(fileDTO.getParceiro().getId(), SecurityUtils.getCurrentTenantHeader());
		pdfParser.extrasFunctions(extratoService,extratos);
		return periodos.stream().findFirst().get();
	}

	private OfxDTO process(PdfParserExtrato pdfParser, FileDTO fileDTO) {
		InputStream first = null;
		PDDocument document = null;
		try {
			first = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			document = PDDocument.load(first);
			Splitter splitter = new Splitter();
			List<PDDocument> pages = splitter.split(document);
			return pdfParser.process(pages);
		} catch (IOException e) {
			throw new FileException("extrato.parser.error", fileDTO.getOriginalFilename(), e);
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
		}
	}


}
