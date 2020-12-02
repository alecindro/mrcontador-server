package br.com.mrcontador.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.extrato.OfxParserfacade;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;


@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestOFXSicred {

	@Autowired
	private OfxParserfacade ofxParserDefault;

	@Test
	public void test() {
	 testSicred();
	// testCaixa();
	}
	
	public void testSicred() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/ofx/05.2020.ofx";
		File initialFile = new File(folder);	
		FileDTO dto = new FileDTO();
		Agenciabancaria bancaria = new Agenciabancaria();
		bancaria.setAgeAgencia("226000");
	    try {
			InputStream stream = new FileInputStream(initialFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			stream.transferTo(baos);
			stream.close();
			dto.setContentType("text/plain");
			dto.setOutputStream(baos);
			dto.setContador("ds_demo");
			dto.setOriginalFilename(initialFile.getName());
			dto.setSize(initialFile.length());
			dto.setUsuario("SYSTEM");
			Parceiro parceiro = new Parceiro();
			parceiro.setId(1L);
			dto.setParceiro(parceiro);
			bancaria.setParceiro(parceiro);
			ofxParserDefault.process(dto,bancaria);
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public void testCaixa() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/caixa.ofx";
		File initialFile = new File(folder);	
		FileDTO dto = new FileDTO();
		Agenciabancaria bancaria = new Agenciabancaria();
	    try {
			InputStream stream = new FileInputStream(initialFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			stream.transferTo(baos);
			stream.close();
			dto.setContentType("text/plain");
			dto.setOutputStream(baos);
			dto.setContador("ds_demo");
			dto.setOriginalFilename(initialFile.getName());
			dto.setSize(initialFile.length());
			dto.setUsuario("SYSTEM");
			Parceiro parceiro = new Parceiro();
			parceiro.setId(1L);
			dto.setParceiro(parceiro);
			ofxParserDefault.process(dto,bancaria);
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
}
