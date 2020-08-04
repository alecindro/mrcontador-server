package br.com.mrcontador.file.ofx;

import java.io.IOException;
import java.io.InputStream;

import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;

import br.com.mrcontador.file.ofx.dto.ListOfxDto;

public interface OfxParser {
	
	public void process(ListOfxDto listOfxDto,AggregateUnmarshaller<ResponseEnvelope> unmarshaller, InputStream stream) throws IOException, OFXParseException;
	

}
