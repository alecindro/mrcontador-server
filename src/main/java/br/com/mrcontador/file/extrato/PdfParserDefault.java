package br.com.mrcontador.file.extrato;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.extrato.banco.PdfBancoDoBrasil;
import br.com.mrcontador.file.extrato.dto.ListOfxDto;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class PdfParserDefault {

	@Autowired
	private ExtratoService extratoService;

	private static Logger log = LoggerFactory.getLogger(PdfParserDefault.class);

	public void process(FileDTO fileDTO, Agenciabancaria agenciaBancaria) {
		InputStream first = null;
		try {
			first = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			PdfParser pdfParser = getParser(agenciaBancaria);
			PDDocument document = PDDocument.load(first);
			Splitter splitter = new Splitter();
			List<PDDocument> pages = splitter.split(document);
			OfxDTO dto = pdfParser.process(pages);
			pdfParser.validate(dto.getBanco(), dto.getAgencia(), dto.getConta(), fileDTO.getParceiro(), agenciaBancaria);
			ListOfxDto listOfxDto = new ListOfxDto();
			listOfxDto.setFileDTO(fileDTO);
			listOfxDto.add(dto);
			extratoService.save(listOfxDto, agenciaBancaria);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(first!= null) {
				first.close();	
				}
				
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	private PdfParser getParser(Agenciabancaria agenciaBancaria) throws IOException {
		String codigoBancario = MrContadorUtil.removeZerosFromInital(agenciaBancaria.getBanCodigobancario());
		BancoCodigoBancario bancoCodigoBancario = BancoCodigoBancario.find(codigoBancario);
		switch (bancoCodigoBancario) {
		case BB:
			return new PdfBancoDoBrasil();
		default:
			throw new MrContadorException("extrato.pdf.banknotimplemented", agenciaBancaria.getBanCodigobancario());
		}
	}
}
