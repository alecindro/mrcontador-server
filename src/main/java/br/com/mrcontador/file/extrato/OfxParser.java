package br.com.mrcontador.file.extrato;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.file.extrato.dto.ListOfxDto;
import br.com.mrcontador.service.ExtratoService;

public interface OfxParser {
	
	public void process(ListOfxDto listOfxDto,AggregateUnmarshaller<ResponseEnvelope> unmarshaller, InputStream stream) throws IOException, OFXParseException;
	
	public void validate(String banco, String agencia, String conta, Parceiro parceiro, Agenciabancaria agenciaBancaria);
	
	public void callExtrato(ExtratoService extratoService, List<Extrato> extratos,
			Long parceiroId);

}