package br.com.mrcontador.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Banco.
 */
@Entity
@Table(name = "banco")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Banco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200)
    @Column(name = "ban_descricao", length = 200)
    private String banDescricao;

    @Size(max = 100)
    @Column(name = "ban_sigla", length = 100)
    private String banSigla;

    @Column(name = "ban_ispb")
    private Integer banIspb;

    @NotNull
    @Size(max = 5)
    @Column(name = "ban_codigobancario", length = 5, nullable = false)
    private String banCodigobancario;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
   

    public String getBanDescricao() {
		return banDescricao;
	}

	public void setBanDescricao(String banDescricao) {
		this.banDescricao = banDescricao;
	}

	public String getBanSigla() {
		return banSigla;
	}

	public void setBanSigla(String banSigla) {
		this.banSigla = banSigla;
	}

	public Integer getBanIspb() {
		return banIspb;
	}

	public void setBanIspb(Integer banIspb) {
		this.banIspb = banIspb;
	}

	public String getBanCodigobancario() {
		return banCodigobancario;
	}

	public void setBanCodigobancario(String banCodigobancario) {
		this.banCodigobancario = banCodigobancario;
	}
	
	 // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Banco)) {
            return false;
        }
        return id != null && id.equals(((Banco) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Banco{" +
            "id=" + getId() +
            ", ban_descricao='" + getBanDescricao() + "'" +
            ", ban_sigla='" + getBanSigla() + "'" +
            ", ban_ispb=" + getBanIspb() +
            ", ban_codigobancario='" + getBanCodigobancario() + "'" +
            "}";
    }
}
