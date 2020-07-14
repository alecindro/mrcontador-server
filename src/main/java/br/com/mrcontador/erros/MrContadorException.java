package br.com.mrcontador.erros;

public class MrContadorException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String errorKey;
	private String defaultMessasge;
	
	public MrContadorException(String errorKey, String message, Throwable e) {
		super(message,e);
		this.defaultMessasge = message;
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey, Throwable e) {
		super(e);
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey, String message) {
		super(message);
		this.defaultMessasge = message;
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey) {
		this.errorKey = errorKey;
	}
	
	public String getErrorKey() {
		return errorKey;
	}

	public String getDefaultMessasge() {
		return defaultMessasge;
	}

}
