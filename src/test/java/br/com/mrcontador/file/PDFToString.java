package br.com.mrcontador.file;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteErro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.file.comprovante.ParserComprovanteDefault;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBradesco;
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
		PDFToString.bradesco();
  // PDFToString.printDoc();
    
 }
	
	public void printDoc() throws Exception {
		FileInputStream inputstream = new FileInputStream(new File("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/bradesco.pdf"));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		System.out.println(pages.size());
		String comprovante = stripper.getText(pages.get(4));
		String[] _lines = comprovante.split("\\r?\\n");
		System.out.println(comprovante);
		System.out.println("===== "+_lines.length+" =======");
		
		document.close();
	}
	
	private void bradesco() throws Exception {
		FileInputStream inputstream = new FileInputStream(new File("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/bradesco.pdf"));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		System.out.println(pages.size());
		String comprovante = stripper.getText(pages.get(83));
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("348");
		agencia.setAgeNumero("128527");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("003.551.834/0001-83");		
		ComprovanteBradesco comprovanteBradesco = new ComprovanteBradesco();
		comprovanteBradesco.parse(comprovante, agencia, parceiro);
	}
	
	private static final String usuario = "teste";
	
	public void testSantander2() throws FileNotFoundException {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("348");
		agencia.setAgeNumero("128527");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("003.551.834/0001-83");		
		agencia.setBanCodigobancario(BancoCodigoBancario.BRADESCO.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setInputStream(load("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/bradesco.pdf"));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		List<ComprovanteErro> errors =  p.process(dto, agencia);
		System.out.println("========== ERROS ==================");
		for(ComprovanteErro erro : errors) {
			
			System.out.println(erro.toString());
		}
		
	}
	
	private InputStream load(String folder) throws FileNotFoundException {
		File initialFile = new File(folder);	
		return new FileInputStream(initialFile);
		
	}

}