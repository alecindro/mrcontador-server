package br.com.mrcontador.file.planoconta;

public enum SistemaPlanoConta {

	DOMINIO_SISTEMAS("Dominio Sistemas");
	
	private String descricao;
	
	private SistemaPlanoConta(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
