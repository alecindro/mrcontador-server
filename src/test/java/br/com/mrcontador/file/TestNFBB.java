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
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestNFBB {
	
	@Autowired
	private FileService fileService;
	@Autowired
	private ParceiroService parceiroService;


	@Test
	public void test() {
		testBB();

	}

	public void testBB() {
		try {
			TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);			
			String folder = "C:\\Users\\alecindro.castilho\\Documents\\study\\mrcontador\\docs dassoler\\nfe2\\012020";
			Optional<Parceiro> parceiro = parceiroService.findOne(1L);
			File fileEntry = new File(folder);	
			for(File file : fileEntry.listFiles()) {
			FileInputStream stream = new FileInputStream(file);
			FileDTO dto = fileService.getFileDTO("application/pdf", file.getName(), file.length(), stream,
					Optional.of("demo@localhost.com"), SecurityUtils.DEMO_TENANT, parceiro.get());
			fileService.processNFE(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
