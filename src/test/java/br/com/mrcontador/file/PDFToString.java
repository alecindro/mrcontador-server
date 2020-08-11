package br.com.mrcontador.file;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;

public class PDFToString {
	
	public static void main(String[] args) throws Exception 
	{
		
		FileInputStream inputstream = new FileInputStream(new File("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/comprovantes.pdf"));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		System.out.println(pages.size());
		System.out.println(stripper.getText(pages.get(3)));
		document.close();
   
    
 }

}
