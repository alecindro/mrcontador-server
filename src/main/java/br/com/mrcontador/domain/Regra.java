package br.com.mrcontador.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.mrcontador.config.type.TipoRegra;

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

    @Column(name = "reg_historico", length = 60)
    private String regHistorico;

    @Column(name = "reg_todos", length = 1)
    private String regTodos;
    
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;
    
    @Column(name = "aplicacao")
    private Boolean aplicacao;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnoreProperties(value = "regras", allowSetters = true)
    private Parceiro parceiro;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnoreProperties(value = "regras", allowSetters = true)
    private Conta conta;
    
    @Column(name = "tipo_regra")
    @Enumerated(EnumType.STRING)
    private TipoRegra tipoRegra;

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
	
	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public Regra dataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
		return this;
	}
	
	public Boolean getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(Boolean aplicacao) {
		this.aplicacao = aplicacao;
	}
	

	public TipoRegra getTipoRegra() {
		return tipoRegra;
	}

	public void setTipoRegra(TipoRegra tipoRegra) {
		this.tipoRegra = tipoRegra;
	}
	
	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Regra conta(Conta conta) {
		this.conta = conta;
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
		return "Regra [id=" + id + ", regDescricao=" + regDescricao + ", regHistorico="
				+ regHistorico + ", regTodos=" + regTodos + "]";
	}

    // prettier-ignore
 
}
