package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Comprovante} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.ComprovanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comprovantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ComprovanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter par_codigo;

    private IntegerFilter age_codigo;

    private StringFilter com_cnpj;

    private StringFilter com_beneficiario;

    private StringFilter com_documento;

    private ZonedDateTimeFilter com_datavencimento;

    private ZonedDateTimeFilter com_datapagamento;

    private BigDecimalFilter com_valordocumento;

    private BigDecimalFilter com_valorpagamento;

    private StringFilter com_observacao;

    public ComprovanteCriteria() {
    }

    public ComprovanteCriteria(ComprovanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.par_codigo = other.par_codigo == null ? null : other.par_codigo.copy();
        this.age_codigo = other.age_codigo == null ? null : other.age_codigo.copy();
        this.com_cnpj = other.com_cnpj == null ? null : other.com_cnpj.copy();
        this.com_beneficiario = other.com_beneficiario == null ? null : other.com_beneficiario.copy();
        this.com_documento = other.com_documento == null ? null : other.com_documento.copy();
        this.com_datavencimento = other.com_datavencimento == null ? null : other.com_datavencimento.copy();
        this.com_datapagamento = other.com_datapagamento == null ? null : other.com_datapagamento.copy();
        this.com_valordocumento = other.com_valordocumento == null ? null : other.com_valordocumento.copy();
        this.com_valorpagamento = other.com_valorpagamento == null ? null : other.com_valorpagamento.copy();
        this.com_observacao = other.com_observacao == null ? null : other.com_observacao.copy();
    }

    @Override
    public ComprovanteCriteria copy() {
        return new ComprovanteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPar_codigo() {
        return par_codigo;
    }

    public void setPar_codigo(IntegerFilter par_codigo) {
        this.par_codigo = par_codigo;
    }

    public IntegerFilter getAge_codigo() {
        return age_codigo;
    }

    public void setAge_codigo(IntegerFilter age_codigo) {
        this.age_codigo = age_codigo;
    }

    public StringFilter getCom_cnpj() {
        return com_cnpj;
    }

    public void setCom_cnpj(StringFilter com_cnpj) {
        this.com_cnpj = com_cnpj;
    }

    public StringFilter getCom_beneficiario() {
        return com_beneficiario;
    }

    public void setCom_beneficiario(StringFilter com_beneficiario) {
        this.com_beneficiario = com_beneficiario;
    }

    public StringFilter getCom_documento() {
        return com_documento;
    }

    public void setCom_documento(StringFilter com_documento) {
        this.com_documento = com_documento;
    }

    public ZonedDateTimeFilter getCom_datavencimento() {
        return com_datavencimento;
    }

    public void setCom_datavencimento(ZonedDateTimeFilter com_datavencimento) {
        this.com_datavencimento = com_datavencimento;
    }

    public ZonedDateTimeFilter getCom_datapagamento() {
        return com_datapagamento;
    }

    public void setCom_datapagamento(ZonedDateTimeFilter com_datapagamento) {
        this.com_datapagamento = com_datapagamento;
    }

    public BigDecimalFilter getCom_valordocumento() {
        return com_valordocumento;
    }

    public void setCom_valordocumento(BigDecimalFilter com_valordocumento) {
        this.com_valordocumento = com_valordocumento;
    }

    public BigDecimalFilter getCom_valorpagamento() {
        return com_valorpagamento;
    }

    public void setCom_valorpagamento(BigDecimalFilter com_valorpagamento) {
        this.com_valorpagamento = com_valorpagamento;
    }

    public StringFilter getCom_observacao() {
        return com_observacao;
    }

    public void setCom_observacao(StringFilter com_observacao) {
        this.com_observacao = com_observacao;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ComprovanteCriteria that = (ComprovanteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(par_codigo, that.par_codigo) &&
            Objects.equals(age_codigo, that.age_codigo) &&
            Objects.equals(com_cnpj, that.com_cnpj) &&
            Objects.equals(com_beneficiario, that.com_beneficiario) &&
            Objects.equals(com_documento, that.com_documento) &&
            Objects.equals(com_datavencimento, that.com_datavencimento) &&
            Objects.equals(com_datapagamento, that.com_datapagamento) &&
            Objects.equals(com_valordocumento, that.com_valordocumento) &&
            Objects.equals(com_valorpagamento, that.com_valorpagamento) &&
            Objects.equals(com_observacao, that.com_observacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        par_codigo,
        age_codigo,
        com_cnpj,
        com_beneficiario,
        com_documento,
        com_datavencimento,
        com_datapagamento,
        com_valordocumento,
        com_valorpagamento,
        com_observacao
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComprovanteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (par_codigo != null ? "par_codigo=" + par_codigo + ", " : "") +
                (age_codigo != null ? "age_codigo=" + age_codigo + ", " : "") +
                (com_cnpj != null ? "com_cnpj=" + com_cnpj + ", " : "") +
                (com_beneficiario != null ? "com_beneficiario=" + com_beneficiario + ", " : "") +
                (com_documento != null ? "com_documento=" + com_documento + ", " : "") +
                (com_datavencimento != null ? "com_datavencimento=" + com_datavencimento + ", " : "") +
                (com_datapagamento != null ? "com_datapagamento=" + com_datapagamento + ", " : "") +
                (com_valordocumento != null ? "com_valordocumento=" + com_valordocumento + ", " : "") +
                (com_valorpagamento != null ? "com_valorpagamento=" + com_valorpagamento + ", " : "") +
                (com_observacao != null ? "com_observacao=" + com_observacao + ", " : "") +
            "}";
    }

}
