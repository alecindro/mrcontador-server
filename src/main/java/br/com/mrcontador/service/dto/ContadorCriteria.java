package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Contador} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.ContadorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contadors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContadorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter razao;

    private StringFilter fantasia;

    private StringFilter telefones;

    private StringFilter datasource;

    private StringFilter cnpj;

    private StringFilter cidade;

    private StringFilter estado;

    private StringFilter cep;
    
    private StringFilter logradouro;
    
    private BooleanFilter pessoafisica;

    private StringFilter email;
    
    private StringFilter crc;
    
    private StringFilter sistema;

    public ContadorCriteria() {
    }

    public ContadorCriteria(ContadorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.razao = other.razao == null ? null : other.razao.copy();
        this.fantasia = other.fantasia == null ? null : other.fantasia.copy();
        this.telefones = other.telefones == null ? null : other.telefones.copy();
        this.datasource = other.datasource == null ? null : other.datasource.copy();
        this.cnpj = other.cnpj == null ? null : other.cnpj.copy();
        this.cidade = other.cidade == null ? null : other.cidade.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.cep = other.cep == null ? null : other.cep.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.crc = other.crc == null ? null : other.crc.copy();
        this.sistema = other.sistema == null ? null : other.sistema.copy();
        this.pessoafisica = other.pessoafisica == null ? null : other.pessoafisica;
        this.logradouro = other.logradouro == null ? null : other.logradouro;
        
    }

    @Override
    public ContadorCriteria copy() {
        return new ContadorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRazao() {
        return razao;
    }

    public void setRazao(StringFilter razao) {
        this.razao = razao;
    }

    public StringFilter getFantasia() {
        return fantasia;
    }

    public void setFantasia(StringFilter fantasia) {
        this.fantasia = fantasia;
    }

    public StringFilter getTelefones() {
        return telefones;
    }

    public void setTelefones(StringFilter telefones) {
        this.telefones = telefones;
    }

    public StringFilter getDatasource() {
        return datasource;
    }

    public void setDatasource(StringFilter datasource) {
        this.datasource = datasource;
    }

    public StringFilter getCnpj() {
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
    }

    public StringFilter getCidade() {
        return cidade;
    }

    public void setCidade(StringFilter cidade) {
        this.cidade = cidade;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public StringFilter getCep() {
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }
    
    public StringFilter getCrc() {
		return crc;
	}

	public void setCrc(StringFilter crc) {
		this.crc = crc;
	}

	public StringFilter getSistema() {
		return sistema;
	}

	public void setSistema(StringFilter sistema) {
		this.sistema = sistema;
	}	
	

	public StringFilter getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(StringFilter logradouro) {
		this.logradouro = logradouro;
	}

	public BooleanFilter getPessoafisica() {
		return pessoafisica;
	}

	public void setPessoafisica(BooleanFilter pessoafisica) {
		this.pessoafisica = pessoafisica;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContadorCriteria that = (ContadorCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(razao, that.razao) &&
            Objects.equals(fantasia, that.fantasia) &&
            Objects.equals(telefones, that.telefones) &&
            Objects.equals(datasource, that.datasource) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(cidade, that.cidade) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(cep, that.cep) &&
            Objects.equals(email, that.email) &&
            Objects.equals(crc, that.crc) &&
            Objects.equals(sistema, that.sistema) &&
            Objects.equals(pessoafisica, that.pessoafisica) &&
            Objects.equals(logradouro, that.logradouro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        razao,
        fantasia,
        telefones,
        datasource,
        cnpj,
        cidade,
        estado,
        cep,
        email,
        crc,
        sistema,
        logradouro,
        pessoafisica
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContadorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (razao != null ? "razao=" + razao + ", " : "") +
                (fantasia != null ? "fantasia=" + fantasia + ", " : "") +
                (telefones != null ? "telefones=" + telefones + ", " : "") +
                (datasource != null ? "datasource=" + datasource + ", " : "") +
                (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
                (cidade != null ? "cidade=" + cidade + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (cep != null ? "cep=" + cep + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (crc != null ? "crc=" + crc + ", " : "") +
                (sistema != null ? "sistema=" + sistema + ", " : "") +
                (pessoafisica != null ? "pessoafisica=" + pessoafisica+ ", " : "") +
                (logradouro != null ? "logradouro=" + logradouro + ", " : "") +
            "}";
    }

}
