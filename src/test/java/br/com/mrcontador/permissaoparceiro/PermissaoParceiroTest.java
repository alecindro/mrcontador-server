package br.com.mrcontador.permissaoparceiro;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.domain.PermissaoParceiro;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.PermissaoParceiroService;
import br.com.mrcontador.service.dto.PermissaoParceiroDTO;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class PermissaoParceiroTest {
	
	@Autowired
	private PermissaoParceiroService service;

	@Test
	public void create() throws Exception {
	 TenantContext.setTenantSchema(SecurityUtils.DEMO_TENANT);
	 PermissaoParceiro pp = new PermissaoParceiro();
	 pp.setDataCadastro(LocalDate.now());
	 Parceiro parceiro = new Parceiro();
	 parceiro.setId(3L);
	 pp.setParceiro(parceiro);
	 pp.setUsuario("demo@loclahost.com");
	 PermissaoParceiroDTO dto = new PermissaoParceiroDTO();
	 dto.setPermissaos(new ArrayList<>());
	 dto.getPermissaos().add(pp);
	 service.save(dto.getPermissaos());
	 
	}
}
