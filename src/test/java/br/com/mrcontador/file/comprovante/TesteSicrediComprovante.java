package br.com.mrcontador.file.comprovante;

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
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;

public class TesteSicrediComprovante {
	
	private static final String usuario = "teste";
	
	public static void main(String[] args) throws Exception {
		TesteSicrediComprovante teste = new TesteSicrediComprovante();
		teste.teste2("C:\\Users\\alecindro.castilho\\Downloads\\06.2020.pdf");
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
		agencia.setAgeAgencia("226000");
		agencia.setAgeNumero("03347-3");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("36.701.160/0001-05");		
		agencia.setBanCodigobancario(BancoCodigoBancario.SICRED.getCodigoBancario());
		String comprovante = stripper.getText(pages.get(0));
		System.out.println(comprovante);
		//ComprovanteSantander cb = new ComprovanteSantander();
		//cb.parse(comprovante, agencia, parceiro);
		
		document.close();
	}
	
	private void teste2(String file) throws Exception {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("226000");
		agencia.setAgeNumero("3347-3");
		Parceiro parceiro = new Parceiro();
		parceiro.setParCnpjcpf("36.701.160/0001-05");		
		agencia.setBanCodigobancario(BancoCodigoBancario.SICRED.getCodigoBancario());
		FileDTO dto = new FileDTO();
		dto.setOutputStream(load(file));
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
	
	private ByteArrayOutputStream load(String folder) throws IOException {
		File initialFile = new File(folder);	
		InputStream stream = new FileInputStream(initialFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stream.transferTo(baos);
		stream.close();
		return baos;
		
	}

}
