package br.com.mrcontador.service;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class InteligentFunctionTest {

	@Autowired
	private InteligentFunction inteligentFunction;
	private Long parceiroID = 2L;
	private Long agenciaBancariaID = 2L;
	private String periodo = "12019";
	private String ds_tenant = "DS_DEMO";

	@Test
	public void test() {
		try {
			inteligentFunction.callInteligent(parceiroID, agenciaBancariaID, periodo, ds_tenant);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
