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
    private ZonedDateTime ext_datalancamento;

    @Column(name = "ext_historico", length = 90)
    private String ext_historico;

    @Column(name = "ext_numerodocumento", length = 30)
    private String ext_numerodocumento;

    @Column(name = "ext_numerocontrole", length = 30)
    private String ext_numerocontrole;

    @Column(name = "ext_debito", precision = 21, scale = 2)
    private BigDecimal ext_debito;

    @Column(name = "ext_credito", precision = 21, scale = 2)
    private BigDecimal ext_credito;

    @Column(name = "ext_descricao", length = 30)
    private String ext_descricao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "extratoes", allowSetters = true)
    private Parceiro parceiro;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "extratoes", allowSetters = true)
    private Agenciabancaria agenciabancaria;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getExt_datalancamento() {
        return ext_datalancamento;
    }

    public Extrato ext_datalancamento(ZonedDateTime ext_datalancamento) {
        this.ext_datalancamento = ext_datalancamento;
        return this;
    }

    public void setExt_datalancamento(ZonedDateTime ext_datalancamento) {
        this.ext_datalancamento = ext_datalancamento;
    }

    public String getExt_historico() {
        return ext_historico;
    }

    public Extrato ext_historico(String ext_historico) {
        this.ext_historico = ext_historico;
        return this;
    }

    public void setExt_historico(String ext_historico) {
        this.ext_historico = ext_historico;
    }

    public String getExt_numerodocumento() {
        return ext_numerodocumento;
    }

    public Extrato ext_numerodocumento(String ext_numerodocumento) {
        this.ext_numerodocumento = ext_numerodocumento;
        return this;
    }

    public void setExt_numerodocumento(String ext_numerodocumento) {
        this.ext_numerodocumento = ext_numerodocumento;
    }

    public String getExt_numerocontrole() {
        return ext_numerocontrole;
    }

    public Extrato ext_numerocontrole(String ext_numerocontrole) {
        this.ext_numerocontrole = ext_numerocontrole;
        return this;
    }

    public void setExt_numerocontrole(String ext_numerocontrole) {
        this.ext_numerocontrole = ext_numerocontrole;
    }

    public BigDecimal getExt_debito() {
        return ext_debito;
    }

    public Extrato ext_debito(BigDecimal ext_debito) {
        this.ext_debito = ext_debito;
        return this;
    }

    public void setExt_debito(BigDecimal ext_debito) {
        this.ext_debito = ext_debito;
    }

    public BigDecimal getExt_credito() {
        return ext_credito;
    }

    public Extrato ext_credito(BigDecimal ext_credito) {
        this.ext_credito = ext_credito;
        return this;
    }

    public void setExt_credito(BigDecimal ext_credito) {
        this.ext_credito = ext_credito;
    }

    public String getExt_descricao() {
        return ext_descricao;
    }

    public Extrato ext_descricao(String ext_descricao) {
        this.ext_descricao = ext_descricao;
        return this;
    }

    public void setExt_descricao(String ext_descricao) {
        this.ext_descricao = ext_descricao;
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
        return "Extrato{" +
            "id=" + getId() +
            ", ext_datalancamento='" + getExt_datalancamento() + "'" +
            ", ext_historico='" + getExt_historico() + "'" +
            ", ext_numerodocumento='" + getExt_numerodocumento() + "'" +
            ", ext_numerocontrole='" + getExt_numerocontrole() + "'" +
            ", ext_debito=" + getExt_debito() +
            ", ext_credito=" + getExt_credito() +
            ", ext_descricao='" + getExt_descricao() + "'" +
            "}";
    }
}
