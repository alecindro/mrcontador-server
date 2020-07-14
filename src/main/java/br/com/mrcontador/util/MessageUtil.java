package br.com.mrcontador.util;

import org.springframework.http.HttpHeaders;

import br.com.mrcontador.erros.MrContadorException;
import io.github.jhipster.web.util.HeaderUtil;

public class MessageUtil {
	
	public static final String LINE_BREAK = "&#10;";
	
	
	public static HttpHeaders generate(String applicationName, MrContadorException e ) {
		return HeaderUtil.createFailureAlert(applicationName, true, e.getDefaultMessasge(), e.getErrorKey(), e.getMessage());
	}




}
