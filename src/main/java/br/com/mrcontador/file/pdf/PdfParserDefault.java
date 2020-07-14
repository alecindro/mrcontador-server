package br.com.mrcontador.file.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import br.com.mrcontador.file.FileParser;
import br.com.mrcontador.file.dto.PlanoConta;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.PlanoContaService;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.SpringUtil;

@Service
public class PdfParserDefault implements FileParser{

	@Autowired
	private PlanoContaService service;
	@Autowired
	private S3Service s3Service;
	@Autowired
	BeanFactory beanFactory;
	

	public void process(FileDTO dto) {
		PDDocument document;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();	
		InputStream first = null;
		InputStream second = null;
		try {
			dto.getInputStream().transferTo(baos);
			second = new ByteArrayInputStream(baos.toByteArray());
			first = new ByteArrayInputStream(baos.toByteArray());
			document = PDDocument.load(first);
			PdfReader reader = getIdentifier(document);
			Splitter splitter = new Splitter();
	    	List<PDDocument> pages = splitter.split(document);
	    	PlanoConta planoConta = reader.process(pages);
	    	service.save(planoConta, dto);
	    	dto.setInputStream(second);
	    	s3Service.uploadPlanoConta(dto);
		} catch (IOException e) {
			dto.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
			throw new FileException("pdf.parse.error", e.getMessage(), e);
		}finally {
			try {
				baos.close();
				if(first != null) {
					first.close();
				}
				if(second != null) {
					second.close();
				}
			} catch (IOException e) {
				
			}
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
