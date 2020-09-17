package br.com.mrcontador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A Notaservico.
 */
@Entity
@Table(name = "notaservico")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notaservico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nse_numero")
    private Integer nseNumero;

    @Column(name = "nse_descricao", length = 50)
    private String nseDescricao;

    @Column(name = "nse_cnpj", length = 20)
    private String nseCnpj;

    @Column(name = "nse_empresa", length = 60)
    private String nseEmpresa;

    @Column(name = "nse_datasaida")
    private ZonedDateTime nseDatasaida;

    @Column(name = "nse_valornota", precision = 21, scale = 2)
    private BigDecimal nseValornota;

    @Column(name = "nse_dataparcela")
    private ZonedDateTime nseDataparcela;

    @Column(name = "nse_valorparcela", precision = 21, scale = 2)
    private BigDecimal nseValorparcela;

    @Column(name = "tno_codigo")
    private Integer tnoCodigo;

    @Column(name = "nse_parcela", length = 10)
    private String nseParcela;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "notaservicos", allowSetters = true)
    private Parceiro parceiro;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notaservico nseNumero(Integer nseNumero) {
        this.nseNumero = nseNumero;
        return this;
    }

    public Notaservico nseDescricao(String nseDescricao) {
        this.nseDescricao = nseDescricao;
        return this;
    }

    public Notaservico nseCnpj(String nseCnpj) {
        this.nseCnpj = nseCnpj;
        return this;
    }

    public Notaservico nseEmpresa(String nseEmpresa) {
        this.nseEmpresa = nseEmpresa;
        return this;
    }

    public Notaservico nseDatasaida(ZonedDateTime nseDatasaida) {
        this.nseDatasaida = nseDatasaida;
        return this;
    }

    public Notaservico nseValornota(BigDecimal nseValornota) {
        this.nseValornota = nseValornota;
        return this;
    }

    public Notaservico nseDataparcela(ZonedDateTime nseDataparcela) {
        this.nseDataparcela = nseDataparcela;
        return this;
    }

    public Notaservico nseValorparcela(BigDecimal nseValorparcela) {
        this.nseValorparcela = nseValorparcela;
        return this;
    }

    public Notaservico tnoCodigo(Integer tnoCodigo) {
        this.tnoCodigo = tnoCodigo;
        return this;
    }

    public Notaservico nseParcela(String nseParcela) {
        this.nseParcela = nseParcela;
        return this;
    }

    public Integer getNseNumero() {
		return nseNumero;
	}

	public void setNseNumero(Integer nseNumero) {
		this.nseNumero = nseNumero;
	}

	public String getNseDescricao() {
		return nseDescricao;
	}

	public void setNseDescricao(String nseDescricao) {
		this.nseDescricao = nseDescricao;
	}

	public String getNseCnpj() {
		return nseCnpj;
	}

	public void setNseCnpj(String nseCnpj) {
		this.nseCnpj = nseCnpj;
	}

	public String getNseEmpresa() {
		return nseEmpresa;
	}

	public void setNseEmpresa(String nseEmpresa) {
		this.nseEmpresa = nseEmpresa;
	}

	public ZonedDateTime getNseDatasaida() {
		return nseDatasaida;
	}

	public void setNseDatasaida(ZonedDateTime nseDatasaida) {
		this.nseDatasaida = nseDatasaida;
	}

	public BigDecimal getNseValornota() {
		return nseValornota;
	}

	public void setNseValornota(BigDecimal nseValornota) {
		this.nseValornota = nseValornota;
	}

	public ZonedDateTime getNseDataparcela() {
		return nseDataparcela;
	}

	public void setNseDataparcela(ZonedDateTime nseDataparcela) {
		this.nseDataparcela = nseDataparcela;
	}

	public BigDecimal getNseValorparcela() {
		return nseValorparcela;
	}

	public void setNseValorparcela(BigDecimal nseValorparcela) {
		this.nseValorparcela = nseValorparcela;
	}

	public Integer getTnoCodigo() {
		return tnoCodigo;
	}

	public void setTnoCodigo(Integer tnoCodigo) {
		this.tnoCodigo = tnoCodigo;
	}

	public String getNseParcela() {
		return nseParcela;
	}

	public void setNseParcela(String nseParcela) {
		this.nseParcela = nseParcela;
	}

	public Parceiro getParceiro() {
        return parceiro;
    }

    public Notaservico parceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
        return this;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notaservico)) {
            return false;
        }
        return id != null && id.equals(((Notaservico) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

 // prettier-ignore
	@Override
	public String toString() {
		return "Notaservico [id=" + id + ", nseNumero=" + nseNumero + ", nseDescricao=" + nseDescricao + ", nseCnpj="
				+ nseCnpj + ", nseEmpresa=" + nseEmpresa + ", nseDatasaida=" + nseDatasaida + ", nseValornota="
				+ nseValornota + ", nseDataparcela=" + nseDataparcela + ", nseValorparcela=" + nseValorparcela
				+ ", tnoCodigo=" + tnoCodigo + ", nseParcela=" + nseParcela + ", parceiro=" + parceiro + "]";
	}    
    
}
