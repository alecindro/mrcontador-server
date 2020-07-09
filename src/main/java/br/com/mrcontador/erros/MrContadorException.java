package br.com.mrcontador.erros;

public class MrContadorException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String errorKey;
	private String defaultMesasge;
	
	public MrContadorException(String errorKey, String message, Throwable e) {
		super(errorKey+" - "+message,e);
		this.defaultMesasge = message;
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey, Throwable e) {
		super(errorKey, e);
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey, String message) {
		super(errorKey+" - "+message);
		this.defaultMesasge = message;
		this.errorKey = errorKey;
	}
	
	public MrContadorException(String errorKey) {
		this.errorKey = errorKey;
	}
	
	public String getErrorKey() {
		return errorKey;
	}

	public String getDefaultMesasge() {
		return defaultMesasge;
	}

}
