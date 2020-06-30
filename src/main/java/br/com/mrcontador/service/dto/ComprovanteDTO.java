package br.com.mrcontador.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Comprovante} entity.
 */
public class ComprovanteDTO implements Serializable {
    
    private Long id;

    private Integer par_codigo;

    private Integer age_codigo;

    private String com_cnpj;

    private String com_beneficiario;

    private String com_documento;

    private ZonedDateTime com_datavencimento;

    private ZonedDateTime com_datapagamento;

    private BigDecimal com_valordocumento;

    private BigDecimal com_valorpagamento;

    private String com_observacao;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPar_codigo() {
        return par_codigo;
    }

    public void setPar_codigo(Integer par_codigo) {
        this.par_codigo = par_codigo;
    }

    public Integer getAge_codigo() {
        return age_codigo;
    }

    public void setAge_codigo(Integer age_codigo) {
        this.age_codigo = age_codigo;
    }

    public String getCom_cnpj() {
        return com_cnpj;
    }

    public void setCom_cnpj(String com_cnpj) {
        this.com_cnpj = com_cnpj;
    }

    public String getCom_beneficiario() {
        return com_beneficiario;
    }

    public void setCom_beneficiario(String com_beneficiario) {
        this.com_beneficiario = com_beneficiario;
    }

    public String getCom_documento() {
        return com_documento;
    }

    public void setCom_documento(String com_documento) {
        this.com_documento = com_documento;
    }

    public ZonedDateTime getCom_datavencimento() {
        return com_datavencimento;
    }

    public void setCom_datavencimento(ZonedDateTime com_datavencimento) {
        this.com_datavencimento = com_datavencimento;
    }

    public ZonedDateTime getCom_datapagamento() {
        return com_datapagamento;
    }

    public void setCom_datapagamento(ZonedDateTime com_datapagamento) {
        this.com_datapagamento = com_datapagamento;
    }

    public BigDecimal getCom_valordocumento() {
        return com_valordocumento;
    }

    public void setCom_valordocumento(BigDecimal com_valordocumento) {
        this.com_valordocumento = com_valordocumento;
    }

    public BigDecimal getCom_valorpagamento() {
        return com_valorpagamento;
    }

    public void setCom_valorpagamento(BigDecimal com_valorpagamento) {
        this.com_valorpagamento = com_valorpagamento;
    }

    public String getCom_observacao() {
        return com_observacao;
    }

    public void setCom_observacao(String com_observacao) {
        this.com_observacao = com_observacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComprovanteDTO)) {
            return false;
        }

        return id != null && id.equals(((ComprovanteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComprovanteDTO{" +
            "id=" + getId() +
            ", par_codigo=" + getPar_codigo() +
            ", age_codigo=" + getAge_codigo() +
            ", com_cnpj='" + getCom_cnpj() + "'" +
            ", com_beneficiario='" + getCom_beneficiario() + "'" +
            ", com_documento='" + getCom_documento() + "'" +
            ", com_datavencimento='" + getCom_datavencimento() + "'" +
            ", com_datapagamento='" + getCom_datapagamento() + "'" +
            ", com_valordocumento=" + getCom_valordocumento() +
            ", com_valorpagamento=" + getCom_valorpagamento() +
            ", com_observacao='" + getCom_observacao() + "'" +
            "}";
    }
}
