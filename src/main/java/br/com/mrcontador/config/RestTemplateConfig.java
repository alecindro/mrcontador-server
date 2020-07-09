package br.com.mrcontador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	
	@Bean("restTemplateClient")
	public RestTemplate restTemplateClient() {
		return new RestTemplate();
	}
}
