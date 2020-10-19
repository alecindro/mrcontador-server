package br.com.mrcontador.exportacao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.export.ExportDominio;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.InteligentQueryService;
import br.com.mrcontador.service.dto.InteligentCriteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles({"dev","no-liquibase"})
@RunWith(SpringRunner.class)
public class ExportDominioTest {

	
	@Autowired
	private InteligentQueryService service;
	@Autowired 
	private AgenciabancariaService agService;
	
	@Test
	public void teste() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		InteligentCriteria criteria = new InteligentCriteria();
		LongFilter p = new LongFilter();
		p.setEquals(2L);
		StringFilter f = new StringFilter();
		f.setEquals("12020");
		BooleanFilter b = new BooleanFilter();
		b.setEquals(true);
		criteria.setParceiroId(p);
		criteria.setPeriodo(f);
		criteria.setAssociado(b);
		List<Inteligent> list = service.findByCriteria(criteria);
		Agenciabancaria agencia = agService.findOne(1L).get();
		ExportDominio ex = new ExportDominio();
		System.out.println(ex.process(list,agencia, "45", "10539433000173"));
	}
}
