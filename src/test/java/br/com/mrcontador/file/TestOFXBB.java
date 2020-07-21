package br.com.mrcontador.file;

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
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.ofx.OfxParserDefault;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;


@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestOFXBB {

	@Autowired
	private OfxParserDefault ofxParserDefault;

	@Test
	public void test() {
	 testSicoob();
	// testCaixa();
	}
	
	public void testSicoob() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/sicoob.ofx";
		File initialFile = new File(folder);	
		FileDTO dto = new FileDTO();
	    try {
			InputStream stream = new FileInputStream(initialFile);
			dto.setContentType("text/plain");
			dto.setInputStream(stream);
			dto.setContador("ds_demo");
			dto.setOriginalFilename(initialFile.getName());
			dto.setSize(initialFile.length());
			dto.setUsuario("SYSTEM");
			Parceiro parceiro = new Parceiro();
			parceiro.setId(1L);
			dto.setParceiro(parceiro);
			ofxParserDefault.process(dto);
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public void testCaixa() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/caixa.ofx";
		File initialFile = new File(folder);	
		FileDTO dto = new FileDTO();
	    try {
			InputStream stream = new FileInputStream(initialFile);
			dto.setContentType("text/plain");
			dto.setInputStream(stream);
			dto.setContador("ds_demo");
			dto.setOriginalFilename(initialFile.getName());
			dto.setSize(initialFile.length());
			dto.setUsuario("SYSTEM");
			Parceiro parceiro = new Parceiro();
			parceiro.setId(1L);
			dto.setParceiro(parceiro);
			ofxParserDefault.process(dto);
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
}
