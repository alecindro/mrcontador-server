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
import br.com.mrcontador.file.TipoDocumento;
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
	public void create() throws Exception {
		 TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/Plano de Contas - mercado dassoler.pdf";
		File file = new File(folder);
		FileInputStream stream = new FileInputStream(file);
		Parceiro parceiro = new Parceiro();
		parceiro.setId(1L);
		FileDTO dto = fileService.getFileDTO("application/pdf", file.getName(), file.length(), stream, Optional.of("demo@localhost.com"),
				SecurityUtils.DEMO_TENANT, parceiro,TipoDocumento.PLANO_DE_CONTA);
		fileService.processPlanoConta(dto, SistemaPlanoConta.DOMINIO_SISTEMAS);
		
	}

}
