package br.com.mrcontador.client.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"atividade_principal",
"data_situacao",
"nome",
"uf",
"qsa",
"situacao",
"bairro",
"logradouro",
"numero",
"cep",
"municipio",
"porte",
"abertura",
"natureza_juridica",
"fantasia",
"cnpj",
"ultima_atualizacao",
"status",
"tipo",
"complemento",
"email",
"telefone",
"efr",
"motivo_situacao",
"situacao_especial",
"data_situacao_especial",
"atividades_secundarias",
"capital_social",
"extra",
"billing"
})
public class PessoaJuridica {

	@JsonProperty("atividade_principal")
	private List<AtividadePrincipal> atividadePrincipal = null;
	@JsonProperty("data_situacao")
	private String dataSituacao;
	@JsonProperty("nome")
	private String nome;
	@JsonProperty("uf")
	private String uf;
	@JsonProperty("qsa")
	private List<Qsa> qsa = null;
	@JsonProperty("situacao")
	private String situacao;
	@JsonProperty("bairro")
	private String bairro;
	@JsonProperty("logradouro")
	private String logradouro;
	@JsonProperty("numero")
	private String numero;
	@JsonProperty("cep")
	private String cep;
	@JsonProperty("municipio")
	private String municipio;
	@JsonProperty("porte")
	private String porte;
	@JsonProperty("abertura")
	private String abertura;
	@JsonProperty("natureza_juridica")
	private String naturezaJuridica;
	@JsonProperty("fantasia")
	private String fantasia;
	@JsonProperty("cnpj")
	private String cnpj;
	@JsonProperty("ultima_atualizacao")
	private String ultimaAtualizacao;
	@JsonProperty("status")
	private String status;
	@JsonProperty("tipo")
	private String tipo;
	@JsonProperty("complemento")
	private String complemento;
	@JsonProperty("email")
	private String email;
	@JsonProperty("telefone")
	private String telefone;
	@JsonProperty("efr")
	private String efr;
	@JsonProperty("motivo_situacao")
	private String motivoSituacao;
	@JsonProperty("situacao_especial")
	private String situacaoEspecial;
	@JsonProperty("data_situacao_especial")
	private String dataSituacaoEspecial;
	@JsonProperty("atividades_secundarias")
	private List<AtividadesSecundaria> atividadesSecundarias = null;
	@JsonProperty("capital_social")
	private String capitalSocial;


	@JsonProperty("atividade_principal")
	public List<AtividadePrincipal> getAtividadePrincipal() {
		if(atividadePrincipal == null) {
			atividadePrincipal = new ArrayList<>();
		}
	return atividadePrincipal;
	}

	@JsonProperty("atividade_principal")
	public void setAtividadePrincipal(List<AtividadePrincipal> atividadePrincipal) {
	this.atividadePrincipal = atividadePrincipal;
	}

	@JsonProperty("data_situacao")
	public String getDataSituacao() {
	return dataSituacao;
	}

	@JsonProperty("data_situacao")
	public void setDataSituacao(String dataSituacao) {
	this.dataSituacao = dataSituacao;
	}

	@JsonProperty("nome")
	public String getNome() {
	return nome;
	}

	@JsonProperty("nome")
	public void setNome(String nome) {
	this.nome = nome;
	}

	@JsonProperty("uf")
	public String getUf() {
	return uf;
	}

	@JsonProperty("uf")
	public void setUf(String uf) {
	this.uf = uf;
	}

	@JsonProperty("qsa")
	public List<Qsa> getQsa() {
		if(qsa == null) {
			qsa = new ArrayList<Qsa>();
		}
	return qsa;
	}

	@JsonProperty("qsa")
	public void setQsa(List<Qsa> qsa) {
	this.qsa = qsa;
	}

	@JsonProperty("situacao")
	public String getSituacao() {
	return situacao;
	}

	@JsonProperty("situacao")
	public void setSituacao(String situacao) {
	this.situacao = situacao;
	}

	@JsonProperty("bairro")
	public String getBairro() {
	return bairro;
	}

	@JsonProperty("bairro")
	public void setBairro(String bairro) {
	this.bairro = bairro;
	}

	@JsonProperty("logradouro")
	public String getLogradouro() {
	return logradouro;
	}

	@JsonProperty("logradouro")
	public void setLogradouro(String logradouro) {
	this.logradouro = logradouro;
	}

	@JsonProperty("numero")
	public String getNumero() {
	return numero;
	}

	@JsonProperty("numero")
	public void setNumero(String numero) {
	this.numero = numero;
	}

