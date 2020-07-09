package br.com.mrcontador.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TestTika {
	
	
	public static void main(final String[] args) throws IOException, TikaException, SAXException {
	      
	      //detecting the file type
	      DefaultHandler handler = new BodyContentHandler(-1);
	      Metadata metadata = new Metadata();
	      FileInputStream inputstream = new FileInputStream(new File("/home/alecindro/Documents/drcontabil/docs/Plano de Contas - Mercado dassoler.xls"));
	      ParseContext pcontext = new ParseContext();
	      
	      //OOXml parser
	      Parser parser = new OfficeParser();
	      parser.parse(inputstream, handler, metadata,pcontext);
	      System.out.println("Contents of the document:" + handler.toString());
	      System.out.println("Metadata of the document:");
	      String[] metadataNames = metadata.names();
	      
	      for(String name : metadataNames) {
	         System.out.println(name + ": " + metadata.get(name));
	      }
	   }
}
