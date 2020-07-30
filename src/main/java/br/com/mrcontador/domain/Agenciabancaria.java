package br.com.mrcontador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Agenciabancaria.
 */
@Entity
@Table(name = "agenciabancaria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Agenciabancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    @Column(name = "age_numero", length = 20)
    private String ageNumero;

    @Size(max = 20)
    @Column(name = "age_digito", length = 20)
    private String ageDigito;

    @Size(max = 6)
    @Column(name = "age_agencia", length = 6)
    private String ageAgencia;

    @Size(max = 30)
    @Column(name = "age_descricao", length = 30)
    private String ageDescricao;

    @Column(name = "age_situacao")
    private Boolean ageSituacao;

    @NotNull
    @Size(max = 5)
    @Column(name = "ban_codigobancario", length = 5, nullable = false)
    private String banCodigobancario;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = "agenciabancarias", allowSetters = true)
    private Banco banco;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "agenciabancarias", allowSetters = true)
    private Parceiro parceiro;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agenciabancaria ageNumero(String ageNumero) {
        this.ageNumero = ageNumero;
        return this;
    }
    
    public Agenciabancaria ageDigito(String ageDigito) {
        this.ageDigito = ageDigito;
        return this;
    }

    public Agenciabancaria ageAgencia(String ageAgencia) {
        this.ageAgencia = ageAgencia;
        return this;
    }

    public Agenciabancaria ageDescricao(String ageDescricao) {
        this.ageDescricao = ageDescricao;
        return this;
    }

    public Agenciabancaria ageSituacao(Boolean ageSituacao) {
        this.ageSituacao = ageSituacao;
        return this;
    }

    public Agenciabancaria banCodigobancario(String banCodigobancario) {
        this.banCodigobancario = banCodigobancario;
        return this;
    }

    public String getAgeNumero() {
		return ageNumero;
	}

	public void setAgeNumero(String ageNumero) {
		this.ageNumero = ageNumero;
	}

	public String getAgeDigito() {
		return ageDigito;
	}

	public void setAgeDigito(String ageDigito) {
		this.ageDigito = ageDigito;
	}

	public String getAgeAgencia() {
		return ageAgencia;
	}

	public void setAgeAgencia(String ageAgencia) {
		this.ageAgencia = ageAgencia;
	}

	public String getAgeDescricao() {
		return ageDescricao;
	}

	public void setAgeDescricao(String ageDescricao) {
		this.ageDescricao = ageDescricao;
	}

	public Boolean getAgeSituacao() {
		return ageSituacao;
	}

	public void setAgeSituacao(Boolean ageSituacao) {
		this.ageSituacao = ageSituacao;
	}

	public String getBanCodigobancario() {
		return banCodigobancario;
	}

	public void setBanCodigobancario(String banCodigobancario) {
		this.banCodigobancario = banCodigobancario;
	}

	public Banco getBanco() {
        return banco;
    }

    public Agenciabancaria banco(Banco banco) {
        this.banco = banco;
        return this;
    }

    public void setBanco(Banco banco) {
    	this.banCodigobancario = banco.getBanCodigobancario();
        this.banco = banco;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public Agenciabancaria parceiro(Parceiro parceiro) {
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
        if (!(o instanceof Agenciabancaria)) {
            return false;
        }
        return id != null && id.equals(((Agenciabancaria) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
    // prettier-ignore
	@Override
	public String toString() {
		return "Agenciabancaria [id=" + id + ", ageNumero=" + ageNumero + ", ageDigito=" + ageDigito + ", ageAgencia="
				+ ageAgencia + ", ageDescricao=" + ageDescricao + ", ageSituacao=" + ageSituacao
				+ ", banCodigobancario=" + banCodigobancario + ", banco=" + banco + ", parceiro=" + parceiro + "]";
	}

   
    
}
