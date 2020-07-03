package br.com.mrcontador.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import br.com.mrcontador.service.dto.ParserDTO;

@Service
public class FileService {
	
	private final Logger log = LoggerFactory.getLogger(FileService.class);

	public void save(InputStream stream) throws IOException, SAXException, TikaException {
		ParserDTO parserDTO = this.process(stream);
		log.info(parserDTO.getContent());
	}
	
	private ParserDTO process(InputStream stream) throws IOException, SAXException, TikaException {
		Metadata metadata = new Metadata();
		ParseContext context = new ParseContext();
		
		Parser parser = new AutoDetectParser();
		ContentHandler contentHandler = new BodyContentHandler(-1);
		parser.parse(stream, contentHandler, metadata, context);
		return new ParserDTO(contentHandler.toString(), metadata);
	}
}
