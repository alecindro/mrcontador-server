package br.com.mrcontador.file.planoconta;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PlanoContaPdf extends PdfReaderPreserveSpace{
	
	protected final Logger log = LoggerFactory.getLogger(PlanoContaPdf.class);
	
	public PlanoContaPdf() throws IOException {
		super();
	}

}
