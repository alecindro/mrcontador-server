package br.com.mrcontador.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
public class TestPlanoDeContas {

	
	@Autowired
	private FileService fileService;
	@Autowired
	private ParceiroService parceiroService;

	
	@Test
	public void teste() {
		TenantContext.setTenantSchema("ds_04656282000130");
		String folder = "/home/alecindro/Documents/drcontabil/docs/Plano de Contas - CDCL.pdf";
		File initialFile = new File(folder);
		try {
			InputStream stream = new FileInputStream(initialFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			stream.transferTo(baos);
			stream.close();
			Optional<Parceiro> oParceiro = parceiroService.findOne(26L);
			FileDTO dto = fileService.getFileDTO("application/pdf", initialFile.getName(), initialFile.length(), stream,Optional.of("alecindrocastillho@gmail.com"), "ds_04656282000130",
					 oParceiro.get(),TipoDocumento.PLANO_DE_CONTA);
     		fileService.processPlanoConta(dto,SistemaPlanoConta.DOMINIO_SISTEMAS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
