package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

public class PermissaoParceiroCriteria implements Serializable, Criteria{

	private static final long serialVersionUID = 1L;
	private LongFilter id;
	private StringFilter usuario;
	private LocalDateFilter dataCadastro;
	private LongFilter parceiroId;
	
	public PermissaoParceiroCriteria() {
		
	}
	public PermissaoParceiroCriteria(PermissaoParceiroCriteria other) {
		this.id = other.id == null ? null : other.id.copy();
		this.usuario = other.usuario == null ? null : other.usuario.copy();
		this.dataCadastro = other.dataCadastro == null ? null : other.dataCadastro.copy();
		this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
	}
	
	public LongFilter getId() {
		return id;
	}



	public void setId(LongFilter id) {
		this.id = id;
	}



	public StringFilter getUsuario() {
		return usuario;
	}



	public void setUsuario(StringFilter usuario) {
		this.usuario = usuario;
	}



	public LocalDateFilter getDataCadastro() {
		return dataCadastro;
	}



	public void setDataCadastro(LocalDateFilter dataCadastro) {
		this.dataCadastro = dataCadastro;
	}



	public LongFilter getParceiroId() {
		return parceiroId;
	}



	public void setParceiroId(LongFilter parceiroId) {
		this.parceiroId = parceiroId;
	}



	@Override
	public Criteria copy() {
		return new PermissaoParceiroCriteria(this);
	}
	@Override
	public int hashCode() {
		return Objects.hash(id,usuario,dataCadastro,parceiroId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissaoParceiroCriteria other = (PermissaoParceiroCriteria) obj;
		if (dataCadastro == null) {
			if (other.dataCadastro != null)
				return false;
		} else if (!dataCadastro.equals(other.dataCadastro))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (parceiroId == null) {
			if (other.parceiroId != null)
				return false;
		} else if (!parceiroId.equals(other.parceiroId))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PermissaoParceiroCriteria [id=" + id + ", usuario=" + usuario + ", dataCadastro=" + dataCadastro
				+ ", parceiroId=" + parceiroId + "]";
	}
	
	

}
