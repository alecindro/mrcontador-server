package br.com.mrcontador.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Conta.
 */
@Entity
@Table(name = "conta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "con_conta")
    private Integer conConta;

    @Column(name = "con_classificacao", length = 20)
    private String conClassificacao;

    @Column(name = "con_tipo", length = 1)
    private String conTipo;

    @Column(name = "con_descricao", length = 60)
    private String conDescricao;

    @Column(name = "con_cnpj", length = 18)
    private String conCnpj;

    @Column(name = "con_grau")
    private Integer conGrau;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "contas", allowSetters = true)
    private Parceiro parceiro;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties(value = "parceiro", allowSetters = true)
    private Arquivo arquivo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta conConta(Integer conConta) {
        this.conConta = conConta;
        return this;
    }

    public String getConClassificacao() {
        return conClassificacao;
    }

    public Conta conClassificacao(String conClassificacao) {
        this.conClassificacao = conClassificacao;
        return this;
    }

    public void setConClassificacao(String conClassificacao) {
        this.conClassificacao = conClassificacao;
    }

    public Conta conTipo(String conTipo) {
        this.conTipo = conTipo;
        return this;
    }

    public Conta conDescricao(String conDescricao) {
        this.conDescricao = conDescricao;
        return this;
    }

    public Conta conCnpj(String conCnpj) {
        this.conCnpj = conCnpj;
        return this;
    }

    public Conta conGrau(Integer conGrau) {
        this.conGrau = conGrau;
        return this;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public Conta parceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
        return this;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }

    public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	public Integer getConConta() {
		return conConta;
	}

	public void setConConta(Integer conConta) {
		this.conConta = conConta;
	}

	public String getConTipo() {
		return conTipo;
	}

	public void setConTipo(String conTipo) {
		this.conTipo = conTipo;
	}

	public String getConDescricao() {
		return conDescricao;
	}

	public void setConDescricao(String conDescricao) {
		this.conDescricao = conDescricao;
	}

	public String getConCnpj() {
		return conCnpj;
	}

	public void setConCnpj(String conCnpj) {
		this.conCnpj = conCnpj;
	}

	public Integer getConGrau() {
		return conGrau;
	}

	public void setConGrau(Integer conGrau) {
		this.conGrau = conGrau;
	}

	 // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conta)) {
            return false;
        }
        return id != null && id.equals(((Conta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conta{" +
            "id=" + getId() +
            ", con_conta=" + getConConta() +
            ", con_classificacao='" + getConClassificacao() + "'" +
            ", con_tipo='" + getConTipo() + "'" +
            ", con_descricao='" + getConDescricao() + "'" +
            ", con_cnpj='" + getConCnpj() + "'" +
            ", con_grau=" + getConGrau() +
            "}";
    }
}
