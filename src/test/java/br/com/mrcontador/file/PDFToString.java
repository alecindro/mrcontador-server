package br.com.mrcontador.file;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteErro;
import br.com.mrcontador.file.comprovante.ParserComprovanteDefault;
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
		
		document.close();*/
		PDFToString PDFToString = new PDFToString();
		PDFToString.testSantander2();
   
    
 }
	
	private static final String usuario = "teste";
	
	public void testSantander2() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("1651");
		agencia.setAgeNumero("13-000889");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("24.408.746/0001-05");		
		agencia.setBanCodigobancario(BancoCodigoBancario.SANTANDER.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/santander/GerarPDF_04082020105944.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		List<ComprovanteErro> errors =  p.process(dto, agencia);
		for(ComprovanteErro erro : errors) {
			System.out.println(erro.toString());
		}
		
	}
	
	private InputStream load(String folder) throws FileNotFoundException {
		File initialFile = new File(folder);	
		return new FileInputStream(initialFile);
		
	}

}
