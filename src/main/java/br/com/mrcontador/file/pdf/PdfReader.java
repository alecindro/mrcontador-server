package br.com.mrcontador.file.pdf;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import br.com.mrcontador.file.dto.PlanoConta;

public interface PdfReader {

	
	public PlanoConta process(List<PDDocument> pages) throws IOException;
}
