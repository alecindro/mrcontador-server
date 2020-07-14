package br.com.mrcontador.file;

import java.time.LocalDateTime;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;

public class TestTika {
	//planoconta_2020-07-11T19%3A01%3A42.709967.pdf
	
	public static void main(final String[] args) throws ParseException {
		LocalDateTime z = LocalDateTime.now();
		ContentType tupe = new ContentType("application/pdf");
		tupe.getBaseType();
		tupe.getPrimaryType();
		tupe.getSubType();
		com.google.common.net.MediaType media = com.google.common.net.MediaType.parse("application/pdf");
		String value = z.getYear()+"_"+z.getMonthValue()+"_"+z.getDayOfMonth()+"_"+z.getHour()+"_"+z.getMinute()+"_"+z.getSecond();
		System.out.println(value);
	}
/*	
	private void test() throws IOException, SAXException, TikaException {
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
	}*/
}
