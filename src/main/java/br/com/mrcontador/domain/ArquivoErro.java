package br.com.mrcontador.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

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

    @Column(name = "contador")
    private String contador;

    @Column(name = "tamanho")
    private Long tamanho;

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

    public String gets3Url() {
        return s3Url;
    }

    public ArquivoErro s3Url(String s3Url) {
        this.s3Url = s3Url;
        return this;
    }

    public void sets3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String gets3Dir() {
        return s3Dir;
    }

    public ArquivoErro s3Dir(String s3Dir) {
        this.s3Dir = s3Dir;
        return this;
    }

    public void sets3Dir(String s3Dir) {
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

    public String getContador() {
        return contador;
    }

    public ArquivoErro contador(String contador) {
        this.contador = contador;
        return this;
    }

    public void setContador(String contador) {
        this.contador = contador;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public ArquivoErro tamanho(Long tamanho) {
        this.tamanho = tamanho;
        return this;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
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
            ", s3Url='" + gets3Url() + "'" +
            ", s3Dir='" + gets3Dir() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", usuario='" + getUsuario() + "'" +
            ", contador='" + getContador() + "'" +
            ", tamanho=" + getTamanho() +
            "}";
    }
}