	@JsonProperty("cep")
	public String getCep() {
	return cep;
	}

	@JsonProperty("cep")
	public void setCep(String cep) {
	this.cep = cep;
	}

	@JsonProperty("municipio")
	public String getMunicipio() {
	return municipio;
	}

	@JsonProperty("municipio")
	public void setMunicipio(String municipio) {
	this.municipio = municipio;
	}

	@JsonProperty("porte")
	public String getPorte() {
	return porte;
	}

	@JsonProperty("porte")
	public void setPorte(String porte) {
	this.porte = porte;
	}

	@JsonProperty("abertura")
	public String getAbertura() {
	return abertura;
	}

	@JsonProperty("abertura")
	public void setAbertura(String abertura) {
	this.abertura = abertura;
	}

	@JsonProperty("natureza_juridica")
	public String getNaturezaJuridica() {
	return naturezaJuridica;
	}

	@JsonProperty("natureza_juridica")
	public void setNaturezaJuridica(String naturezaJuridica) {
	this.naturezaJuridica = naturezaJuridica;
	}

	@JsonProperty("fantasia")
	public String getFantasia() {
	return fantasia;
	}

	@JsonProperty("fantasia")
	public void setFantasia(String fantasia) {
	this.fantasia = fantasia;
	}

	@JsonProperty("cnpj")
	public String getCnpj() {
	return cnpj;
	}

	@JsonProperty("cnpj")
	public void setCnpj(String cnpj) {
	this.cnpj = cnpj;
	}

	@JsonProperty("ultima_atualizacao")
	public String getUltimaAtualizacao() {
	return ultimaAtualizacao;
	}

	@JsonProperty("ultima_atualizacao")
	public void setUltimaAtualizacao(String ultimaAtualizacao) {
	this.ultimaAtualizacao = ultimaAtualizacao;
	}

	@JsonProperty("status")
	public String getStatus() {
	return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
	this.status = status;
	}

	@JsonProperty("tipo")
	public String getTipo() {
	return tipo;
	}

	@JsonProperty("tipo")
	public void setTipo(String tipo) {
	this.tipo = tipo;
	}

	@JsonProperty("complemento")
	public String getComplemento() {
	return complemento;
	}

	@JsonProperty("complemento")
	public void setComplemento(String complemento) {
	this.complemento = complemento;
	}

	@JsonProperty("email")
	public String getEmail() {
	return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
	this.email = email;
	}

	@JsonProperty("telefone")
	public String getTelefone() {
	return telefone;
	}

	@JsonProperty("telefone")
	public void setTelefone(String telefone) {
	this.telefone = telefone;
	}

	@JsonProperty("efr")
	public String getEfr() {
	return efr;
	}

	@JsonProperty("efr")
	public void setEfr(String efr) {
	this.efr = efr;
	}

	@JsonProperty("motivo_situacao")
	public String getMotivoSituacao() {
	return motivoSituacao;
	}

	@JsonProperty("motivo_situacao")
	public void setMotivoSituacao(String motivoSituacao) {
	this.motivoSituacao = motivoSituacao;
	}

	@JsonProperty("situacao_especial")
	public String getSituacaoEspecial() {
	return situacaoEspecial;
	}

	@JsonProperty("situacao_especial")
	public void setSituacaoEspecial(String situacaoEspecial) {
	this.situacaoEspecial = situacaoEspecial;
	}

	@JsonProperty("data_situacao_especial")
	public String getDataSituacaoEspecial() {
	return dataSituacaoEspecial;
	}

	@JsonProperty("data_situacao_especial")
	public void setDataSituacaoEspecial(String dataSituacaoEspecial) {
	this.dataSituacaoEspecial = dataSituacaoEspecial;
	}

	@JsonProperty("atividades_secundarias")
	public List<AtividadesSecundaria> getAtividadesSecundarias() {
		if(atividadesSecundarias == null) {
			atividadesSecundarias = new ArrayList<>();
		}
	return atividadesSecundarias;
	}

	@JsonProperty("atividades_secundarias")
	public void setAtividadesSecundarias(List<AtividadesSecundaria> atividadesSecundarias) {
	this.atividadesSecundarias = atividadesSecundarias;
	}

	@JsonProperty("capital_social")
	public String getCapitalSocial() {
	return capitalSocial;
	}

	@JsonProperty("capital_social")
	public void setCapitalSocial(String capitalSocial) {
	this.capitalSocial = capitalSocial;
	}


}
