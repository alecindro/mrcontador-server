package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import br.com.mrcontador.file.TipoDocumento;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
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
    
    public static class TipoDocumentoFilter extends Filter<TipoDocumento> {

        private static final long serialVersionUID = 1L;

  		public TipoDocumentoFilter() {
          }

          public TipoDocumentoFilter(TipoDocumentoFilter filter) {
              super(filter);
          }

          @Override
          public TipoDocumentoFilter copy() {
              return new TipoDocumentoFilter(this);
          }

      }

    private LongFilter id;

    private StringFilter nome;

    private StringFilter nomeOriginal;

    private StringFilter tipoArquivo;

    private StringFilter s3Url;

    private StringFilter s3Dir;

    private ZonedDateTimeFilter dataCadastro;

    private StringFilter usuario;

    private StringFilter schema;

    private LongFilter tamanho;
    
    private BooleanFilter processado;
    
    private BooleanFilter valido;
    
    private LongFilter contadorId;
    
    private TipoDocumentoFilter tipoDocumento;

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
        this.schema = other.schema == null ? null : other.schema.copy();
        this.tamanho = other.tamanho == null ? null : other.tamanho.copy();
        this.processado = other.processado == null ? null : other.processado.copy();
        this.valido = other.valido == null ? null : other.valido.copy();
        this.contadorId = other.contadorId == null ? null : other.contadorId.copy();
        this.tipoDocumento = other.tipoDocumento == null ? null : other.tipoDocumento.copy();
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

    public StringFilter getSchema() {
        return schema;
    }

    public void setSchema(StringFilter schema) {
        this.schema = schema;
    }

    public LongFilter getTamanho() {
        return tamanho;
    }

    public void setTamanho(LongFilter tamanho) {
        this.tamanho = tamanho;
    }
    
    public BooleanFilter getProcessado() {
		return processado;
	}

	public void setProcessado(BooleanFilter processado) {
		this.processado = processado;
	}

	public LongFilter getContadorId() {
		return contadorId;
	}

	public void setContadorId(LongFilter contadorId) {
		this.contadorId = contadorId;
	}

	public TipoDocumentoFilter getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoFilter tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public BooleanFilter getValido() {
		return valido;
	}

	public void setValido(BooleanFilter valido) {
		this.valido = valido;
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
            Objects.equals(schema, that.schema) &&
            Objects.equals(tamanho, that.tamanho) &&
            Objects.equals(processado, that.processado) &&
            Objects.equals(valido, that.valido) &&
            Objects.equals(contadorId, that.contadorId) &&
            Objects.equals(tipoDocumento, that.tipoDocumento);
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
        schema,
        tamanho,
        processado,
        contadorId,
        tipoDocumento,
        valido
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
                (schema != null ? "schema=" + schema + ", " : "") +
                (tamanho != null ? "tamanho=" + tamanho + ", " : "") +
            "}";
    }

}
