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
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestPlanoDeContas {

	
	@Autowired
	private FileService fileService;

	
	@Test
	public void teste() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/Plano de Contas - mercado dassoler.pdf";
		File initialFile = new File(folder);
		FileDTO dto = new FileDTO();
	    try {
			InputStream stream = new FileInputStream(initialFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			stream.transferTo(baos);
			stream.close();
			dto.setContentType("application/pdf");
			dto.setOutputStream(baos);
			dto.setContador("ds_demo");
			dto.setOriginalFilename(initialFile.getName());
			dto.setSize(initialFile.length());
			dto.setUsuario("SYSTEM");
     		fileService.processPlanoConta(dto,1L,SistemaPlanoConta.DOMINIO_SISTEMAS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
