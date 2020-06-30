package br.com.mrcontador.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Notafiscal} entity.
 */
public class NotafiscalDTO implements Serializable {
    
    private Long id;

    private Integer not_numero;

    private String not_descricao;

    private String not_cnpj;

    private String not_empresa;

    private ZonedDateTime not_datasaida;

    private BigDecimal not_valornota;

    private ZonedDateTime not_dataparcela;

    private BigDecimal not_valorparcela;

    private Integer tno_codigo;

    private String not_parcela;


    private Long parceiroId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNot_numero() {
        return not_numero;
    }

    public void setNot_numero(Integer not_numero) {
        this.not_numero = not_numero;
    }

    public String getNot_descricao() {
        return not_descricao;
    }

    public void setNot_descricao(String not_descricao) {
        this.not_descricao = not_descricao;
    }

    public String getNot_cnpj() {
        return not_cnpj;
    }

    public void setNot_cnpj(String not_cnpj) {
        this.not_cnpj = not_cnpj;
    }

    public String getNot_empresa() {
        return not_empresa;
    }

    public void setNot_empresa(String not_empresa) {
        this.not_empresa = not_empresa;
    }

    public ZonedDateTime getNot_datasaida() {
        return not_datasaida;
    }

    public void setNot_datasaida(ZonedDateTime not_datasaida) {
        this.not_datasaida = not_datasaida;
    }

    public BigDecimal getNot_valornota() {
        return not_valornota;
    }

    public void setNot_valornota(BigDecimal not_valornota) {
        this.not_valornota = not_valornota;
    }

    public ZonedDateTime getNot_dataparcela() {
        return not_dataparcela;
    }

    public void setNot_dataparcela(ZonedDateTime not_dataparcela) {
        this.not_dataparcela = not_dataparcela;
    }

    public BigDecimal getNot_valorparcela() {
        return not_valorparcela;
    }

    public void setNot_valorparcela(BigDecimal not_valorparcela) {
        this.not_valorparcela = not_valorparcela;
    }

    public Integer getTno_codigo() {
        return tno_codigo;
    }

    public void setTno_codigo(Integer tno_codigo) {
        this.tno_codigo = tno_codigo;
    }

    public String getNot_parcela() {
        return not_parcela;
    }

    public void setNot_parcela(String not_parcela) {
        this.not_parcela = not_parcela;
    }

    public Long getParceiroId() {
        return parceiroId;
    }

    public void setParceiroId(Long parceiroId) {
        this.parceiroId = parceiroId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotafiscalDTO)) {
            return false;
        }

        return id != null && id.equals(((NotafiscalDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotafiscalDTO{" +
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
            ", parceiroId=" + getParceiroId() +
            "}";
    }
}
