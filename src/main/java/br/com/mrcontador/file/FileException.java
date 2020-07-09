package br.com.mrcontador.file;

public class FileException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String errorKey;
	private String defaultMesasge;
	
	public FileException(String errorKey, String message, Throwable e) {
		super(message,e);
		this.defaultMesasge = message;
		this.errorKey = errorKey;
	}
	
	public FileException(String errorKey, String message) {
		super(message);
		this.defaultMesasge = message;
		this.errorKey = errorKey;
	}
	
	public FileException(String errorKey) {
		this.errorKey = errorKey;
	}
	
	public String getErrorKey() {
		return errorKey;
	}

	public String getDefaultMesasge() {
		return defaultMesasge;
	}
	

}
