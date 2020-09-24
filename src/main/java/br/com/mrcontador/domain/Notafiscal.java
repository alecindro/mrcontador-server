package br.com.mrcontador.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Notafiscal.
 */
@Entity
@Table(name = "notafiscal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notafiscal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "not_numero")
    private String notNumero;

    @Column(name = "not_descricao", length = 50)
    private String notDescricao;

    @Column(name = "not_cnpj", length = 20)
    private String notCnpj;

    @Column(name = "not_empresa", length = 60)
    private String notEmpresa;

    @Column(name = "not_datasaida")
    private LocalDate notDatasaida;

    @Column(name = "not_valornota", precision = 21, scale = 2)
    private BigDecimal notValornota;

    @Column(name = "not_dataparcela")
    private LocalDate notDataparcela;

    @Column(name = "not_valorparcela", precision = 21, scale = 2)
    private BigDecimal notValorparcela;

    @Column(name = "tno_codigo")
    private Integer tnoCodigo;

    @Column(name = "not_parcela", length = 10)
    private String notParcela;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnoreProperties(value = "notafiscals", allowSetters = true)
    private Parceiro parceiro;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "notas", allowSetters = true)
    private Arquivo arquivo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="arquivopdf_id")
    @JsonIgnoreProperties(value = "notas", allowSetters = true)
    private Arquivo arquivoPDF;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotNumero() {
        return notNumero;
    }

    public Notafiscal notNumero(String notNumero) {
        this.notNumero = notNumero;
        return this;
    }

    public Notafiscal notDescricao(String notDescricao) {
        this.notDescricao = notDescricao;
        return this;
    }


    public Notafiscal notCnpj(String notCnpj) {
        this.notCnpj = notCnpj;
        return this;
    }

    public Notafiscal notEmpresa(String notEmpresa) {
        this.notEmpresa = notEmpresa;
        return this;
    }

    public Notafiscal notDatasaida(LocalDate notDatasaida) {
        this.notDatasaida = notDatasaida;
        return this;
    }

    public void setNotDatasaida(LocalDate notDatasaida) {
        this.notDatasaida = notDatasaida;
    }

    public Notafiscal notValornota(BigDecimal notValornota) {
        this.notValornota = notValornota;
        return this;
    }

    public Notafiscal notDataparcela(LocalDate notDataparcela) {
        this.notDataparcela = notDataparcela;
        return this;
    }

    public Notafiscal notValorparcela(BigDecimal notValorparcela) {
        this.notValorparcela = notValorparcela;
        return this;
    }
    public Notafiscal tnoCodigo(Integer tnoCodigo) {
        this.tnoCodigo = tnoCodigo;
        return this;
    }

    public Notafiscal notParcela(String notParcela) {
        this.notParcela = notParcela;
        return this;
    }
    public Parceiro getParceiro() {
        return parceiro;
    }

    public Notafiscal parceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
        return this;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }
  
    public String getNotDescricao() {
		return notDescricao;
	}

	public void setNotDescricao(String notDescricao) {
		this.notDescricao = notDescricao;
	}

	public String getNotCnpj() {
		return notCnpj;
	}

	public void setNotCnpj(String notCnpj) {
		this.notCnpj = notCnpj;
	}

	public String getNotEmpresa() {
		return notEmpresa;
	}

	public void setNotEmpresa(String notEmpresa) {
		this.notEmpresa = notEmpresa;
	}

	public BigDecimal getNotValornota() {
		return notValornota;
	}

	public void setNotValornota(BigDecimal notValornota) {
		this.notValornota = notValornota;
	}

	public LocalDate getNotDataparcela() {
		return notDataparcela;
	}

	public void setNotDataparcela(LocalDate notDataparcela) {
		this.notDataparcela = notDataparcela;
	}

	public BigDecimal getNotValorparcela() {
		return notValorparcela;
	}

	public void setNotValorparcela(BigDecimal notValorparcela) {
		this.notValorparcela = notValorparcela;
	}

	public Integer getTnoCodigo() {
		return tnoCodigo;
	}

	public void setTnoCodigo(Integer tnoCodigo) {
		this.tnoCodigo = tnoCodigo;
	}

	public String getNotParcela() {
		return notParcela;
	}

	public void setNotParcela(String notParcela) {
		this.notParcela = notParcela;
	}

	public LocalDate getNotDatasaida() {
		return notDatasaida;
	}

	public void setNotNumero(String notNumero) {
		this.notNumero = notNumero;
	}
	
	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	public Arquivo getArquivoPDF() {
		return arquivoPDF;
	}

	public void setArquivoPDF(Arquivo arquivoPDF) {
		this.arquivoPDF = arquivoPDF;
	}
	
	  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notafiscal)) {
            return false;
        }
        return id != null && id.equals(((Notafiscal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "Notafiscal [id=" + id + ", notNumero=" + notNumero + ", notDescricao=" + notDescricao + ", notCnpj="
				+ notCnpj + ", notEmpresa=" + notEmpresa + ", notDatasaida=" + notDatasaida + ", notValornota="
				+ notValornota + ", notDataparcela=" + notDataparcela + ", notValorparcela=" + notValorparcela
				+ ", tnoCodigo=" + tnoCodigo + ", notParcela=" + notParcela +"]";
	}
    
    

}
