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
 * A Extrato.
 */
@Entity
@Table(name = "extrato")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Extrato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ext_datalancamento")
    private LocalDate extDatalancamento;

    @Column(name = "ext_historico", length = 90)
    private String extHistorico;

    @Column(name = "ext_numerodocumento", length = 30)
    private String extNumerodocumento;

    @Column(name = "ext_numerocontrole", length = 30)
    private String extNumerocontrole;

    @Column(name = "ext_debito", precision = 21, scale = 2)
    private BigDecimal extDebito;

    @Column(name = "ext_credito", precision = 21, scale = 2)
    private BigDecimal extCredito;

    @Column(name = "ext_descricao", length = 30)
    private String extDescricao;
    
    @Column(name = "info_adicional", length = 254)
    private String infoAdicional;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "extratos", allowSetters = true)
    private Parceiro parceiro;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "extratos", allowSetters = true)
    private Agenciabancaria agenciabancaria;
    
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @NotNull
    @JsonIgnoreProperties(value = "extratos", allowSetters = true)
    private Arquivo arquivo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Extrato extDatalancamento(LocalDate extDatalancamento) {
        this.extDatalancamento = extDatalancamento;
        return this;
    }

    public void setExtDatalancamento(LocalDate extDatalancamento) {
        this.extDatalancamento = extDatalancamento;
    }

    public String getExtHistorico() {
        return extHistorico;
    }

    public Extrato extHistorico(String extHistorico) {
        this.extHistorico = extHistorico;
        return this;
    }

    public void setExtHistorico(String extHistorico) {
        this.extHistorico = extHistorico;
    }

    public String getExtNumerodocumento() {
        return extNumerodocumento;
    }

    public Extrato extNumerodocumento(String extNumerodocumento) {
        this.extNumerodocumento = extNumerodocumento;
        return this;
    }

    public Extrato extNumerocontrole(String extNumerocontrole) {
        this.extNumerocontrole = extNumerocontrole;
        return this;
    }

    public Extrato extDebito(BigDecimal extDebito) {
        this.extDebito = extDebito;
        return this;
    }

    public Extrato extCredito(BigDecimal extCredito) {
        this.extCredito = extCredito;
        return this;
    }

    public Extrato extDescricao(String extDescricao) {
        this.extDescricao = extDescricao;
        return this;
    }

    public String getExtNumerocontrole() {
		return extNumerocontrole;
	}

	public void setExtNumerocontrole(String extNumerocontrole) {
		this.extNumerocontrole = extNumerocontrole;
	}

	public BigDecimal getExtDebito() {
		return extDebito;
	}

	public void setExtDebito(BigDecimal extDebito) {
		this.extDebito = extDebito;
	}

	public BigDecimal getExtCredito() {
		return extCredito;
	}

	public void setExtCredito(BigDecimal extCredito) {
		this.extCredito = extCredito;
	}

	public String getExtDescricao() {
		return extDescricao;
	}

	public void setExtDescricao(String extDescricao) {
		this.extDescricao = extDescricao;
	}

	public LocalDate getExtDatalancamento() {
		return extDatalancamento;
	}

	public void setExtNumerodocumento(String extNumerodocumento) {
		this.extNumerodocumento = extNumerodocumento;
	}
	
	 public Extrato infoAdicional(String infoAdicional) {
		 this.infoAdicional = infoAdicional;
		 return this;
	 }

	public String getInfoAdicional() {
		return infoAdicional;
	}

	public void setInfoAdicional(String infoAdicional) {
		this.infoAdicional = infoAdicional;
	}

	public Parceiro getParceiro() {
        return parceiro;
    }

    public Extrato parceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
        return this;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }

    public Agenciabancaria getAgenciabancaria() {
        return agenciabancaria;
    }

    public Extrato agenciabancaria(Agenciabancaria agenciabancaria) {
        this.agenciabancaria = agenciabancaria;
        return this;
    }

    public void setAgenciabancaria(Agenciabancaria agenciabancaria) {
        this.agenciabancaria = agenciabancaria;
    }
    
    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Extrato)) {
            return false;
        }
        return id != null && id.equals(((Extrato) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
	@Override
	public String toString() {
		return "Extrato [id=" + id + ", extDatalancamento=" + extDatalancamento + ", extHistorico=" + extHistorico
				+ ", extNumerodocumento=" + extNumerodocumento + ", extNumerocontrole=" + extNumerocontrole
				+ ", extDebito=" + extDebito + ", extCredito=" + extCredito + ", extDescricao=" + extDescricao
				+ ", parceiro=" + parceiro + ", agenciabancaria=" + agenciabancaria + ", arquivo=" + arquivo + "]";
	}

  
   
    
}
