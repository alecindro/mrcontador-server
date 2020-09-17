package br.com.mrcontador.config.tenant;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.mrcontador.security.SecurityUtils;

@Component
public class TenantInterceptor extends HandlerInterceptorAdapter{

	 Logger logger = LoggerFactory.getLogger(getClass());
	 public static final String TENANT_HEADER = "tenant-uuid";

	 @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 String tenantUuid = request.getHeader(TENANT_HEADER);
		 if(tenantUuid == null) {
			 TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			 return super.preHandle(request, response, handler);
		 }
		 List<String> roles = SecurityUtils.getAuthorities();
		 boolean permit = roles.stream().anyMatch(role -> role.equalsIgnoreCase(tenantUuid));
		 if(!permit) {
			 throw new RuntimeException("not.valide");
		 }
		 for(String role : roles) {
			 logger.debug("Role: {}",role);
			 
		 }
		 
		 logger.debug("Set TenantContext: {}",tenantUuid);
	        TenantContext.setTenantSchema(tenantUuid);
		return super.preHandle(request, response, handler);
	}
	 
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("Clear TenantContext: {}",TenantContext.getTenantSchema());
        TenantContext.setTenantSchema(null);
		super.postHandle(request, response, handler, modelAndView);
	} 
}
