package br.com.mrcontador.file.comprovante;

public class DiffValue {
	
	@Override
	public String toString() {
		return "DiffValues [oldValue=" + oldValue + ", newValue=" + newValue + "]";
	}
	private String oldValue;
	private String newValue;
	
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	
	

}
