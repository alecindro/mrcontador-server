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

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Banco} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.BancoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bancos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BancoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ban_descricao;

    private StringFilter ban_sigla;

    private IntegerFilter ban_ispb;

    private StringFilter ban_codigobancario;

    public BancoCriteria() {
    }

    public BancoCriteria(BancoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ban_descricao = other.ban_descricao == null ? null : other.ban_descricao.copy();
        this.ban_sigla = other.ban_sigla == null ? null : other.ban_sigla.copy();
        this.ban_ispb = other.ban_ispb == null ? null : other.ban_ispb.copy();
        this.ban_codigobancario = other.ban_codigobancario == null ? null : other.ban_codigobancario.copy();
    }

    @Override
    public BancoCriteria copy() {
        return new BancoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBan_descricao() {
        return ban_descricao;
    }

    public void setBan_descricao(StringFilter ban_descricao) {
        this.ban_descricao = ban_descricao;
    }

    public StringFilter getBan_sigla() {
        return ban_sigla;
    }

    public void setBan_sigla(StringFilter ban_sigla) {
        this.ban_sigla = ban_sigla;
    }

    public IntegerFilter getBan_ispb() {
        return ban_ispb;
    }

    public void setBan_ispb(IntegerFilter ban_ispb) {
        this.ban_ispb = ban_ispb;
    }

    public StringFilter getBan_codigobancario() {
        return ban_codigobancario;
    }

    public void setBan_codigobancario(StringFilter ban_codigobancario) {
        this.ban_codigobancario = ban_codigobancario;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BancoCriteria that = (BancoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ban_descricao, that.ban_descricao) &&
            Objects.equals(ban_sigla, that.ban_sigla) &&
            Objects.equals(ban_ispb, that.ban_ispb) &&
            Objects.equals(ban_codigobancario, that.ban_codigobancario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ban_descricao,
        ban_sigla,
        ban_ispb,
        ban_codigobancario
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BancoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ban_descricao != null ? "ban_descricao=" + ban_descricao + ", " : "") +
                (ban_sigla != null ? "ban_sigla=" + ban_sigla + ", " : "") +
                (ban_ispb != null ? "ban_ispb=" + ban_ispb + ", " : "") +
                (ban_codigobancario != null ? "ban_codigobancario=" + ban_codigobancario + ", " : "") +
            "}";
    }

}
