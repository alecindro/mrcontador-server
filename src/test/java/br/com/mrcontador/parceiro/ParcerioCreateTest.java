package br.com.mrcontador.parceiro;

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
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.FileService;
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.FileDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class ParcerioCreateTest {

	@Autowired
	private FileService fileService;
	
	@Test
	public void create() {
		  try {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "C:\\Users\\alecindro.castilho\\Documents\\study\\mrcontador\\docs dassoler\\planoconta_2020108_41411.pdf";
		File file = new File(folder);
		FileInputStream stream = new FileInputStream(file);
		FileDTO dto = fileService.getFileDTO("application/pdf", file.getName(), file.length(), stream, Optional.of("demo@localhost.com"),
				SecurityUtils.DEMO_TENANT, null);
		Parceiro parceiro = fileService.processPlanoConta(dto, null, SistemaPlanoConta.DOMINIO_SISTEMAS);
		System.out.println(parceiro.toString());
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}

}
