package br.com.mrcontador.file.comprovante;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBradesco;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;

public class TesteBBComprovante {
	
	private static final String usuario = "teste";
	
	public static void main(String[] args) throws Exception {
		TesteBBComprovante teste = new TesteBBComprovante();
		teste.caixa("/home/alecindro/Documents/drcontabil/docs/comprovantes/bb/comprovantes072020.pdf");
		//teste.teste1();
		/*try (Stream<Path> filePathStream=Files.walk(Paths.get("/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco/bradesco.pdf"))) {
		    filePathStream.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		            try {
						teste.caixa(filePath.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    });
		}*/
	}
	
	private void teste1(String file) throws Exception {
		FileInputStream inputstream = new FileInputStream(new File(file));
	    PDDocument document = PDDocument.load(inputstream);
		Splitter splitter = new Splitter();
		PDFTextStripper stripper = new PdfReaderPreserveSpace();
		List<PDDocument> pages = splitter.split(document);
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("5248");
		agencia.setAgeNumero("456550");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf(null);		
		agencia.setBanCodigobancario(BancoCodigoBancario.BB.getCodigoBancario());
		String comprovante = stripper.getText(pages.get(1));
		ComprovanteBradesco cb = new ComprovanteBradesco();
		cb.parse(comprovante, agencia, parceiro);
		
		document.close();
	}
	
	private void caixa(String file) throws Exception {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("5248");
		agencia.setAgeNumero("456550");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf(null);		
		agencia.setBanCodigobancario(BancoCodigoBancario.BB.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setInputStream(load(file));
		dto.setUsuario(usuario);
		dto.setParceiro(parceiro);
		ParserComprovanteDefault p = new ParserComprovanteDefault();
		List<FileS3> errors =  p.process(dto, agencia);
		System.out.println("========== ERROS ==================");
		System.out.println("========== "+ file + " ==================");
		for(FileS3 erro : errors) {			
			System.out.println(erro.toString());
		}
	}
	
	private InputStream load(String folder) throws FileNotFoundException {
		File initialFile = new File(folder);	
		return new FileInputStream(initialFile);
		
	}

}
