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
@Table(name = "exportacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exportacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "usuario")
    private String usuario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "exportacao", allowSetters = true)
    private Parceiro parceiro;
    
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "exportacao", allowSetters = true)
    private Agenciabancaria agenciabancaria;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Exportacao dataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getUsuario() {
        return usuario;
    }

    public Exportacao usuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public Exportacao parceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
        return this;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }
    
    public Exportacao agenciabancaria(Agenciabancaria agenciabancaria) {
		this.agenciabancaria = agenciabancaria;
        return this;
    }
    
    public Agenciabancaria getAgenciabancaria() {
		return agenciabancaria;
	}

	public void setAgenciabancaria(Agenciabancaria agenciabancaria) {
		this.agenciabancaria = agenciabancaria;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
    public Exportacao periodo(String periodo) {
        this.periodo = periodo;
        return this;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exportacao)) {
            return false;
        }
        return id != null && id.equals(((Exportacao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "Exportacao [id=" + id + ", periodo=" + periodo + ", dataCadastro=" + dataCadastro + ", usuario="
				+ usuario + ", parceiro=" + parceiro + "]";
	}
    
    

}
