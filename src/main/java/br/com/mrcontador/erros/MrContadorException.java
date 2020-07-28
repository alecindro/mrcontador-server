package br.com.mrcontador.erros;

import br.com.mrcontador.web.rest.errors.BadRequestAlertException;
import br.com.mrcontador.web.rest.errors.ErrorConstants;

public class MrContadorException extends BadRequestAlertException{
	
	private static final long serialVersionUID = 1L;
	private String errorKey;
	private String defaultMessasge;
	
	public MrContadorException(String errorKey, String message, Throwable e) {
		 super(ErrorConstants.DEFAULT_TYPE, e.getMessage(), message, errorKey);
		this.defaultMessasge = message;
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey, Throwable e) {
		 super(ErrorConstants.DEFAULT_TYPE, e.getMessage(), e.getMessage(), errorKey);
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey, String message) {
		 super(ErrorConstants.DEFAULT_TYPE, message, message, errorKey);
		this.defaultMessasge = message;
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey) {
		 super(ErrorConstants.DEFAULT_TYPE, "", "", errorKey);
		this.errorKey = errorKey;
	}
	
	public String getErrorKey() {
		return errorKey;
	}

	public String getDefaultMessasge() {
		return defaultMessasge;
	}

}
