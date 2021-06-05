package br.com.mrcontador.inteligent;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.repository.InteligentRepository.InteligentStats;
import br.com.mrcontador.service.InteligentService;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class InteligentStatsTest {
	
	@Autowired
	private InteligentService service;
	
	@Test
	public void test() {
		 List<InteligentStats> list = service.getInteligentStats(1L);
		 for(InteligentStats s : list) {
			 System.out.println(s.getPeriodo());
		 }
		 
	}

}
