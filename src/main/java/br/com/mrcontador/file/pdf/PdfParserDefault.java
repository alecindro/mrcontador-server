package br.com.mrcontador.file.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.file.FileException;
import br.com.mrcontador.service.PlanoContaService;
import br.com.mrcontador.util.SpringUtil;

@Service
public class PdfParserDefault {

	@Autowired
	private PlanoContaService service;
	@Autowired
	BeanFactory beanFactory;
	

	public void process(InputStream stream) {
		PDDocument document;
		try {
			document = PDDocument.load(stream);
			PdfReader reader = getIdentifier(document);
			Splitter splitter = new Splitter();
	    	List<PDDocument> pages = splitter.split(document);
	    	reader.process(pages,service);
		} catch (IOException e) {
			throw new FileException("pdf.parse.error", e.getMessage(), e);
		}
    	
	}
	
private PdfReader getIdentifier(PDDocument document){
	
	PDDocumentInformation info = document.getDocumentInformation();
	return SpringUtil.instantiate(PdfReaderEnum.getClass(info.getTitle()), PdfReader.class);
		
		/*PDDocumentCatalog catalog = document.getDocumentCatalog();
		PDMetadata metadata = catalog.getMetadata();
		// To read the XML metadata
		if(metadata != null) {
		InputStream xmlInputStream = metadata.createInputStream();
		PDDocument metaDocument = PDDocument.load(xmlInputStream);
		PDFTextStripper stripper = new PDFTextStripper();
		System.out.println(stripper.getText(metaDocument));
		return "";
		}else {
			PDDocumentInformation info = document.getDocumentInformation();
			/*System.out.println( "Page Count=" + document.getNumberOfPages() );
			System.out.println( "Title=" + info.getTitle() );
			System.out.println( "Author=" + info.getAuthor() );
			System.out.println( "Subject=" + info.getSubject() );
			System.out.println( "Keywords=" + info.getKeywords() );
			System.out.println( "Creator=" + info.getCreator() );
			System.out.println( "Producer=" + info.getProducer() );
			System.out.println( "Creation Date=" + info.getCreationDate() );
			System.out.println( "Modification Date=" + info.getModificationDate());
			System.out.println( "Trapped=" + info.getTrapped() );     
			return info.getTitle();
		}*/
	}

}
