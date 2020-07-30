package br.com.mrcontador.file.comprovante;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBanco;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBradesco;
import br.com.mrcontador.file.comprovante.banco.ComprovanteCredCrea;
import br.com.mrcontador.file.comprovante.banco.ComprovanteItau;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSantander;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSicoob;
import br.com.mrcontador.file.comprovante.banco.ComprovanteUnicred;
import br.com.mrcontador.file.pdf.PdfReaderPreserveSpace;
import br.com.mrcontador.service.ComprovanteService;

@Service
public class ParserComprovanteDefault {

	@Autowired
	private ComprovanteService service;

	public void process(InputStream stream, Banco banco, Parceiro parceiro) {
		try {
			List<String> comprovantes = parseComprovante(stream);
			ComprovanteBanco parser = getParser(banco.getBanCodigobancario());
			List<DiffPage> diffPages = new ArrayList<>();
			int page = 1;
			for (String comprovante : comprovantes) {
				System.out.println("Pagina:" +page);
				List<DiffValue> diffvalues = parser.parse(comprovante);
				if(diffvalues!= null) {
				DiffPage diffPage = new DiffPage();
				diffPage.setPage(page);
				diffPage.setDiffValues(diffvalues);
				diffPages.add(diffPage);
				System.out.println(diffPage.toString());
				}
				++page;
			}
			//service.save(diffPages, banco, parceiro);
		} catch (DiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ComprovanteBanco getParser(String codigoBancario) {
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
		return comprovantes;
	}

}
