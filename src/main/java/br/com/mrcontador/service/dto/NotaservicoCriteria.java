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
 * Criteria class for the {@link br.com.mrcontador.domain.Notaservico} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.NotaservicoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notaservicos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotaservicoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter nse_numero;

    private StringFilter nse_descricao;

    private StringFilter nse_cnpj;

    private StringFilter nse_empresa;

    private ZonedDateTimeFilter nse_datasaida;

    private BigDecimalFilter nse_valornota;

    private ZonedDateTimeFilter nse_dataparcela;

    private BigDecimalFilter nse_valorparcela;

    private IntegerFilter tno_codigo;

    private StringFilter nse_parcela;

    private LongFilter parceiroId;

    public NotaservicoCriteria() {
    }

    public NotaservicoCriteria(NotaservicoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nse_numero = other.nse_numero == null ? null : other.nse_numero.copy();
        this.nse_descricao = other.nse_descricao == null ? null : other.nse_descricao.copy();
        this.nse_cnpj = other.nse_cnpj == null ? null : other.nse_cnpj.copy();
        this.nse_empresa = other.nse_empresa == null ? null : other.nse_empresa.copy();
        this.nse_datasaida = other.nse_datasaida == null ? null : other.nse_datasaida.copy();
        this.nse_valornota = other.nse_valornota == null ? null : other.nse_valornota.copy();
        this.nse_dataparcela = other.nse_dataparcela == null ? null : other.nse_dataparcela.copy();
        this.nse_valorparcela = other.nse_valorparcela == null ? null : other.nse_valorparcela.copy();
        this.tno_codigo = other.tno_codigo == null ? null : other.tno_codigo.copy();
        this.nse_parcela = other.nse_parcela == null ? null : other.nse_parcela.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
    }

    @Override
    public NotaservicoCriteria copy() {
        return new NotaservicoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNse_numero() {
        return nse_numero;
    }

    public void setNse_numero(IntegerFilter nse_numero) {
        this.nse_numero = nse_numero;
    }

    public StringFilter getNse_descricao() {
        return nse_descricao;
    }

    public void setNse_descricao(StringFilter nse_descricao) {
        this.nse_descricao = nse_descricao;
    }

    public StringFilter getNse_cnpj() {
        return nse_cnpj;
    }

    public void setNse_cnpj(StringFilter nse_cnpj) {
        this.nse_cnpj = nse_cnpj;
    }

    public StringFilter getNse_empresa() {
        return nse_empresa;
    }

    public void setNse_empresa(StringFilter nse_empresa) {
        this.nse_empresa = nse_empresa;
    }

    public ZonedDateTimeFilter getNse_datasaida() {
        return nse_datasaida;
    }

    public void setNse_datasaida(ZonedDateTimeFilter nse_datasaida) {
        this.nse_datasaida = nse_datasaida;
    }

    public BigDecimalFilter getNse_valornota() {
        return nse_valornota;
    }

    public void setNse_valornota(BigDecimalFilter nse_valornota) {
        this.nse_valornota = nse_valornota;
    }

    public ZonedDateTimeFilter getNse_dataparcela() {
        return nse_dataparcela;
    }

    public void setNse_dataparcela(ZonedDateTimeFilter nse_dataparcela) {
        this.nse_dataparcela = nse_dataparcela;
    }

    public BigDecimalFilter getNse_valorparcela() {
        return nse_valorparcela;
    }

    public void setNse_valorparcela(BigDecimalFilter nse_valorparcela) {
        this.nse_valorparcela = nse_valorparcela;
    }

    public IntegerFilter getTno_codigo() {
        return tno_codigo;
    }

    public void setTno_codigo(IntegerFilter tno_codigo) {
        this.tno_codigo = tno_codigo;
    }

    public StringFilter getNse_parcela() {
        return nse_parcela;
    }

    public void setNse_parcela(StringFilter nse_parcela) {
        this.nse_parcela = nse_parcela;
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
        final NotaservicoCriteria that = (NotaservicoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nse_numero, that.nse_numero) &&
            Objects.equals(nse_descricao, that.nse_descricao) &&
            Objects.equals(nse_cnpj, that.nse_cnpj) &&
            Objects.equals(nse_empresa, that.nse_empresa) &&
            Objects.equals(nse_datasaida, that.nse_datasaida) &&
            Objects.equals(nse_valornota, that.nse_valornota) &&
            Objects.equals(nse_dataparcela, that.nse_dataparcela) &&
            Objects.equals(nse_valorparcela, that.nse_valorparcela) &&
            Objects.equals(tno_codigo, that.tno_codigo) &&
            Objects.equals(nse_parcela, that.nse_parcela) &&
            Objects.equals(parceiroId, that.parceiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nse_numero,
        nse_descricao,
        nse_cnpj,
        nse_empresa,
        nse_datasaida,
        nse_valornota,
        nse_dataparcela,
        nse_valorparcela,
        tno_codigo,
        nse_parcela,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotaservicoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nse_numero != null ? "nse_numero=" + nse_numero + ", " : "") +
                (nse_descricao != null ? "nse_descricao=" + nse_descricao + ", " : "") +
                (nse_cnpj != null ? "nse_cnpj=" + nse_cnpj + ", " : "") +
                (nse_empresa != null ? "nse_empresa=" + nse_empresa + ", " : "") +
                (nse_datasaida != null ? "nse_datasaida=" + nse_datasaida + ", " : "") +
                (nse_valornota != null ? "nse_valornota=" + nse_valornota + ", " : "") +
                (nse_dataparcela != null ? "nse_dataparcela=" + nse_dataparcela + ", " : "") +
                (nse_valorparcela != null ? "nse_valorparcela=" + nse_valorparcela + ", " : "") +
                (tno_codigo != null ? "tno_codigo=" + tno_codigo + ", " : "") +
                (nse_parcela != null ? "nse_parcela=" + nse_parcela + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
