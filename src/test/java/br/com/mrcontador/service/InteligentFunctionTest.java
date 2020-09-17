package br.com.mrcontador.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.security.SecurityUtils;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class InteligentFunctionTest {

	@Autowired
	private InteligentService service;
	private Long parceiroID = 2L;
	private Long agenciaBancariaID = 2L;
	private String periodo = "12020";

	@Test
	public void test() {
		try {
			TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
			System.out.println("rESULTADO: " + service.processInteligent(parceiroID, agenciaBancariaID, periodo));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
