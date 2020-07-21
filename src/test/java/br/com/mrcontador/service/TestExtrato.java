package br.com.mrcontador.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Parceiro;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class TestExtrato {
	
	@Autowired
	private ExtratoService service;

	@Test
	public void test() {
		Extrato extrato = new Extrato();
		Parceiro parceiro = new Parceiro();
		parceiro.setId(1l);
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setId(4L);
		Arquivo arquivo = new Arquivo();
		arquivo.setParceiro(parceiro);
		arquivo.setNome("teste arquivo");
		extrato.setParceiro(parceiro);
		extrato.setAgenciabancaria(agencia);
		extrato.setArquivo(arquivo);
		extrato.setExtNumerodocumento("1234");
		service.save(extrato);
	}
}
