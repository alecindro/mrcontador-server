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
    private Integer nse_numero;

    @Column(name = "nse_descricao", length = 50)
    private String nse_descricao;

    @Column(name = "nse_cnpj", length = 20)
    private String nse_cnpj;

    @Column(name = "nse_empresa", length = 60)
    private String nse_empresa;

    @Column(name = "nse_datasaida")
    private ZonedDateTime nse_datasaida;

    @Column(name = "nse_valornota", precision = 21, scale = 2)
    private BigDecimal nse_valornota;

    @Column(name = "nse_dataparcela")
    private ZonedDateTime nse_dataparcela;

    @Column(name = "nse_valorparcela", precision = 21, scale = 2)
    private BigDecimal nse_valorparcela;

    @Column(name = "tno_codigo")
    private Integer tno_codigo;

    @Column(name = "nse_parcela", length = 10)
    private String nse_parcela;

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

    public Integer getNse_numero() {
        return nse_numero;
    }

    public Notaservico nse_numero(Integer nse_numero) {
        this.nse_numero = nse_numero;
        return this;
    }

    public void setNse_numero(Integer nse_numero) {
        this.nse_numero = nse_numero;
    }

    public String getNse_descricao() {
        return nse_descricao;
    }

    public Notaservico nse_descricao(String nse_descricao) {
        this.nse_descricao = nse_descricao;
        return this;
    }

    public void setNse_descricao(String nse_descricao) {
        this.nse_descricao = nse_descricao;
    }

    public String getNse_cnpj() {
        return nse_cnpj;
    }

    public Notaservico nse_cnpj(String nse_cnpj) {
        this.nse_cnpj = nse_cnpj;
        return this;
    }

    public void setNse_cnpj(String nse_cnpj) {
        this.nse_cnpj = nse_cnpj;
    }

    public String getNse_empresa() {
        return nse_empresa;
    }

    public Notaservico nse_empresa(String nse_empresa) {
        this.nse_empresa = nse_empresa;
        return this;
    }

    public void setNse_empresa(String nse_empresa) {
        this.nse_empresa = nse_empresa;
    }

    public ZonedDateTime getNse_datasaida() {
        return nse_datasaida;
    }

    public Notaservico nse_datasaida(ZonedDateTime nse_datasaida) {
        this.nse_datasaida = nse_datasaida;
        return this;
    }

    public void setNse_datasaida(ZonedDateTime nse_datasaida) {
        this.nse_datasaida = nse_datasaida;
    }

    public BigDecimal getNse_valornota() {
        return nse_valornota;
    }

    public Notaservico nse_valornota(BigDecimal nse_valornota) {
        this.nse_valornota = nse_valornota;
        return this;
    }

    public void setNse_valornota(BigDecimal nse_valornota) {
        this.nse_valornota = nse_valornota;
    }

    public ZonedDateTime getNse_dataparcela() {
        return nse_dataparcela;
    }

    public Notaservico nse_dataparcela(ZonedDateTime nse_dataparcela) {
        this.nse_dataparcela = nse_dataparcela;
        return this;
    }

    public void setNse_dataparcela(ZonedDateTime nse_dataparcela) {
        this.nse_dataparcela = nse_dataparcela;
    }

    public BigDecimal getNse_valorparcela() {
        return nse_valorparcela;
    }

    public Notaservico nse_valorparcela(BigDecimal nse_valorparcela) {
        this.nse_valorparcela = nse_valorparcela;
        return this;
    }

    public void setNse_valorparcela(BigDecimal nse_valorparcela) {
        this.nse_valorparcela = nse_valorparcela;
    }

    public Integer getTno_codigo() {
        return tno_codigo;
    }

    public Notaservico tno_codigo(Integer tno_codigo) {
        this.tno_codigo = tno_codigo;
        return this;
    }

    public void setTno_codigo(Integer tno_codigo) {
        this.tno_codigo = tno_codigo;
    }

    public String getNse_parcela() {
        return nse_parcela;
    }

    public Notaservico nse_parcela(String nse_parcela) {
        this.nse_parcela = nse_parcela;
        return this;
    }

    public void setNse_parcela(String nse_parcela) {
        this.nse_parcela = nse_parcela;
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
        return "Notaservico{" +
            "id=" + getId() +
            ", nse_numero=" + getNse_numero() +
            ", nse_descricao='" + getNse_descricao() + "'" +
            ", nse_cnpj='" + getNse_cnpj() + "'" +
            ", nse_empresa='" + getNse_empresa() + "'" +
            ", nse_datasaida='" + getNse_datasaida() + "'" +
            ", nse_valornota=" + getNse_valornota() +
            ", nse_dataparcela='" + getNse_dataparcela() + "'" +
            ", nse_valorparcela=" + getNse_valorparcela() +
            ", tno_codigo=" + getTno_codigo() +
            ", nse_parcela='" + getNse_parcela() + "'" +
            "}";
    }
}
