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
 * Criteria class for the {@link br.com.mrcontador.domain.Extrato} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.ExtratoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /extratoes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtratoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter ext_datalancamento;

    private StringFilter ext_historico;

    private StringFilter ext_numerodocumento;

    private StringFilter ext_numerocontrole;

    private BigDecimalFilter ext_debito;

    private BigDecimalFilter ext_credito;

    private StringFilter ext_descricao;

    private LongFilter parceiroId;

    private LongFilter agenciabancariaId;

    public ExtratoCriteria() {
    }

    public ExtratoCriteria(ExtratoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ext_datalancamento = other.ext_datalancamento == null ? null : other.ext_datalancamento.copy();
        this.ext_historico = other.ext_historico == null ? null : other.ext_historico.copy();
        this.ext_numerodocumento = other.ext_numerodocumento == null ? null : other.ext_numerodocumento.copy();
        this.ext_numerocontrole = other.ext_numerocontrole == null ? null : other.ext_numerocontrole.copy();
        this.ext_debito = other.ext_debito == null ? null : other.ext_debito.copy();
        this.ext_credito = other.ext_credito == null ? null : other.ext_credito.copy();
        this.ext_descricao = other.ext_descricao == null ? null : other.ext_descricao.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
        this.agenciabancariaId = other.agenciabancariaId == null ? null : other.agenciabancariaId.copy();
    }

    @Override
    public ExtratoCriteria copy() {
        return new ExtratoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getExt_datalancamento() {
        return ext_datalancamento;
    }

    public void setExt_datalancamento(ZonedDateTimeFilter ext_datalancamento) {
        this.ext_datalancamento = ext_datalancamento;
    }

    public StringFilter getExt_historico() {
        return ext_historico;
    }

    public void setExt_historico(StringFilter ext_historico) {
        this.ext_historico = ext_historico;
    }

    public StringFilter getExt_numerodocumento() {
        return ext_numerodocumento;
    }

    public void setExt_numerodocumento(StringFilter ext_numerodocumento) {
        this.ext_numerodocumento = ext_numerodocumento;
    }

    public StringFilter getExt_numerocontrole() {
        return ext_numerocontrole;
    }

    public void setExt_numerocontrole(StringFilter ext_numerocontrole) {
        this.ext_numerocontrole = ext_numerocontrole;
    }

    public BigDecimalFilter getExt_debito() {
        return ext_debito;
    }

    public void setExt_debito(BigDecimalFilter ext_debito) {
        this.ext_debito = ext_debito;
    }

    public BigDecimalFilter getExt_credito() {
        return ext_credito;
    }

    public void setExt_credito(BigDecimalFilter ext_credito) {
        this.ext_credito = ext_credito;
    }

    public StringFilter getExt_descricao() {
        return ext_descricao;
    }

    public void setExt_descricao(StringFilter ext_descricao) {
        this.ext_descricao = ext_descricao;
    }

    public LongFilter getParceiroId() {
        return parceiroId;
    }

    public void setParceiroId(LongFilter parceiroId) {
        this.parceiroId = parceiroId;
    }

    public LongFilter getAgenciabancariaId() {
        return agenciabancariaId;
    }

    public void setAgenciabancariaId(LongFilter agenciabancariaId) {
        this.agenciabancariaId = agenciabancariaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExtratoCriteria that = (ExtratoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ext_datalancamento, that.ext_datalancamento) &&
            Objects.equals(ext_historico, that.ext_historico) &&
            Objects.equals(ext_numerodocumento, that.ext_numerodocumento) &&
            Objects.equals(ext_numerocontrole, that.ext_numerocontrole) &&
            Objects.equals(ext_debito, that.ext_debito) &&
            Objects.equals(ext_credito, that.ext_credito) &&
            Objects.equals(ext_descricao, that.ext_descricao) &&
            Objects.equals(parceiroId, that.parceiroId) &&
            Objects.equals(agenciabancariaId, that.agenciabancariaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ext_datalancamento,
        ext_historico,
        ext_numerodocumento,
        ext_numerocontrole,
        ext_debito,
        ext_credito,
        ext_descricao,
        parceiroId,
        agenciabancariaId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtratoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ext_datalancamento != null ? "ext_datalancamento=" + ext_datalancamento + ", " : "") +
                (ext_historico != null ? "ext_historico=" + ext_historico + ", " : "") +
                (ext_numerodocumento != null ? "ext_numerodocumento=" + ext_numerodocumento + ", " : "") +
                (ext_numerocontrole != null ? "ext_numerocontrole=" + ext_numerocontrole + ", " : "") +
                (ext_debito != null ? "ext_debito=" + ext_debito + ", " : "") +
                (ext_credito != null ? "ext_credito=" + ext_credito + ", " : "") +
                (ext_descricao != null ? "ext_descricao=" + ext_descricao + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
                (agenciabancariaId != null ? "agenciabancariaId=" + agenciabancariaId + ", " : "") +
            "}";
    }

}
