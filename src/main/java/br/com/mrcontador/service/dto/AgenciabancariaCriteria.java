package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import br.com.mrcontador.domain.TipoAgencia;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
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
	
    public static class TipoAgenciaFilter extends Filter<TipoAgencia> {

      private static final long serialVersionUID = 1L;

		public TipoAgenciaFilter() {
        }

        public TipoAgenciaFilter(TipoAgenciaFilter filter) {
            super(filter);
        }

        @Override
        public TipoAgenciaFilter copy() {
            return new TipoAgenciaFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ageNumero;

    private StringFilter ageDigito;

    private StringFilter ageAgencia;

    private StringFilter ageDescricao;

    private BooleanFilter ageSituacao;

    private StringFilter banCodigobancario;

    private LongFilter bancoId;

    private LongFilter parceiroId;

    private LongFilter contaId;
    
    private TipoAgenciaFilter tipoAgencia;

    public AgenciabancariaCriteria() {
    }

    public AgenciabancariaCriteria(AgenciabancariaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ageNumero = other.ageNumero == null ? null : other.ageNumero.copy();
        this.ageDigito = other.ageDigito == null ? null : other.ageDigito.copy();
        this.ageAgencia = other.ageAgencia == null ? null : other.ageAgencia.copy();
        this.ageDescricao = other.ageDescricao == null ? null : other.ageDescricao.copy();
        this.ageSituacao = other.ageSituacao == null ? null : other.ageSituacao.copy();
        this.banCodigobancario = other.banCodigobancario == null ? null : other.banCodigobancario.copy();
        this.bancoId = other.bancoId == null ? null : other.bancoId.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
        this.contaId = other.contaId == null ? null : other.contaId.copy();
        this.tipoAgencia = other.tipoAgencia == null ? null : other.tipoAgencia.copy();
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

    public StringFilter getAgeNumero() {
        return ageNumero;
    }

    public void setAgeNumero(StringFilter ageNumero) {
        this.ageNumero = ageNumero;
    }

    public StringFilter getAgeDigito() {
        return ageDigito;
    }

    public void setAgeDigito(StringFilter ageDigito) {
        this.ageDigito = ageDigito;
    }

    public StringFilter getAgeAgencia() {
        return ageAgencia;
    }

    public void setAgeAgencia(StringFilter ageAgencia) {
        this.ageAgencia = ageAgencia;
    }

    public StringFilter getAgeDescricao() {
        return ageDescricao;
    }

    public void setAgeDescricao(StringFilter ageDescricao) {
        this.ageDescricao = ageDescricao;
    }

    public BooleanFilter getAgeSituacao() {
        return ageSituacao;
    }

    public void setAgeSituacao(BooleanFilter ageSituacao) {
        this.ageSituacao = ageSituacao;
    }

    public StringFilter getBanCodigobancario() {
        return banCodigobancario;
    }

    public void setBanCodigobancario(StringFilter banCodigobancario) {
        this.banCodigobancario = banCodigobancario;
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

    public LongFilter getContaId() {
		return contaId;
	}

	public void setContaId(LongFilter contaId) {
		this.contaId = contaId;
	}
	
	public TipoAgenciaFilter getTipoAgencia() {
		return tipoAgencia;
	}

	public void setTipoAgencia(TipoAgenciaFilter tipoAgencia) {
		this.tipoAgencia = tipoAgencia;
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
            Objects.equals(ageNumero, that.ageNumero) &&
            Objects.equals(ageDigito, that.ageDigito) &&
            Objects.equals(ageAgencia, that.ageAgencia) &&
            Objects.equals(ageDescricao, that.ageDescricao) &&
            Objects.equals(ageSituacao, that.ageSituacao) &&
            Objects.equals(banCodigobancario, that.banCodigobancario) &&
            Objects.equals(bancoId, that.bancoId) &&
            Objects.equals(parceiroId, that.parceiroId) &&
            Objects.equals(contaId, that.contaId) &&
            Objects.equals(tipoAgencia, that.tipoAgencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ageNumero,
        ageDigito,
        ageAgencia,
        ageDescricao,
        ageSituacao,
        banCodigobancario,
        bancoId,
        parceiroId,
        contaId,
        tipoAgencia
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgenciabancariaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ageNumero != null ? "age_numero=" + ageNumero + ", " : "") +
                (ageDigito != null ? "age_digito=" + ageDigito + ", " : "") +
                (ageAgencia != null ? "age_agencia=" + ageAgencia + ", " : "") +
                (ageDescricao != null ? "age_descricao=" + ageDescricao + ", " : "") +
                (ageSituacao != null ? "age_situacao=" + ageSituacao + ", " : "") +
                (banCodigobancario != null ? "ban_codigobancario=" + banCodigobancario + ", " : "") +
                (bancoId != null ? "bancoId=" + bancoId + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
                (contaId != null ? "contaId=" + contaId + ", " : "") +
                (tipoAgencia != null ? "tipoAgencia=" + tipoAgencia + ", " : "") +
            "}";
    }

}
