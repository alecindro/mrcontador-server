package br.com.mrcontador.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Notaservico} entity.
 */
public class NotaservicoDTO implements Serializable {
    
    private Long id;

    private Integer nse_numero;

    private String nse_descricao;

    private String nse_cnpj;

    private String nse_empresa;

    private ZonedDateTime nse_datasaida;

    private BigDecimal nse_valornota;

    private ZonedDateTime nse_dataparcela;

    private BigDecimal nse_valorparcela;

    private Integer tno_codigo;

    private String nse_parcela;


    private Long parceiroId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNse_numero() {
        return nse_numero;
    }

    public void setNse_numero(Integer nse_numero) {
        this.nse_numero = nse_numero;
    }

    public String getNse_descricao() {
        return nse_descricao;
    }

    public void setNse_descricao(String nse_descricao) {
        this.nse_descricao = nse_descricao;
    }

    public String getNse_cnpj() {
        return nse_cnpj;
    }

    public void setNse_cnpj(String nse_cnpj) {
        this.nse_cnpj = nse_cnpj;
    }

    public String getNse_empresa() {
        return nse_empresa;
    }

    public void setNse_empresa(String nse_empresa) {
        this.nse_empresa = nse_empresa;
    }

    public ZonedDateTime getNse_datasaida() {
        return nse_datasaida;
    }

    public void setNse_datasaida(ZonedDateTime nse_datasaida) {
        this.nse_datasaida = nse_datasaida;
    }

    public BigDecimal getNse_valornota() {
        return nse_valornota;
    }

    public void setNse_valornota(BigDecimal nse_valornota) {
        this.nse_valornota = nse_valornota;
    }

    public ZonedDateTime getNse_dataparcela() {
        return nse_dataparcela;
    }

    public void setNse_dataparcela(ZonedDateTime nse_dataparcela) {
        this.nse_dataparcela = nse_dataparcela;
    }

    public BigDecimal getNse_valorparcela() {
        return nse_valorparcela;
    }

    public void setNse_valorparcela(BigDecimal nse_valorparcela) {
        this.nse_valorparcela = nse_valorparcela;
    }

    public Integer getTno_codigo() {
        return tno_codigo;
    }

    public void setTno_codigo(Integer tno_codigo) {
        this.tno_codigo = tno_codigo;
    }

    public String getNse_parcela() {
        return nse_parcela;
    }

    public void setNse_parcela(String nse_parcela) {
        this.nse_parcela = nse_parcela;
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
        if (!(o instanceof NotaservicoDTO)) {
            return false;
        }

        return id != null && id.equals(((NotaservicoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotaservicoDTO{" +
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
            ", parceiroId=" + getParceiroId() +
            "}";
    }
}
