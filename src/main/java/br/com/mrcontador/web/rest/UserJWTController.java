package br.com.mrcontador.web.rest;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.security.jwt.JWTFilter;
import br.com.mrcontador.security.jwt.TokenProvider;
import br.com.mrcontador.service.ContadorService;
import br.com.mrcontador.service.dto.ContadorDTO;
import br.com.mrcontador.web.rest.vm.LoginVM;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ContadorService contadorService;
    
    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, ContadorService contadorService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.contadorService = contadorService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        String tenantUuid = SecurityUtils.getTenantHeader(authentication);
        String sistema = getSistema(tenantUuid);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt,tenantUuid,sistema), httpHeaders, HttpStatus.OK);
    }
    
    private String getSistema(String tenantUuid) {
    	Optional<ContadorDTO> contador = contadorService.findOneByDatasource(tenantUuid);
    	if(contador.isPresent()) {
    		return contador.get().getSistema();
    	}
    	return "";
    }
    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;
        private String tenantUuid;
        private String sistema;

        JWTToken(String idToken,String tenantUuid, String sistema) {
            this.idToken = idToken;
            this.tenantUuid = tenantUuid;
            this.sistema = sistema;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

		@JsonProperty("tenant_uuid")
        String getTenantUuid() {
			return tenantUuid;
		}

		void setTenantUuid(String tenantUuid) {
			this.tenantUuid = tenantUuid;
		}

		@JsonProperty("sistema")
		public String getSistema() {
			return sistema;
		}

		public void setSistema(String sistema) {
			this.sistema = sistema;
		}
		
		
        
    }
}
