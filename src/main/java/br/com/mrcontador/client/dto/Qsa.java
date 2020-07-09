package br.com.mrcontador.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"qual",
"nome"
})
public class Qsa {

@JsonProperty("qual")
private String qual;
@JsonProperty("nome")
private String nome;

@JsonProperty("qual")
public String getQual() {
return qual;
}

@JsonProperty("qual")
public void setQual(String qual) {
this.qual = qual;
}

@JsonProperty("nome")
public String getNome() {
return nome;
}

@JsonProperty("nome")
public void setNome(String nome) {
this.nome = nome;
}

}
