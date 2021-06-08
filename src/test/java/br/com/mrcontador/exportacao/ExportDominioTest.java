package br.com.mrcontador.exportacao;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Inteligent;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.export.ExportDominio;
import br.com.mrcontador.export.ExportLancamento;
import br.com.mrcontador.export.ExportLancamentoFactory;
import br.com.mrcontador.file.planoconta.SistemaPlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.ExportacaoService;
import br.com.mrcontador.service.InteligentQueryService;
import br.com.mrcontador.service.ParceiroService;
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
	@Autowired
	private ExportacaoService exportacaoService;
	@Autowired
	private ParceiroService parceiroService;
	
	
	@Test
	public void test2() {
		TenantContext.setTenantSchema("ds_04656282000130");
		Long parceiroId = 3L;
		Long agenciabancariaId = 9L;
		String periodo = "32021";
		
		InteligentCriteria criteria = new InteligentCriteria();
		LongFilter _parceiro = new LongFilter();
		_parceiro.setEquals(parceiroId);
		StringFilter _periodo = new StringFilter();
		_periodo.setEquals(periodo);
		LongFilter _agencia = new LongFilter();
		_agencia.setEquals(agenciabancariaId);
		BooleanFilter _associado = new BooleanFilter();
		_associado.setEquals(true);
		criteria.setParceiroId(_parceiro);
		criteria.setPeriodo(_periodo);
		criteria.setAssociado(_associado);
		criteria.setAgenciabancariaId(_agencia);
		List<Inteligent> inteligents = service.findByCriteria(criteria);
		Agenciabancaria agencia = agService.findOne(agenciabancariaId).get();
		Parceiro parceiro = parceiroService.findOne(parceiroId).get();
		ExportLancamento lancamento = ExportLancamentoFactory.get(SistemaPlanoConta.DOMINIO_SISTEMAS);
		byte[] result = lancamento.process(inteligents, agencia.getConta());
		exportacaoService.save(parceiro, periodo, "alecindro");
		/*try {
		      FileWriter myWriter = new FileWriter("C:\\java\\testes\\exportacao_dominio.txt");
		      myWriter.write(new String(result));
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
*/
	}
	//@Test
	public void teste() {
		TenantContext.setTenantSchema("ds_04656282000130");
		InteligentCriteria criteria = new InteligentCriteria();
		LongFilter p = new LongFilter();
		p.setEquals(6L);
		StringFilter f = new StringFilter();
		f.setEquals("32021");
		BooleanFilter b = new BooleanFilter();
		b.setEquals(true);
		criteria.setParceiroId(p);
		criteria.setPeriodo(f);
		criteria.setAssociado(b);
		LongFilter a = new LongFilter();
		a.setEquals(15L);
		criteria.setAgenciabancariaId(a);
		List<Inteligent> list = service.findByCriteria(criteria);
		ExportDominio ex = new ExportDominio();
		Agenciabancaria agencia = agService.findOne(15L).get();
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
