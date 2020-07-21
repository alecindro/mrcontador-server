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
    private String ban_descricao;

    @Size(max = 100)
    @Column(name = "ban_sigla", length = 100)
    private String ban_sigla;

    @Column(name = "ban_ispb")
    private Integer ban_ispb;

    @NotNull
    @Size(max = 5)
    @Column(name = "ban_codigobancario", length = 5, nullable = false)
    private String ban_codigobancario;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBan_descricao() {
        return ban_descricao;
    }

    public Banco ban_descricao(String ban_descricao) {
        this.ban_descricao = ban_descricao;
        return this;
    }

    public void setBan_descricao(String ban_descricao) {
        this.ban_descricao = ban_descricao;
    }

    public String getBan_sigla() {
        return ban_sigla;
    }

    public Banco ban_sigla(String ban_sigla) {
        this.ban_sigla = ban_sigla;
        return this;
    }

    public void setBan_sigla(String ban_sigla) {
        this.ban_sigla = ban_sigla;
    }

    public Integer getBan_ispb() {
        return ban_ispb;
    }

    public Banco ban_ispb(Integer ban_ispb) {
        this.ban_ispb = ban_ispb;
        return this;
    }

    public void setBan_ispb(Integer ban_ispb) {
        this.ban_ispb = ban_ispb;
    }

    public String getBan_codigobancario() {
        return ban_codigobancario;
    }

    public Banco ban_codigobancario(String ban_codigobancario) {
        this.ban_codigobancario = ban_codigobancario;
        return this;
    }

    public void setBan_codigobancario(String ban_codigobancario) {
        this.ban_codigobancario = ban_codigobancario;
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
            ", ban_descricao='" + getBan_descricao() + "'" +
            ", ban_sigla='" + getBan_sigla() + "'" +
            ", ban_ispb=" + getBan_ispb() +
            ", ban_codigobancario='" + getBan_codigobancario() + "'" +
            "}";
    }
}
