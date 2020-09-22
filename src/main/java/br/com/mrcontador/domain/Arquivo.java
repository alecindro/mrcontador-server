package br.com.mrcontador.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Arquivo.
 */
@Entity
@Table(name = "arquivo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "nome_original")
    private String nomeOriginal;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "tipo_arquivo")
    private String tipoArquivo;

    @Column(name = "tipo_doc")
    private String tipoDoc;

    @Column(name = "s3_url")
    private String s3Url;

    @Column(name = "s3_dir")
    private String s3Dir;

    @Column(name = "tamanho")
    private Long tamanho;

    @Column(name = "etag")
    private String etag;

    @Column(name = "usuario")
    private String usuario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "arquivos", allowSetters = true)
    private Parceiro parceiro;

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

    public Arquivo nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public Arquivo nomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
        return this;
    }

    public void setNomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Arquivo dataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public Arquivo tipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
        return this;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public Arquivo tipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
        return this;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String gets3Url() {
        return s3Url;
    }

    public Arquivo s3Url(String s3Url) {
        this.s3Url = s3Url;
        return this;
    }

    public void sets3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String gets3Dir() {
        return s3Dir;
    }

    public Arquivo s3Dir(String s3Dir) {
        this.s3Dir = s3Dir;
        return this;
    }

    public void sets3Dir(String s3Dir) {
        this.s3Dir = s3Dir;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public Arquivo tamanho(Long tamanho) {
        this.tamanho = tamanho;
        return this;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public String getEtag() {
        return etag;
    }

    public Arquivo etag(String etag) {
        this.etag = etag;
        return this;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getUsuario() {
        return usuario;
    }

    public Arquivo usuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public Arquivo parceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
        return this;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Arquivo)) {
            return false;
        }
        return id != null && id.equals(((Arquivo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Arquivo{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", nomeOriginal='" + getNomeOriginal() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", tipoArquivo='" + getTipoArquivo() + "'" +
            ", tipoDoc='" + getTipoDoc() + "'" +
            ", s3Url='" + gets3Url() + "'" +
            ", s3Dir='" + gets3Dir() + "'" +
            ", tamanho=" + getTamanho() +
            ", etag='" + getEtag() + "'" +
            ", usuario='" + getUsuario() + "'" +
            "}";
    }
}
