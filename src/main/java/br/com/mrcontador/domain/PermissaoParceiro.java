package br.com.mrcontador.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "permissao_parceiro")
public class PermissaoParceiro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;
    
    @Column(name = "usuario")
    private String usuario;
    
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "permissaos", allowSetters = true)    
    private Parceiro parceiro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public PermissaoParceiro id(Long id) {
		this.id = id;
		return this;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public PermissaoParceiro dataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
		return this;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public PermissaoParceiro usuario(String usuario) {
		this.usuario = usuario;
		return this;
	}

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}
	
	public PermissaoParceiro parceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		PermissaoParceiro other = (PermissaoParceiro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PermissaoParceiro [id=" + id + ", dataCadastro=" + dataCadastro + ", usuario=" + usuario + ", parceiro="
				+ parceiro + "]";
	}
    
    
}
