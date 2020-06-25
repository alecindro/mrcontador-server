package br.com.mrcontador.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.mrcontador.config.Constants;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;

/**
 * A Contador.
 */
@Entity
@Table(name = "contador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "razao")
    private String razao;

    @Column(name = "fantasia")
    private String fantasia;

    @Column(name = "telefones")
    private String telefones;

    @NotNull
    @Column(name = "datasource")
    private String datasource;

    @NotNull
    @Size(min=14,max = 18)
    @Column(length = 18, name = "cnpj", unique = true)
    private String cnpj;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cep")
    private String cep;

    @NotNull
    @Email(regexp = Constants.EMAIL_REGEX)
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true, name = "email")
    private String email;
    
    @NotNull
    @Column(name = "crc", unique = true)
    private String crc;
    
    @NotNull
    @Column(name = "sistema")
    private String sistema;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazao() {
        return razao;
    }

    public Contador razao(String razao) {
        this.razao = razao;
        return this;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getFantasia() {
        return fantasia;
    }

    public Contador fantasia(String fantasia) {
        this.fantasia = fantasia;
        return this;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getTelefones() {
        return telefones;
    }

    public Contador telefones(String telefones) {
        this.telefones = telefones;
        return this;
    }

    public void setTelefones(String telefones) {
        this.telefones = telefones;
    }

    public String getDatasource() {
        return datasource;
    }

    public Contador datasource(String datasource) {
        this.datasource = datasource;
        return this;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Contador cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCidade() {
        return cidade;
    }

    public Contador cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public Contador estado(String estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public Contador cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEmail() {
        return email;
    }

    public Contador email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}
	
    public Contador crc(String crc) {
        this.crc = crc;
        return this;
    }

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	
	 public Contador sistema(String sistema) {
	        this.sistema = sistema;
	        return this;
	    }
	
	

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contador)) {
            return false;
        }
        return id != null && id.equals(((Contador) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contador{" +
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
