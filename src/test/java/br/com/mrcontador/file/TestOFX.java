package br.com.mrcontador.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.ofx.OfxParserDefault;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;

public abstract class TestOFX {
	
	@Autowired
	private OfxParserDefault ofxParserDefault;
	
	public void test(String file) {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/ofx/"+file;
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
