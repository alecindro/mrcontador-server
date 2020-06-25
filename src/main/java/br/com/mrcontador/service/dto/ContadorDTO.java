package br.com.mrcontador.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Contador} entity.
 */
public class ContadorDTO implements Serializable {
    
    
	private static final long serialVersionUID = 1L;

	private Long id;

    private String razao;

    private String fantasia;

    private String telefones;

    private String datasource;

    private String cnpj;

    private String cidade;

    private String estado;

    private String cep;

    private String email;
    
    private String crc;
    
    private String sistema;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getTelefones() {
        return telefones;
    }

    public void setTelefones(String telefones) {
        this.telefones = telefones;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}
	
	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContadorDTO)) {
            return false;
        }

        return id != null && id.equals(((ContadorDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContadorDTO{" +
            "id=" + getId() +
            ", razao='" + getRazao() + "'" +
            ", fantasia='" + getFantasia() + "'" +
            ", telefones='" + getTelefones() + "'" +
            ", datasource='" + getDatasource() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cep='" + getCep() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
