package br.com.mrcontador.file.pdf;

public enum PdfReaderEnum {

PLANO_CONTAS_DOMINIO("br.com.mrcontador.file.pdf.PdfPlanoContaDominio","Relatório Domínio PDF");
	
	private String clazz;
	private String identifier;	
	
	
	private PdfReaderEnum(String clazz, String identifier) {
		this.clazz = clazz;
		this.identifier = identifier;
	}
	
	public String getClazz() {
		return clazz;
	}
	public String getIdentifier() {
		return identifier;
	}
	
	public static String getClass(String identifier){
		for(PdfReaderEnum pdfReaderEnum : PdfReaderEnum.values()) {
			if(pdfReaderEnum.getIdentifier().equalsIgnoreCase(identifier)) {
				return pdfReaderEnum.clazz;
			}
		}
		return PLANO_CONTAS_DOMINIO.clazz;
	}
	
	
}
