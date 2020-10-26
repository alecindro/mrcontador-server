package br.com.mrcontador.agencia;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Banco;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.domain.TipoAgencia;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.AgenciabancariaService;
import br.com.mrcontador.service.BancoService;
import br.com.mrcontador.service.ContaService;
import br.com.mrcontador.service.ParceiroService;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class AgenciaBancariaTest {

	@Autowired
	private BancoService bancoService;
	@Autowired
	private ContaService contaService;
	@Autowired
	private ParceiroService parceiroService;
	@Autowired
	private AgenciabancariaService agenciaService;
	
	@Test
	public void save() {
		TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
		Agenciabancaria agencia = new Agenciabancaria();
		agencia.setAgeAgencia("3174-7");
		agencia.setAgeDescricao("Banco do Brasil - USFC");
		agencia.setAgeDigito("4");
		agencia.setAgeNumero("40106");
		agencia.setAgeSituacao(true);
		Banco banco = findBanco();
		agencia.setBanco(banco);
		agencia.setTipoAgencia(TipoAgencia.CONTA);
		agencia.setBanCodigobancario(banco.getBanCodigobancario());
		agencia.setConta(findConta());
		agencia.setParceiro(findParceiro());
		agenciaService.save(agencia);
	}
	
	private Banco findBanco() {
		return bancoService.findOne(1L).get();
	}
	
	private Conta findConta() {
		return contaService.findById(22L).get();
	}
	
	private Parceiro findParceiro() {
		return parceiroService.findOne(1L).get();
	}
}
