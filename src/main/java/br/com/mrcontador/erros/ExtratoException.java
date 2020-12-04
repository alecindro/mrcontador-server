package br.com.mrcontador.erros;

import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import br.com.mrcontador.web.rest.errors.ErrorConstants;

public class ExtratoException extends AbstractThrowableProblem{
	
	private static final long serialVersionUID = 1L;
	private String errorKey;
	private String defaultMessasge;
	
	public ExtratoException(String errorKey, String message, Throwable e) {
		 this(errorKey,message);
		this.defaultMessasge = message;
		this.errorKey = errorKey;
	}
	
	public ExtratoException(String errorKey, Throwable e) {
		this(errorKey,errorKey);
     	this.errorKey = errorKey;
	}
	
	public ExtratoException(String errorKey, String message) {
		 super(ErrorConstants.MR_CONTADOR_TYPE, message, Status.BAD_REQUEST, null, null, null, getAlertParameters(message, errorKey));
		this.defaultMessasge = message;
		this.errorKey = errorKey;
	}
	
	public ExtratoException(String errorKey) {
		this(errorKey,errorKey);
     	this.errorKey = errorKey;
	}
	
	public String getErrorKey() {
		return errorKey;
	}

	public String getDefaultMessasge() {
		return defaultMessasge;
	}
	
	   private static Map<String, Object> getAlertParameters(String param, String errorKey) {
	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("message", "error." + errorKey);
	        parameters.put("params", param);
	        return parameters;
	    }
}