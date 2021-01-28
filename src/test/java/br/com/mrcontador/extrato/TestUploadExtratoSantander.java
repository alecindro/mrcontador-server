package br.com.mrcontador.extrato;

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
import br.com.mrcontador.file.FileService;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestUploadExtratoSantander {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private AgenciabancariaService agenciaService;
	
	@Autowired
	private ParceiroService parceiroService;
	
	@Test
	public void create() throws Exception{
		  
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder1 = "C:\\Users\\alecindro.castilho\\Documents\\study\\mrcontador\\docs dassoler\\wtf sushi\\12.2019 .pdf";
		
		
		
		Agenciabancaria agencia = agenciaService.findOne(12L).get();
		Optional<Parceiro> parceiro = parceiroService.findOne(4L);
		process(folder1, agencia, parceiro);
		//process(folder2, agencia, parceiro);
		//process(folder3, agencia, parceiro);
		//process(folder4, agencia, parceiro);
		//process(folder5, agencia, parceiro);
		//process(folder6, agencia, parceiro);
	}
	
	private void process(String folder,Agenciabancaria agencia, Optional<Parceiro> parceiro) throws Exception{
		
		File file = new File(folder);
		FileInputStream stream = new FileInputStream(file);
		FileDTO dto = fileService.getFileDTO("application/pdf", file.getName(), file.length(), stream, Optional.of("demo@localhost.com"),
				SecurityUtils.DEMO_TENANT, parceiro.get(), TipoDocumento.EXTRATO);
		fileService.processExtrato(dto, agencia, "application/pdf");
	   
	}

}