package br.com.mrcontador.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Regra.
 */
@Entity
@Table(name = "regra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Regra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reg_descricao", length = 60)
    private String regDescricao;

    @Column(name = "reg_conta")
    private Integer regConta;

    @Column(name = "reg_historico", length = 60)
    private String regHistorico;

    @Column(name = "reg_todos", length = 1)
    private String regTodos;
    
    @Column(name = "data_cadastro")
    private ZonedDateTime dataCadastro;
    
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "regras", allowSetters = true)
    private Parceiro parceiro;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegDescricao() {
        return regDescricao;
    }

    public Regra regDescricao(String regDescricao) {
        this.regDescricao = regDescricao;
        return this;
    }
    
    public Regra regConta(Integer regConta) {
        this.regConta = regConta;
        return this;
    }

    public Regra regHistorico(String regHistorico) {
        this.regHistorico = regHistorico;
        return this;
    }

    public void setRegHistorico(String regHistorico) {
        this.regHistorico = regHistorico;
    }

    public String getRegTodos() {
        return regTodos;
    }

    public Regra regTodos(String regTodos) {
        this.regTodos = regTodos;
        return this;
    }

    public Integer getRegConta() {
		return regConta;
	}

	public void setRegConta(Integer regConta) {
		this.regConta = regConta;
	}

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}
	
	public Regra parceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
		return this;
	}

	public String getRegHistorico() {
		return regHistorico;
	}

	public void setRegDescricao(String regDescricao) {
		this.regDescricao = regDescricao;
	}

	public void setRegTodos(String regTodos) {
		this.regTodos = regTodos;
	}
	
	public ZonedDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(ZonedDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public Regra dataCadastro(ZonedDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
		return this;
	}
	
	  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regra)) {
            return false;
        }
        return id != null && id.equals(((Regra) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "Regra [id=" + id + ", regDescricao=" + regDescricao + ", regConta=" + regConta + ", regHistorico="
				+ regHistorico + ", regTodos=" + regTodos + ", parceiro=" + parceiro + "]";
	}

    // prettier-ignore
 
}
