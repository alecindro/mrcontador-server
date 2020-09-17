package br.com.mrcontador.file;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import br.com.mrcontador.file.extrato.banco.PdfBancoDoBrasil;
import br.com.mrcontador.file.extrato.dto.OfxDTO;

public class TestPDFExtrato {
	
	public static void main(String[] args) throws Exception {
		TestPDFExtrato t = new TestPDFExtrato();
		t.teste1("/home/alecindro/Documents/drcontabil/docs/nfepdf/extratobb.pdf");
	}
	
	private void teste1(String file) throws Exception {
		FileInputStream inputstream = new FileInputStream(new File(file));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PdfBancoDoBrasil bb = new PdfBancoDoBrasil();
		List<PDDocument> pages = splitter.split(document);
		OfxDTO dto = bb.process(pages);	
		System.out.println(dto.toString());
	}

}
