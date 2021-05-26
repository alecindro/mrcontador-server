package br.com.mrcontador.exportacao;

import java.io.FileWriter;
import java.io.IOException;
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
		TenantContext.setTenantSchema("ds_04656282000130");
		InteligentCriteria criteria = new InteligentCriteria();
		LongFilter p = new LongFilter();
		p.setEquals(5L);
		StringFilter f = new StringFilter();
		f.setEquals("42021");
		BooleanFilter b = new BooleanFilter();
		b.setEquals(true);
		criteria.setParceiroId(p);
		criteria.setPeriodo(f);
		criteria.setAssociado(b);
		LongFilter a = new LongFilter();
		a.setEquals(12L);
		criteria.setAgenciabancariaId(a);
		List<Inteligent> list = service.findByCriteria(criteria);
		ExportDominio ex = new ExportDominio();
		Agenciabancaria agencia = agService.findOne(12L).get();
		byte[] result = ex.process(list,agencia.getConta());
		try {
		      FileWriter myWriter = new FileWriter("C:\\java\\testes\\exportacao_dominio.txt");
		      myWriter.write(new String(result));
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
}
