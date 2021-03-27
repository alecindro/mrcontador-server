package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import br.com.mrcontador.domain.TipoAgencia;
import br.com.mrcontador.domain.TipoIntegracao;
import br.com.mrcontador.service.dto.AgenciabancariaCriteria.TipoAgenciaFilter;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.Integracao} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.IntegracaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /integracaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IntegracaoCriteria implements Serializable, Criteria {
	
	 public static class TipoIntegracaoFilter extends Filter<TipoIntegracao> {

	      private static final long serialVersionUID = 1L;

			public TipoIntegracaoFilter() {
	        }

	        public TipoIntegracaoFilter(TipoIntegracaoFilter filter) {
	            super(filter);
	        }

	        @Override
	        public TipoIntegracaoFilter copy() {
	            return new TipoIntegracaoFilter(this);
	        }

	    }


    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter integrador;

    private LocalDateFilter dataInicio;

    private LocalDateFilter dataCadastro;

    private BooleanFilter habilitado;

    private LongFilter parceiroId;
    
    private TipoIntegracaoFilter tipoIntegracao;

    public IntegracaoCriteria() {
    }

    public IntegracaoCriteria(IntegracaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.integrador = other.integrador == null ? null : other.integrador.copy();
        this.tipoIntegracao = other.tipoIntegracao == null ? null : other.tipoIntegracao.copy();
        this.dataInicio = other.dataInicio == null ? null : other.dataInicio.copy();
        this.dataCadastro = other.dataCadastro == null ? null : other.dataCadastro.copy();
        this.habilitado = other.habilitado == null ? null : other.habilitado.copy();
        this.parceiroId = other.parceiroId == null ? null : other.parceiroId.copy();
    }

    @Override
    public IntegracaoCriteria copy() {
        return new IntegracaoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIntegrador() {
        return integrador;
    }

    public void setIntegrador(StringFilter integrador) {
        this.integrador = integrador;
    }

    public TipoIntegracaoFilter getTipoIntegracao() {
        return tipoIntegracao;
    }

    public void setTipoIntegracao(TipoIntegracaoFilter tipoIntegracao) {
        this.tipoIntegracao = tipoIntegracao;
    }

    public LocalDateFilter getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateFilter dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateFilter getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateFilter dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public BooleanFilter getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(BooleanFilter habilitado) {
        this.habilitado = habilitado;
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
        final IntegracaoCriteria that = (IntegracaoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(integrador, that.integrador) &&
            Objects.equals(tipoIntegracao, that.tipoIntegracao) &&
            Objects.equals(dataInicio, that.dataInicio) &&
            Objects.equals(dataCadastro, that.dataCadastro) &&
            Objects.equals(habilitado, that.habilitado) &&
            Objects.equals(parceiroId, that.parceiroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        integrador,
        tipoIntegracao,
        dataInicio,
        dataCadastro,
        habilitado,
        parceiroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntegracaoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (integrador != null ? "integrador=" + integrador + ", " : "") +
                (tipoIntegracao != null ? "tipoIntegracao=" + tipoIntegracao + ", " : "") +
                (dataInicio != null ? "dataInicio=" + dataInicio + ", " : "") +
                (dataCadastro != null ? "dataCadastro=" + dataCadastro + ", " : "") +
                (habilitado != null ? "habilitado=" + habilitado + ", " : "") +
                (parceiroId != null ? "parceiroId=" + parceiroId + ", " : "") +
            "}";
    }

}
