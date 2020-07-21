package br.com.mrcontador.file.comprovante;

import java.util.ArrayList;
import java.util.List;

public class DiffPage {
	
	private Integer page;
	private List<DiffValue> diffValues;
	
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public List<DiffValue> getDiffValues() {
		if(diffValues == null) {
			diffValues = new ArrayList<>();
		}
		return diffValues;
	}
	public void setDiffValues(List<DiffValue> diffValues) {
		this.diffValues = diffValues;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diffValues == null) ? 0 : diffValues.hashCode());
		result = prime * result + ((page == null) ? 0 : page.hashCode());
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
		DiffPage other = (DiffPage) obj;
		if (diffValues == null) {
			if (other.diffValues != null)
				return false;
		} else if (!diffValues.equals(other.diffValues))
			return false;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("page=" + page );
		for(DiffValue diffValue : getDiffValues()) {
			builder.append(diffValue.toString());
		}
		return builder.toString();
	}
	
	
	
	

}
