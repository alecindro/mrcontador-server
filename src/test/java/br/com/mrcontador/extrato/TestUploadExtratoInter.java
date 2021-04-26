package br.com.mrcontador.extrato;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.AgenciaException;
import br.com.mrcontador.erros.ExtratoException;
import br.com.mrcontador.file.FileService;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.file.extrato.PdfParserExtrato;
import br.com.mrcontador.file.extrato.banco.PdfInter;
import br.com.mrcontador.file.extrato.dto.OfxDTO;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestUploadExtratoInter {
	
	public static void main(String[] args) throws FileNotFoundException {
		TestUploadExtratoInter t = new TestUploadExtratoInter();
		String folder1 = "/home/alecindro/Documents/drcontabil/docs/ESTRATEGIA_SAUDE/EXTRATOS/01-2021.pdf";
		File _file = new File(folder1);
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("0001");
		agencia.setAgeNumero("85676578");
		Parceiro parceiro = new Parceiro();
		parceiro.setId(1L);
		parceiro.setParCnpjcpf("10539433000173");
		agencia.setBanCodigobancario(BancoCodigoBancario.INTER.getCodigoBancario());
		t.process(parceiro, agencia, new FileInputStream(_file));
		try {
			t.create();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void create() throws Exception{
		  
		String folder1 = "/home/alecindro/Documents/drcontabil/docs/PLAYBPO/EXTRATOS";
		String folder2 = "/home/alecindro/Documents/drcontabil/docs/ESTRATEGIA_SAUDE/EXTRATOS/02-2021.pdf";
		String folder3 = "/home/alecindro/Documents/drcontabil/docs/ESTRATEGIA_SAUDE/EXTRATOS/03-2021.pdf";
		
		
		File _file = new File(folder1);
		for (File f : _file.listFiles()) {
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("0001");
		agencia.setAgeNumero("85676578");
		Parceiro parceiro = new Parceiro();
		parceiro.setId(1L);
		parceiro.setParCnpjcpf("10539433000173");
		agencia.setBanCodigobancario(BancoCodigoBancario.INTER.getCodigoBancario());
		process(parceiro, agencia, new FileInputStream(f));
		}
		//process(folder2, agencia, parceiro);
		//process(folder3, agencia, parceiro);
		//process(folder4, agencia, parceiro);
		//process(folder5, agencia, parceiro);
		//process(folder6, agencia, parceiro);
	}
	
	private OfxDTO process(Parceiro parceiro, Agenciabancaria agenciaBancaria, FileInputStream file) {
		PDDocument document = null;
		List<PDDocument> pages = null;
		OfxDTO ofxDTO = null;
		try {
			PdfParserExtrato pdfParser = new PdfInter();
			document = PDDocument.load(file);
			Splitter splitter = new Splitter();
			pages = splitter.split(document);
			ofxDTO =  pdfParser.process(pages, parceiro, agenciaBancaria);
		} catch (ExtratoException e) {
			throw e;
		} catch (AgenciaException e) {
			throw e;
		} catch(Exception e1){
			e1.printStackTrace();
		}
		finally {
			if(pages != null) {
				pages.forEach(page -> {
					try {
						page.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		}
		if(ofxDTO != null) {
		
		}
		return ofxDTO;
	}

}