package br.com.mrcontador.client;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.mrcontador.client.dto.PessoaJuridica;

@Service
public class CnpjClient {
	
	@Value("${client.cnpj.url}")
	private String url;
	@Autowired
	@Qualifier("restTemplateClient")
	private RestTemplate restTemplate;
	
	public PessoaJuridica fromRecitaWs(String cnpj) {
		String _url = MessageFormat.format(url, cnpj);
		ResponseEntity<PessoaJuridica> response = restTemplate.getForEntity(_url,PessoaJuridica.class);
		PessoaJuridica juridica = response.getBody();
		juridica.setCnpj(cnpj);
		return juridica;
	}
	

}
