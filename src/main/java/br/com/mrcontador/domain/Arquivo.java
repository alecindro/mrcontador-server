package br.com.mrcontador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "nome", length = 255)
    @NotNull
    private String nome;

    @Column(name = "nome_original", length = 255)
    private String nomeOriginal;

    @NotNull
    @Column(name = "data_cadastro")
    private ZonedDateTime dataCadastro;

    @Column(name = "tipo_arquivo", length = 100)
    private String tipoArquivo;

    @NotNull
    @Column(name = "tipo_doc", length = 100)
    private String tipoDoc;

    @Column(name = "s3_url", length = 255)
    private String s3Url;

    @Column(name = "s3_dir", length = 255)
    private String s3Dir;

    @Column(name = "tamanho")
    private Long tamanho;

    @Column(name = "etag", length = 255)
    private String etag;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "arquivos", allowSetters = true)
    @JoinColumn(name="parceiro_id", nullable=false)
    private Parceiro parceiro;
    
    @OneToMany(mappedBy = "arquivo", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Notafiscal> notas;

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

    public ZonedDateTime getDataCadastro() {
        return dataCadastro;
    }

    public Arquivo dataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
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
    
    public Set<Notafiscal> getNotas() {
    	if(notas == null) {
    		 notas = new HashSet<>();
    	}
		return notas;
	}

	public void setNotas(Set<Notafiscal> notas) {
		this.notas = notas;
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
            "}";
    }
}
