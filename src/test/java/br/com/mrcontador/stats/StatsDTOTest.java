package br.com.mrcontador.stats;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.StatsService;
import br.com.mrcontador.service.dto.StatsDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class StatsDTOTest {
	
	@Autowired
	private StatsService service;
	
	@Test
	public void test() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		 StatsDTO dto = service.getStats(1L);
		 	 System.out.println(dto.toString());
		 
		 
	}

}
