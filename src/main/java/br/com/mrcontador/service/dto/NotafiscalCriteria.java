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
 * Criteria class for the {@link br.com.mrcontador.domain.Notafiscal} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.NotafiscalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notafiscals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotafiscalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter not_numero;

    private StringFilter not_descricao;

    private StringFilter not_cnpj;

    private StringFilter not_empresa;

    private ZonedDateTimeFilter not_datasaida;

    private BigDecimalFilter not_valornota;

    private ZonedDateTimeFilter not_dataparcela;

    private BigDecimalFilter not_valorparcela;

    private IntegerFilter tno_codigo;

    private StringFilter not_parcela;

    private LongFilter parceiroId;

    public NotafiscalCriteria() {
    }

    public NotafiscalCriteria(NotafiscalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.not_numero = other.not_numero == null ? null : other.not_numero.copy();
        this.not_descricao = other.not_descricao == null ? null : other.not_descricao.copy();
        this.not_cnpj = other.not_cnpj == null ? null : other.not_cnpj.copy();
        this.not_empresa = other.not_empresa == null ? null : other.not_empresa.copy();
        this.not_datasaida = other.not_datasaida == null ? null : other.not_datasaida.copy();
        this.not_valornota = other.not_valornota == null ? null : other.not_valornota.copy();
        this.not_dataparcela = other.not_dataparcela == null ? null : other.not_dataparcela.copy();
        this.not_valorparcela = other.not_valorparcela == null ? null : other.not_valorparcela.copy();
        this.tno_codigo = other.tno_codigo == null ? null : other.tno_codigo.copy();
        this.not_parcela = other.not_parcela == null ? null : other.not_parcela.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
    }

    @Override
    public NotafiscalCriteria copy() {
        return new NotafiscalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNot_numero() {
        return not_numero;
    }

    public void setNot_numero(IntegerFilter not_numero) {
        this.not_numero = not_numero;
    }

    public StringFilter getNot_descricao() {
        return not_descricao;
    }

    public void setNot_descricao(StringFilter not_descricao) {
        this.not_descricao = not_descricao;
    }

    public StringFilter getNot_cnpj() {
        return not_cnpj;
    }

    public void setNot_cnpj(StringFilter not_cnpj) {
        this.not_cnpj = not_cnpj;
    }

    public StringFilter getNot_empresa() {
        return not_empresa;
    }

    public void setNot_empresa(StringFilter not_empresa) {
        this.not_empresa = not_empresa;
    }

    public ZonedDateTimeFilter getNot_datasaida() {
        return not_datasaida;
    }

    public void setNot_datasaida(ZonedDateTimeFilter not_datasaida) {
        this.not_datasaida = not_datasaida;
    }

    public BigDecimalFilter getNot_valornota() {
        return not_valornota;
    }

    public void setNot_valornota(BigDecimalFilter not_valornota) {
        this.not_valornota = not_valornota;
    }

    public ZonedDateTimeFilter getNot_dataparcela() {
        return not_dataparcela;
    }

    public void setNot_dataparcela(ZonedDateTimeFilter not_dataparcela) {
        this.not_dataparcela = not_dataparcela;
    }

    public BigDecimalFilter getNot_valorparcela() {
        return not_valorparcela;
    }

    public void setNot_valorparcela(BigDecimalFilter not_valorparcela) {
        this.not_valorparcela = not_valorparcela;
    }

    public IntegerFilter getTno_codigo() {
        return tno_codigo;
    }

    public void setTno_codigo(IntegerFilter tno_codigo) {
        this.tno_codigo = tno_codigo;
    }

    public StringFilter getNot_parcela() {
        return not_parcela;
    }

    public void setNot_parcela(StringFilter not_parcela) {
        this.not_parcela = not_parcela;
    }

    public LongFilter getParceiroId() {
        return parceiroId;
    }

    public void setParceiroId(LongFilter parceiroId) {
        this.parceiroId = parceiroId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotafiscalCriteria that = (NotafiscalCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(not_numero, that.not_numero) &&
            Objects.equals(not_descricao, that.not_descricao) &&
            Objects.equals(not_cnpj, that.not_cnpj) &&
            Objects.equals(not_empresa, that.not_empresa) &&
            Objects.equals(not_datasaida, that.not_datasaida) &&
            Objects.equals(not_valornota, that.not_valornota) &&
            Objects.equals(not_dataparcela, that.not_dataparcela) &&
            Objects.equals(not_valorparcela, that.not_valorparcela) &&
            Objects.equals(tno_codigo, that.tno_codigo) &&
            Objects.equals(not_parcela, that.not_parcela) &&
            Objects.equals(parceiroId, that.parceiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        not_numero,
        not_descricao,
        not_cnpj,
        not_empresa,
        not_datasaida,
        not_valornota,
        not_dataparcela,
        not_valorparcela,
        tno_codigo,
        not_parcela,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotafiscalCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (not_numero != null ? "not_numero=" + not_numero + ", " : "") +
                (not_descricao != null ? "not_descricao=" + not_descricao + ", " : "") +
                (not_cnpj != null ? "not_cnpj=" + not_cnpj + ", " : "") +
                (not_empresa != null ? "not_empresa=" + not_empresa + ", " : "") +
                (not_datasaida != null ? "not_datasaida=" + not_datasaida + ", " : "") +
                (not_valornota != null ? "not_valornota=" + not_valornota + ", " : "") +
                (not_dataparcela != null ? "not_dataparcela=" + not_dataparcela + ", " : "") +
                (not_valorparcela != null ? "not_valorparcela=" + not_valorparcela + ", " : "") +
                (tno_codigo != null ? "tno_codigo=" + tno_codigo + ", " : "") +
                (not_parcela != null ? "not_parcela=" + not_parcela + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
