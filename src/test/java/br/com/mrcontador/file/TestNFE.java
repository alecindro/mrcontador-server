package br.com.mrcontador.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestNFE {

	@Autowired
	private FileService fileService;

	@Test
	public void test() throws Exception {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/nfe2";
		File fileEntry = new File(folder);	
		
		for(File initialFile : fileEntry.listFiles()) {
		FileDTO dto = new FileDTO();
	    try {
			InputStream stream = new FileInputStream(initialFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			stream.transferTo(baos);
			stream.close();
			dto.setContentType("text/xml");
			dto.setOutputStream(baos);
			dto.setContador("ds_demo");
			Parceiro parceiro = new Parceiro();
			parceiro.setParCnpjcpf("10539433000173");
			parceiro.setId(2L);
			dto.setParceiro(parceiro);
			dto.setOriginalFilename(initialFile.getName());
			dto.setSize(initialFile.length());
			dto.setUsuario("SYSTEM");
			fileService.processNFE(dto);
	} catch(Exception e) {
		e.printStackTrace();
	}
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
