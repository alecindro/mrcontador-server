package br.com.mrcontador.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.fincatto.documentofiscal.utils.DFPersister;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.file.notafiscal.pdf.NFDanfeReport;
import br.com.mrcontador.security.SecurityUtils;

public class TestDanfe {
	
	@Test
	public void test() throws Exception {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		String folder = "/home/alecindro/Documents/drcontabil/docs/nfe_erro/42200182873068000140550010193342991992633550.xml";
		String fileDest = "/home/alecindro/Documents/drcontabil/docs/nfe2/nfe.pdf";
		File fileEntry = new File(folder);	
		InputStream stream = new FileInputStream(fileEntry);
		com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfNotaProcessada = new DFPersister()
				.read(com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada.class, stream, false);
		NFDanfeReport nfDanfeReport = new NFDanfeReport(nfNotaProcessada);
		byte[] bytesArray = nfDanfeReport.gerarDanfeNFe(null);
		Path path = Paths.get(fileDest);
		Files.write(path, bytesArray);
	}

}
