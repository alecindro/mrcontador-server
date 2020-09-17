package br.com.mrcontador.file.comprovante;

public class DiffValue implements Comparable<DiffValue> {
	
	@Override
	public String toString() {
		return "DiffValues [oldValue=" + oldValue + ", newValue=" + newValue + "]";
	}
	private String oldValue;
	private String newValue;
	private int position;
	private Integer line;
	
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
	
	public DiffValue newValue(String newValue) {
		this.newValue = newValue;
		return this;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public Integer getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		result = prime * result + ((newValue == null) ? 0 : newValue.hashCode());
		result = prime * result + ((oldValue == null) ? 0 : oldValue.hashCode());
		result = prime * result + position;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiffValue other = (DiffValue) obj;
		if (line == null) {
			if (other.line != null)
				return false;
		} else if (!line.equals(other.line))
			return false;
		if (oldValue == null) {
			if (other.oldValue != null)
				return false;
		} else if (!oldValue.equals(other.oldValue))
			return false;
		if (position != other.position)
			return false;
		return true;
	}
	@Override
	public int compareTo(DiffValue arg0) {
		if(arg0.equals(this)) {
			return 0;
		}
		return -1;
	}
	
	
	
}
