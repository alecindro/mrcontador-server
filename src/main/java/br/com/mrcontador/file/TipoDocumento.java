package br.com.mrcontador.file;

public enum TipoDocumento {

	PLANO_DE_CONTA("Plano de conta", "planoconta"),
	NOTA("Nota","nota"),
	COMPROVANTE("Comprovante","comprovante");
	
	private String indexName;
	
	private String tipoDoc;
	
	private TipoDocumento(String tipoDoc, String indeName) {
		this.tipoDoc = tipoDoc;
		this.indexName = indeName;
	}
	
	public String getTipoDoc() {
		return tipoDoc;
	}

	public String getIndexName() {
		return indexName;
	}
	
}
