package br.com.mrcontador.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

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
 * A Comprovante.
 */
@Entity
@Table(name = "comprovante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comprovante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "par_codigo")
    private Integer parCodigo;

    @Column(name = "age_codigo")
    private Integer ageCodigo;

    @Column(name = "com_cnpj", length = 18)
    private String comCnpj;

    @Column(name = "com_beneficiario", length = 60)
    private String comBeneficiario;

    @Column(name = "com_documento", length = 25)
    private String comDocumento;

    @Column(name = "com_datavencimento")
    private LocalDate comDatavencimento;

    @Column(name = "com_datapagamento")
    private LocalDate comDatapagamento;

    @Column(name = "com_valordocumento", precision = 21, scale = 2)
    private BigDecimal comValordocumento;

    @Column(name = "com_valorpagamento", precision = 21, scale = 2)
    private BigDecimal comValorpagamento;

    @Column(name = "com_observacao", length = 90)
    private String comObservacao;
    
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "comprovantes", allowSetters = true)
    private Parceiro parceiro;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "comprovantes", allowSetters = true)
    private Agenciabancaria agenciabancaria;
    
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "comprovantes", allowSetters = true)
    private Arquivo arquivo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Comprovante parCodigo(Integer parCodigo) {
        this.parCodigo = parCodigo;
        return this;
    }



    public Comprovante ageCodigo(Integer ageCodigo) {
        this.ageCodigo = ageCodigo;
        return this;
    }

    public Comprovante com_cnpj(String comCnpj) {
        this.comCnpj = comCnpj;
        return this;
    }

    public Comprovante comBeneficiario(String comBeneficiario) {
        this.comBeneficiario = comBeneficiario;
        return this;
    }


    public Comprovante comDocumento(String comDocumento) {
        this.comDocumento = comDocumento;
        return this;
    }


    public Comprovante comDatavencimento(LocalDate comDatavencimento) {
        this.comDatavencimento = comDatavencimento;
        return this;
    }

    public Comprovante comDatapagamento(LocalDate comDatapagamento) {
        this.comDatapagamento = comDatapagamento;
        return this;
    }


    public Comprovante comValordocumento(BigDecimal comValordocumento) {
        this.comValordocumento = comValordocumento;
        return this;
    }


    public Comprovante comValorpagamento(BigDecimal comValorpagamento) {
        this.comValorpagamento = comValorpagamento;
        return this;
    }
    public Comprovante com_observacao(String comObservacao) {
        this.comObservacao = comObservacao;
        return this;
    }
    
    public Integer getParCodigo() {
		return parCodigo;
	}

	public void setParCodigo(Integer parCodigo) {
		this.parCodigo = parCodigo;
	}

	public Integer getAgeCodigo() {
		return ageCodigo;
	}

	public void setAgeCodigo(Integer ageCodigo) {
		this.ageCodigo = ageCodigo;
	}

	public String getComCnpj() {
		return comCnpj;
	}

	public void setComCnpj(String comCnpj) {
		this.comCnpj = comCnpj;
	}

	public String getComBeneficiario() {
		return comBeneficiario;
	}

	public void setComBeneficiario(String comBeneficiario) {
		this.comBeneficiario = comBeneficiario;
	}

	public String getComDocumento() {
		return comDocumento;
	}

	public void setComDocumento(String comDocumento) {
		this.comDocumento = comDocumento;
	}

	public LocalDate getComDatavencimento() {
		return comDatavencimento;
	}

	public void setComDatavencimento(LocalDate comDatavencimento) {
		this.comDatavencimento = comDatavencimento;
	}

	public LocalDate getComDatapagamento() {
		return comDatapagamento;
	}

	public void setComDatapagamento(LocalDate comDatapagamento) {
		this.comDatapagamento = comDatapagamento;
	}

	public BigDecimal getComValordocumento() {
		return comValordocumento;
	}

	public void setComValordocumento(BigDecimal comValordocumento) {
		this.comValordocumento = comValordocumento;
	}

	public BigDecimal getComValorpagamento() {
		return comValorpagamento;
	}

	public void setComValorpagamento(BigDecimal comValorpagamento) {
		this.comValorpagamento = comValorpagamento;
	}

	public String getComObservacao() {
		return comObservacao;
	}

	public void setComObservacao(String comObservacao) {
		this.comObservacao = comObservacao;
	}

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}
	
	public Comprovante parceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
		return this;
	}

	public Agenciabancaria getAgenciabancaria() {
		return agenciabancaria;
	}

	public void setAgenciabancaria(Agenciabancaria agenciabancaria) {
		this.agenciabancaria = agenciabancaria;
	}
	
	public Comprovante agenciabancaria(Agenciabancaria agenciabancaria) {
		this.agenciabancaria = agenciabancaria;
		return this;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	public Comprovante arquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
		return this;
	}
	
	   // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comprovante)) {
            return false;
        }
        return id != null && id.equals(((Comprovante) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comprovante{" +
            "id=" + getId() +
            ", par_codigo=" + getParCodigo() +
            ", age_codigo=" + getAgeCodigo() +
            ", com_cnpj='" + getComCnpj() + "'" +
            ", com_beneficiario='" + getComBeneficiario() + "'" +
            ", com_documento='" + getComDocumento() + "'" +
            ", com_datavencimento='" + getComDatavencimento() + "'" +
            ", com_datapagamento='" + getComDatapagamento() + "'" +
            ", com_valordocumento=" + getComValordocumento() +
            ", com_valorpagamento=" + getComValorpagamento() +
            ", com_observacao='" + getComObservacao() + "'" +
            "}";
    }
}
