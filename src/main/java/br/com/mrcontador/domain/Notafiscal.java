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
    private Integer not_numero;

    @Column(name = "not_descricao", length = 50)
    private String not_descricao;

    @Column(name = "not_cnpj", length = 20)
    private String not_cnpj;

    @Column(name = "not_empresa", length = 60)
    private String not_empresa;

    @Column(name = "not_datasaida")
    private ZonedDateTime not_datasaida;

    @Column(name = "not_valornota", precision = 21, scale = 2)
    private BigDecimal not_valornota;

    @Column(name = "not_dataparcela")
    private ZonedDateTime not_dataparcela;

    @Column(name = "not_valorparcela", precision = 21, scale = 2)
    private BigDecimal not_valorparcela;

    @Column(name = "tno_codigo")
    private Integer tno_codigo;

    @Column(name = "not_parcela", length = 10)
    private String not_parcela;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "notafiscals", allowSetters = true)
    private Parceiro parceiro;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNot_numero() {
        return not_numero;
    }

    public Notafiscal not_numero(Integer not_numero) {
        this.not_numero = not_numero;
        return this;
    }

    public void setNot_numero(Integer not_numero) {
        this.not_numero = not_numero;
    }

    public String getNot_descricao() {
        return not_descricao;
    }

    public Notafiscal not_descricao(String not_descricao) {
        this.not_descricao = not_descricao;
        return this;
    }

    public void setNot_descricao(String not_descricao) {
        this.not_descricao = not_descricao;
    }

    public String getNot_cnpj() {
        return not_cnpj;
    }

    public Notafiscal not_cnpj(String not_cnpj) {
        this.not_cnpj = not_cnpj;
        return this;
    }

    public void setNot_cnpj(String not_cnpj) {
        this.not_cnpj = not_cnpj;
    }

    public String getNot_empresa() {
        return not_empresa;
    }

    public Notafiscal not_empresa(String not_empresa) {
        this.not_empresa = not_empresa;
        return this;
    }

    public void setNot_empresa(String not_empresa) {
        this.not_empresa = not_empresa;
    }

    public ZonedDateTime getNot_datasaida() {
        return not_datasaida;
    }

    public Notafiscal not_datasaida(ZonedDateTime not_datasaida) {
        this.not_datasaida = not_datasaida;
        return this;
    }

    public void setNot_datasaida(ZonedDateTime not_datasaida) {
        this.not_datasaida = not_datasaida;
    }

    public BigDecimal getNot_valornota() {
        return not_valornota;
    }

    public Notafiscal not_valornota(BigDecimal not_valornota) {
        this.not_valornota = not_valornota;
        return this;
    }

    public void setNot_valornota(BigDecimal not_valornota) {
        this.not_valornota = not_valornota;
    }

    public ZonedDateTime getNot_dataparcela() {
        return not_dataparcela;
    }

    public Notafiscal not_dataparcela(ZonedDateTime not_dataparcela) {
        this.not_dataparcela = not_dataparcela;
        return this;
    }

    public void setNot_dataparcela(ZonedDateTime not_dataparcela) {
        this.not_dataparcela = not_dataparcela;
    }

    public BigDecimal getNot_valorparcela() {
        return not_valorparcela;
    }

    public Notafiscal not_valorparcela(BigDecimal not_valorparcela) {
        this.not_valorparcela = not_valorparcela;
        return this;
    }

    public void setNot_valorparcela(BigDecimal not_valorparcela) {
        this.not_valorparcela = not_valorparcela;
    }

    public Integer getTno_codigo() {
        return tno_codigo;
    }

    public Notafiscal tno_codigo(Integer tno_codigo) {
        this.tno_codigo = tno_codigo;
        return this;
    }

    public void setTno_codigo(Integer tno_codigo) {
        this.tno_codigo = tno_codigo;
    }

    public String getNot_parcela() {
        return not_parcela;
    }

    public Notafiscal not_parcela(String not_parcela) {
        this.not_parcela = not_parcela;
        return this;
    }

    public void setNot_parcela(String not_parcela) {
        this.not_parcela = not_parcela;
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

    // prettier-ignore
    @Override
    public String toString() {
        return "Notafiscal{" +
            "id=" + getId() +
            ", not_numero=" + getNot_numero() +
            ", not_descricao='" + getNot_descricao() + "'" +
            ", not_cnpj='" + getNot_cnpj() + "'" +
            ", not_empresa='" + getNot_empresa() + "'" +
            ", not_datasaida='" + getNot_datasaida() + "'" +
            ", not_valornota=" + getNot_valornota() +
            ", not_dataparcela='" + getNot_dataparcela() + "'" +
            ", not_valorparcela=" + getNot_valorparcela() +
            ", tno_codigo=" + getTno_codigo() +
            ", not_parcela='" + getNot_parcela() + "'" +
            "}";
    }
}
