package br.com.mrcontador.file;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

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
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;


@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestPdfBB {

	@Autowired
	private FileService fileService;
	@Autowired
	private ParceiroService parceiroService;
	@Autowired
	private AgenciabancariaService agenciaService;

	@Test
	public void test() {
	 testBB();
	
	}
	
	public void testBB() {
		  try {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/teste/01-2020/01-2020.pdf";
		File initialFile = new File(folder);
		FileInputStream stream = new FileInputStream(initialFile);
		Optional<Parceiro> parceiro = parceiroService.findOne(2L);
		Optional<Agenciabancaria> agencia = agenciaService.findOne(1L);
		FileDTO fileDTO = fileService.getFileDTO("application/pdf", initialFile.getName(), initialFile.length(), stream, Optional.of("demo@localhost.com"),
				"ds_demo", parceiro.get());
		fileService.processExtrato(fileDTO, agencia.get(), "application/pdf");
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	

}
