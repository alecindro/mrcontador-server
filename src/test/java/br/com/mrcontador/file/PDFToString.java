package br.com.mrcontador.file;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.comprovante.ParserComprovanteDefault;
import br.com.mrcontador.file.comprovante.banco.ComprovanteItau;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.service.dto.FileDTO;

public class PDFToString {
	
	public static void main(String[] args) throws Exception 
	{
		
		/*FileInputStream inputstream = new FileInputStream(new File("/home/alecindro/Documents/drcontabil/docs/comprovantes/santander/GerarPDF_04082020105944.pdf"));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		System.out.println(pages.size());
		String comprovante = stripper.getText(pages.get(16));
		String[] _lines = comprovante.split("\\r?\\n");
		System.out.println(comprovante);
		System.out.println("===== "+_lines.length+" =======");
		//64
		 //65
		  //66
		  // 83
		document.close();*/
		PDFToString PDFToString = new PDFToString();
	//	
  //PDFToString.printDoc();
  PDFToString.caixa();
  
 }
	
	private void caixa() throws Exception {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1011");
		agencia.setAgeNumero("842-4");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("09.165.847/0001-09");		
		agencia.setBanCodigobancario(BancoCodigoBancario.CAIXA.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/caixa/Aciplas - ND-33470-1 - 06.05.2020.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		String periodo =  p.process(dto, agencia);
			
			System.out.println(periodo);
	}
	
	public void printDoc() throws Exception {
		FileInputStream inputstream = new FileInputStream(new File("/home/alecindro/Documents/drcontabil/docs/comprovantes/caixa/Aciplas - ND-33470-1 - 06.05.2020.pdf"));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		System.out.println(pages.size());
		String comprovante = stripper.getText(pages.get(0));
		String[] _lines = comprovante.split("\\r?\\n");
		System.out.println(comprovante);
		System.out.println("===== "+_lines.length+" =======");
		
		document.close();
	}
	
	private void itau() throws Exception {
		FileInputStream inputstream = new FileInputStream(new File("/home/alecindro/Documents/drcontabil/docs/comprovantes/itau/Comprovantes_Itaú_Moto_Bombas_04.2020_.pdf"));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		System.out.println(pages.size());
		String comprovante = stripper.getText(pages.get(85));
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1575");
		agencia.setAgeNumero("12906");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("08.892.611/0001-01");		
		ComprovanteItau comprovanteItau = new ComprovanteItau();
		comprovanteItau.parse(comprovante, agencia, parceiro);
	}
	
	public void itau2() throws IOException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1575");
		agencia.setAgeNumero("12906");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("08.892.611/0001-01");		
		agencia.setBanCodigobancario(BancoCodigoBancario.ITAU.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/itau/Comprovantes_Itaú_Moto_Bombas_04.2020_.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		String periodo =  p.process(dto, agencia);
		
		System.out.println(periodo);
		
	}
	
	private void bradesco() throws Exception {
	 	Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("348");
		agencia.setAgeNumero("128527");
		agencia.setBanCodigobancario(BancoCodigoBancario.BRADESCO.getCodigoBancario());
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("003.551.834/0001-83");
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/comprovantes.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		String periodo =  p.process(dto, agencia);
		
		System.out.println(periodo);
	}
	
	private void sicoob() throws Exception {
	 	Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("3069-4");
		agencia.setAgeNumero("97.731-4");
		agencia.setBanCodigobancario(BancoCodigoBancario.SICOOB.getCodigoBancario());
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("18.976.447/0001-66");
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/sicoob.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		String periodo =  p.process(dto, agencia);
		
		System.out.println(periodo);
	}
	private void unicred() throws Exception {
		Agenciabancaria agencia = new Agenciabancaria();
		//agencia.setAgeAgencia("3069-4");
		agencia.setAgeNumero("1022695");
		agencia.setBanCodigobancario(BancoCodigoBancario.UNICRED.getCodigoBancario());
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("15.584.243/0001-91");
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/unicred.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		String periodo =  p.process(dto, agencia);
		
		System.out.println(periodo);
	}
	
	private void credcrea() throws Exception {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("106");
		agencia.setAgeNumero("66540");
		agencia.setBanCodigobancario(BancoCodigoBancario.CREDCREA.getCodigoBancario());
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("004.516.599-88");
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/credcrea.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		String periodo =  p.process(dto, agencia);
		
		System.out.println(periodo);
	}
	
	
	private static final String usuario = "teste";
	
	public void testSantander2() throws IOException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("348");
		agencia.setAgeNumero("128527");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("003.551.834/0001-83");		
		agencia.setBanCodigobancario(BancoCodigoBancario.BRADESCO.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/bradesco.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		String periodo =  p.process(dto, agencia);
		
		System.out.println(periodo);
		
	}
	
	public void testBB() throws IOException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("3174");
		agencia.setAgeNumero("40106");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("10.539.433/0001-73");		
		agencia.setBanCodigobancario(BancoCodigoBancario.BB.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/bb/06.2020.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		String periodo =  p.process(dto, agencia);
		
		System.out.println(periodo);
		
	}
	
	private ByteArrayOutputStream load(String folder) throws IOException {
		File initialFile = new File(folder);	
		InputStream stream = new FileInputStream(initialFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stream.transferTo(baos);
		stream.close();
		return baos;
		
	}

}
