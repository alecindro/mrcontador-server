package br.com.mrcontador.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.mrcontador.file.TipoDocumento;

/**
 * A ArquivoErro.
 */
@Entity
@Table(name = "arquivo_erro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArquivoErro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "nome_original")
    private String nomeOriginal;

    @Column(name = "tipo_arquivo")
    private String tipoArquivo;

    @Column(name = "s3_url")
    private String s3Url;

    @Column(name = "s3_dir")
    private String s3Dir;

    @Column(name = "data_cadastro")
    private ZonedDateTime dataCadastro;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "schema")
    private String schema;

    @Column(name = "tamanho")
    private Long tamanho;
    
    @Column(name = "tipo_documento")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;
    
    @Column(name= "parceiro_id")
    private Long parceiroId;
    
    @Column(name = "parceiro")
    private String parceiroName;
    
    @Column(name = "processado")
    private Boolean processado;
    
    @Column(name = "valido")
    private Boolean valido;
    
    @Column(name = "id_agencia")
    private Long idAgencia;
    
    @Column(name = "content_type")
    private String contentType;
    
    
    @ManyToOne
    @JoinColumn(name="contador_id")
    private Contador contador;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public ArquivoErro nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public ArquivoErro nomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
        return this;
    }

    public void setNomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public ArquivoErro tipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
        return this;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public String getS3Url() {
        return s3Url;
    }

    public ArquivoErro s3Url(String s3Url) {
        this.s3Url = s3Url;
        return this;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getS3Dir() {
        return s3Dir;
    }

    public ArquivoErro s3Dir(String s3Dir) {
        this.s3Dir = s3Dir;
        return this;
    }

    public void setS3Dir(String s3Dir) {
        this.s3Dir = s3Dir;
    }

    public ZonedDateTime getDataCadastro() {
        return dataCadastro;
    }

    public ArquivoErro dataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getUsuario() {
        return usuario;
    }

    public ArquivoErro usuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Contador getContador() {
        return contador;
    }

    public ArquivoErro contador(Contador contador) {
        this.contador = contador;
        return this;
    }

    public void setContador(Contador contador) {
        this.contador = contador;
    }

    public Long getTamanho() {
        return tamanho;
    }
    
    public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public ArquivoErro tamanho(Long tamanho) {
        this.tamanho = tamanho;
        return this;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }
    
    public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public ArquivoErro tipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
		return this;
	}

	public Long getParceiroId() {
		return parceiroId;
	}

	public void setParceiroId(Long parceiroId) {
		this.parceiroId = parceiroId;
	}
	
	public ArquivoErro parceiroId(Long parceiroId) {
		this.parceiroId = parceiroId;
		return this;
	}

	public String getParceiroName() {
		return parceiroName;
	}

	public void setParceiroName(String parceiroName) {
		this.parceiroName = parceiroName;
	}
	
	public ArquivoErro parceiroName(String parceiroName) {
		this.parceiroName = parceiroName;
		return this;
	}
	
	public Boolean getProcessado() {
		return processado;
	}

	public void setProcessado(Boolean processado) {
		this.processado = processado;
	}
	
	public ArquivoErro processado(Boolean processado) {
		this.processado = processado;
		return this;
	}
	
	public Boolean getValido() {
		return valido;
	}

	public void setValido(Boolean valido) {
		this.valido = valido;
	}
	
	public ArquivoErro valido(Boolean valido) {
		this.valido = valido;
		return this;
	}

	public Long getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(Long idAgencia) {
		this.idAgencia = idAgencia;
	}
	public ArquivoErro idAgencia(Long idAgencia) {
		this.idAgencia = idAgencia;
		return this;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArquivoErro)) {
            return false;
        }
        return id != null && id.equals(((ArquivoErro) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArquivoErro{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", nomeOriginal='" + getNomeOriginal() + "'" +
            ", tipoArquivo='" + getTipoArquivo() + "'" +
            ", s3Url='" + getS3Url() + "'" +
            ", s3Dir='" + getS3Dir() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", usuario='" + getUsuario() + "'" +
            ", schema='" + getSchema() + "'" +
            ", tamanho=" + getTamanho() +
            "}";
    }
}
