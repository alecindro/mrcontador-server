package br.com.mrcontador.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link br.com.mrcontador.domain.Extrato} entity.
 */
public class ExtratoDTO implements Serializable {
    
    private Long id;

    private ZonedDateTime ext_datalancamento;

    private String ext_historico;

    private String ext_numerodocumento;

    private String ext_numerocontrole;

    private BigDecimal ext_debito;

    private BigDecimal ext_credito;

    private String ext_descricao;


    private Long parceiroId;

    private Long agenciabancariaId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getExt_datalancamento() {
        return ext_datalancamento;
    }

    public void setExt_datalancamento(ZonedDateTime ext_datalancamento) {
        this.ext_datalancamento = ext_datalancamento;
    }

    public String getExt_historico() {
        return ext_historico;
    }

    public void setExt_historico(String ext_historico) {
        this.ext_historico = ext_historico;
    }

    public String getExt_numerodocumento() {
        return ext_numerodocumento;
    }

    public void setExt_numerodocumento(String ext_numerodocumento) {
        this.ext_numerodocumento = ext_numerodocumento;
    }

    public String getExt_numerocontrole() {
        return ext_numerocontrole;
    }

    public void setExt_numerocontrole(String ext_numerocontrole) {
        this.ext_numerocontrole = ext_numerocontrole;
    }

    public BigDecimal getExt_debito() {
        return ext_debito;
    }

    public void setExt_debito(BigDecimal ext_debito) {
        this.ext_debito = ext_debito;
    }

    public BigDecimal getExt_credito() {
        return ext_credito;
    }

    public void setExt_credito(BigDecimal ext_credito) {
        this.ext_credito = ext_credito;
    }

    public String getExt_descricao() {
        return ext_descricao;
    }

    public void setExt_descricao(String ext_descricao) {
        this.ext_descricao = ext_descricao;
    }

    public Long getParceiroId() {
        return parceiroId;
    }

    public void setParceiroId(Long parceiroId) {
        this.parceiroId = parceiroId;
    }

    public Long getAgenciabancariaId() {
        return agenciabancariaId;
    }

    public void setAgenciabancariaId(Long agenciabancariaId) {
        this.agenciabancariaId = agenciabancariaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtratoDTO)) {
            return false;
        }

        return id != null && id.equals(((ExtratoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtratoDTO{" +
            "id=" + getId() +
            ", ext_datalancamento='" + getExt_datalancamento() + "'" +
            ", ext_historico='" + getExt_historico() + "'" +
            ", ext_numerodocumento='" + getExt_numerodocumento() + "'" +
            ", ext_numerocontrole='" + getExt_numerocontrole() + "'" +
            ", ext_debito=" + getExt_debito() +
            ", ext_credito=" + getExt_credito() +
            ", ext_descricao='" + getExt_descricao() + "'" +
            ", parceiroId=" + getParceiroId() +
            ", agenciabancariaId=" + getAgenciabancariaId() +
            "}";
    }
}
