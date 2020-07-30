package br.com.mrcontador.service;

import java.util.List;

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
import br.com.mrcontador.service.dto.ParceiroCriteria;
import io.github.jhipster.service.filter.LongFilter;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class ParceiroQueryTest {

	@Autowired
	private ParceiroQueryService query;
	
	@Test
	public void test() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		ParceiroCriteria criteria = new ParceiroCriteria();
		LongFilter filter = new LongFilter();
		filter.setEquals(5L);
		criteria.setId(filter);
		List<Parceiro> parceiros = query.findByCriteria(criteria);
		System.out.println(parceiros.get(0).toString());
	}
}
