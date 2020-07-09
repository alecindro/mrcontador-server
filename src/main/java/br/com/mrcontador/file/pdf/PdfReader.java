package br.com.mrcontador.file.pdf;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import br.com.mrcontador.service.PlanoContaService;

public interface PdfReader {

	
	public void process(List<PDDocument> pages, PlanoContaService service) throws IOException;
}
