package br.com.mrcontador.service.mapper;

public enum TipoPessoa {
	
	JURIDICA("J",false),
	FISICA("F",true);

	private String value;
	private boolean bValue;
	
	private TipoPessoa(String value, boolean bValue) {
		this.value = value;
		this.bValue = bValue;
				
	}

	public String getValue() {
		return value;
	}

	public boolean isbValue() {
		return bValue;
	}
	
	
}
