package br.com.mrcontador.file.comprovante;

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
public class TestPdfBB {

	@Autowired
	private FileService fileService;
	@Autowired
	private ParceiroService parceiroService;
	@Autowired
	private AgenciabancariaService agenciaService;

	@Test
	public void test() throws Exception {
		TenantContext.setTenantSchema("ds_demo");
		testComprovante("/home/alecindro/Documents/drcontabil/docs/DASSOLER/05-2021.pdf", 10L, 22L);
	}
	
	public void testComprovante(String folder, Long parceiroId, Long agenciaId) throws Exception {
		Agenciabancaria agencia = agenciaService.findOne(agenciaId).get();
		Optional<Parceiro> parceiro = parceiroService.findOne(parceiroId);
		process(folder, agencia, parceiro);
	
	}

	public void testBB() throws Exception {
			
			
			String folder1 = "/home/alecindro/Documents/drcontabil/upload/03-2021.pdf";
			String folder2 = "/home/alecindro/Documents/drcontabil/docs/teste/02-2020/2ª Comprovante de Pagamentos  02_2020.pdf";
			String folder3 = "/home/alecindro/Documents/drcontabil/docs/teste/03-2020/2ª Comprovante de Pagamentos  03_2020.pdf";
			String folder4 = "/home/alecindro/Documents/drcontabil/docs/teste/04-2020/2ª Comprovante de Pagamentos  04_2020.pdf";
			String folder5 = "/home/alecindro/Documents/drcontabil/docs/teste/05-2020/2ª Comprovante de Pagamentos  05_2020.pdf";
			String folder6 = "/home/alecindro/Documents/drcontabil/docs/teste/06-2020/2ª Comprovante de Pagamentos  06_2020.pdf";
			Agenciabancaria agencia = agenciaService.findOne(44L).get();
			Optional<Parceiro> parceiro = parceiroService.findOne(15L);
			process(folder1, agencia, parceiro);
		//	process(folder2, agencia, parceiro);
		//	process(folder3, agencia, parceiro);
		//	process(folder4, agencia, parceiro);
		//	process(folder5, agencia, parceiro);
		//	process(folder6, agencia, parceiro);
	}
	
	private void process(String folder, Agenciabancaria agencia, Optional<Parceiro> parceiro) throws Exception {
			File file = new File(folder);
			FileInputStream stream = new FileInputStream(file);
			FileDTO dto = fileService.getFileDTO("application/pdf", file.getName(), file.length(), stream,
					Optional.of("demo"), "ds_demo", parceiro.get(),TipoDocumento.COMPROVANTE);
			fileService.processComprovante(dto, agencia);
	}

}
