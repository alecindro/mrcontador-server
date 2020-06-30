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
 * Criteria class for the {@link br.com.mrcontador.domain.Agenciabancaria} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.AgenciabancariaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agenciabancarias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AgenciabancariaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter age_numero;

    private StringFilter age_digito;

    private StringFilter age_agencia;

    private StringFilter age_descricao;

    private BooleanFilter age_situacao;

    private LongFilter bancoId;

    private LongFilter parceiroId;

    public AgenciabancariaCriteria() {
    }

    public AgenciabancariaCriteria(AgenciabancariaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.age_numero = other.age_numero == null ? null : other.age_numero.copy();
        this.age_digito = other.age_digito == null ? null : other.age_digito.copy();
        this.age_agencia = other.age_agencia == null ? null : other.age_agencia.copy();
        this.age_descricao = other.age_descricao == null ? null : other.age_descricao.copy();
        this.age_situacao = other.age_situacao == null ? null : other.age_situacao.copy();
        this.bancoId = other.bancoId == null ? null : other.bancoId.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
    }

    @Override
    public AgenciabancariaCriteria copy() {
        return new AgenciabancariaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAge_numero() {
        return age_numero;
    }

    public void setAge_numero(StringFilter age_numero) {
        this.age_numero = age_numero;
    }

    public StringFilter getAge_digito() {
        return age_digito;
    }

    public void setAge_digito(StringFilter age_digito) {
        this.age_digito = age_digito;
    }

    public StringFilter getAge_agencia() {
        return age_agencia;
    }

    public void setAge_agencia(StringFilter age_agencia) {
        this.age_agencia = age_agencia;
    }

    public StringFilter getAge_descricao() {
        return age_descricao;
    }

    public void setAge_descricao(StringFilter age_descricao) {
        this.age_descricao = age_descricao;
    }

    public BooleanFilter getAge_situacao() {
        return age_situacao;
    }

    public void setAge_situacao(BooleanFilter age_situacao) {
        this.age_situacao = age_situacao;
    }

    public LongFilter getBancoId() {
        return bancoId;
    }

    public void setBancoId(LongFilter bancoId) {
        this.bancoId = bancoId;
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
        final AgenciabancariaCriteria that = (AgenciabancariaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(age_numero, that.age_numero) &&
            Objects.equals(age_digito, that.age_digito) &&
            Objects.equals(age_agencia, that.age_agencia) &&
            Objects.equals(age_descricao, that.age_descricao) &&
            Objects.equals(age_situacao, that.age_situacao) &&
            Objects.equals(bancoId, that.bancoId) &&
            Objects.equals(parceiroId, that.parceiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        age_numero,
        age_digito,
        age_agencia,
        age_descricao,
        age_situacao,
        bancoId,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgenciabancariaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (age_numero != null ? "age_numero=" + age_numero + ", " : "") +
                (age_digito != null ? "age_digito=" + age_digito + ", " : "") +
                (age_agencia != null ? "age_agencia=" + age_agencia + ", " : "") +
                (age_descricao != null ? "age_descricao=" + age_descricao + ", " : "") +
                (age_situacao != null ? "age_situacao=" + age_situacao + ", " : "") +
                (bancoId != null ? "bancoId=" + bancoId + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
