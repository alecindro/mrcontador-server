package br.com.mrcontador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;

@Configuration
public class OfxUnMarshall {
	
	
	
	@Bean
	public AggregateUnmarshaller<ResponseEnvelope> aggregateUnmarshaller(){
			return   new AggregateUnmarshaller<>(ResponseEnvelope.class);
	}

}
