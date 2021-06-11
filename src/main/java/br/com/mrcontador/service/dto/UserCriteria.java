package br.com.mrcontador.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

public class UserCriteria implements Serializable, Criteria{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LongFilter id;
    private StringFilter login;
    private StringFilter firstName;
    private StringFilter lastName;
    private StringFilter email;
    private BooleanFilter activated;
    private StringFilter funcao;
    private StringFilter datasource;
    private StringFilter authorityName;
    
    public UserCriteria() {
    	
    }
    public UserCriteria(UserCriteria other) {
    	this.id = other.id;
    	this.login = other.login;
    	this.firstName = other.firstName;
    	this.lastName = other.lastName;
    	this.email = other.email;
    	this.activated = other.activated;
    	this.funcao = other.funcao;
    	this.datasource = other.datasource;
    	this.authorityName = other.authorityName;
    }
    

	public LongFilter getId() {
		return id;
	}
	public void setId(LongFilter id) {
		this.id = id;
	}
	public StringFilter getLogin() {
		return login;
	}
	public void setLogin(StringFilter login) {
		this.login = login;
	}
	public StringFilter getFirstName() {
		return firstName;
	}
	public void setFirstName(StringFilter firstName) {
		this.firstName = firstName;
	}
	public StringFilter getLastName() {
		return lastName;
	}
	public void setLastName(StringFilter lastName) {
		this.lastName = lastName;
	}
	public StringFilter getEmail() {
		return email;
	}
	public void setEmail(StringFilter email) {
		this.email = email;
	}
	public BooleanFilter getActivated() {
		return activated;
	}
	public void setActivated(BooleanFilter activated) {
		this.activated = activated;
	}
	public StringFilter getFuncao() {
		return funcao;
	}
	public void setFuncao(StringFilter funcao) {
		this.funcao = funcao;
	}
	public StringFilter getDatasource() {
		return datasource;
	}
	public void setDatasource(StringFilter datasource) {
		this.datasource = datasource;
	}
	public StringFilter getAuthorityName() {
		return authorityName;
	}
	public void setAuthorityName(StringFilter authorityName) {
		this.authorityName = authorityName;
	}
	@Override
	public Criteria copy() {
		return new UserCriteria(this);
	}
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) {
	            return true;
	        }
	        if (o == null || getClass() != o.getClass()) {
	            return false;
	        }
	        final UserCriteria that = (UserCriteria) o;
	        return
	            Objects.equals(id, that.id) &&
	            Objects.equals(activated, that.activated) &&
	            Objects.equals(datasource, that.datasource) &&
	            Objects.equals(email, that.email) &&
	            Objects.equals(firstName, that.firstName) &&
	            Objects.equals(funcao, that.funcao) &&
	            Objects.equals(lastName, that.lastName) &&
	            Objects.equals(login, that.login) &&
	            Objects.equals(authorityName, that.authorityName);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(
	        id,
	        activated,
	        datasource,
	        email,
	        firstName,
	        funcao,
	        lastName,
	        login,
	        authorityName
	        );
	    }
		@Override
		public String toString() {
			return "UserCriteria [id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName
					+ ", email=" + email + ", activated=" + activated + ", funcao=" + funcao + ", datasource="
					+ datasource + "]";
		}


}
