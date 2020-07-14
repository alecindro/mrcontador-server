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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.mrcontador.domain.ArquivoErro} entity. This class is used
 * in {@link br.com.mrcontador.web.rest.ArquivoErroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /arquivo-erros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArquivoErroCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter nomeOriginal;

    private StringFilter tipoArquivo;

    private StringFilter s3Url;

    private StringFilter s3Dir;

    private ZonedDateTimeFilter dataCadastro;

    private StringFilter usuario;

    private StringFilter contador;

    private LongFilter tamanho;

    public ArquivoErroCriteria() {
    }

    public ArquivoErroCriteria(ArquivoErroCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.nomeOriginal = other.nomeOriginal == null ? null : other.nomeOriginal.copy();
        this.tipoArquivo = other.tipoArquivo == null ? null : other.tipoArquivo.copy();
        this.s3Url = other.s3Url == null ? null : other.s3Url.copy();
        this.s3Dir = other.s3Dir == null ? null : other.s3Dir.copy();
        this.dataCadastro = other.dataCadastro == null ? null : other.dataCadastro.copy();
        this.usuario = other.usuario == null ? null : other.usuario.copy();
        this.contador = other.contador == null ? null : other.contador.copy();
        this.tamanho = other.tamanho == null ? null : other.tamanho.copy();
    }

    @Override
    public ArquivoErroCriteria copy() {
        return new ArquivoErroCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getNomeOriginal() {
        return nomeOriginal;
    }

    public void setNomeOriginal(StringFilter nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
    }

    public StringFilter getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(StringFilter tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public StringFilter gets3Url() {
        return s3Url;
    }

    public void sets3Url(StringFilter s3Url) {
        this.s3Url = s3Url;
    }

    public StringFilter gets3Dir() {
        return s3Dir;
    }

    public void sets3Dir(StringFilter s3Dir) {
        this.s3Dir = s3Dir;
    }

    public ZonedDateTimeFilter getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(ZonedDateTimeFilter dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public StringFilter getUsuario() {
        return usuario;
    }

    public void setUsuario(StringFilter usuario) {
        this.usuario = usuario;
    }

    public StringFilter getContador() {
        return contador;
    }

    public void setContador(StringFilter contador) {
        this.contador = contador;
    }

    public LongFilter getTamanho() {
        return tamanho;
    }

    public void setTamanho(LongFilter tamanho) {
        this.tamanho = tamanho;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArquivoErroCriteria that = (ArquivoErroCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(nomeOriginal, that.nomeOriginal) &&
            Objects.equals(tipoArquivo, that.tipoArquivo) &&
            Objects.equals(s3Url, that.s3Url) &&
            Objects.equals(s3Dir, that.s3Dir) &&
            Objects.equals(dataCadastro, that.dataCadastro) &&
            Objects.equals(usuario, that.usuario) &&
            Objects.equals(contador, that.contador) &&
            Objects.equals(tamanho, that.tamanho);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        nomeOriginal,
        tipoArquivo,
        s3Url,
        s3Dir,
        dataCadastro,
        usuario,
        contador,
        tamanho
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArquivoErroCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (nomeOriginal != null ? "nomeOriginal=" + nomeOriginal + ", " : "") +
                (tipoArquivo != null ? "tipoArquivo=" + tipoArquivo + ", " : "") +
                (s3Url != null ? "s3Url=" + s3Url + ", " : "") +
                (s3Dir != null ? "s3Dir=" + s3Dir + ", " : "") +
                (dataCadastro != null ? "dataCadastro=" + dataCadastro + ", " : "") +
                (usuario != null ? "usuario=" + usuario + ", " : "") +
                (contador != null ? "contador=" + contador + ", " : "") +
                (tamanho != null ? "tamanho=" + tamanho + ", " : "") +
            "}";
    }

}
