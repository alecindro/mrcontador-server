package br.com.mrcontador.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mrcontador.domain.Authority;
import br.com.mrcontador.domain.Authority_;
import br.com.mrcontador.domain.User;
import br.com.mrcontador.domain.User_;
import br.com.mrcontador.repository.UserRepository;
import br.com.mrcontador.service.dto.UserCriteria;
import io.github.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class UserQueryService extends QueryService<User>{

	
	 private final Logger log = LoggerFactory.getLogger(UserQueryService.class);
	 
	 private final UserRepository userRepository;
	 
	 public UserQueryService(UserRepository userRepository) {
		 this.userRepository = userRepository;
	 }
	 
	    @Transactional(readOnly = true)
	    public List<User> findByCriteriaNotAuthority(UserCriteria criteria) {
	        log.debug("find by criteria : {}", criteria);
	        final Specification<User> specification = createSpecification(criteria,Specification.where(null));
	        return userRepository.findAll(specification);
	    }
	    protected Specification<User> createSpecification(UserCriteria criteria, Specification<User> specification) {
	        if (criteria != null) {
	            if (criteria.getId() != null) {
	                specification = specification.and(buildRangeSpecification(criteria.getId(), User_.id));
	            }
	            if (criteria.getActivated() != null) {
	                specification = specification.and(buildSpecification(criteria.getActivated(), User_.activated));
	            }
	            if (criteria.getDatasource() != null) {
	            	specification = specification.and(buildSpecification(criteria.getDatasource(),User_.datasource));
	            }
	            if (criteria.getEmail() != null) {
	                specification = specification.and(buildStringSpecification(criteria.getEmail(), User_.email));
	            }
	            if (criteria.getFirstName() != null) {
	                specification = specification.and(buildStringSpecification(criteria.getFirstName(), User_.firstName));
	            }
	            if (criteria.getFuncao() != null) {
	                specification = specification.and(buildStringSpecification(criteria.getFuncao(), User_.funcao));
	            }
	            if (criteria.getLastName() != null) {
	                specification = specification.and(buildStringSpecification(criteria.getLastName(), User_.lastName));
	            }
	            if (criteria.getLogin() != null) {
	                specification = specification.and(buildStringSpecification(criteria.getLogin(), User_.login));
	            }
	            if (criteria.getAuthorityName() != null && criteria.getAuthorityName().getNotEquals() != null) {
	                specification = specification.and(userNotAuthority(criteria.getAuthorityName().getNotEquals()));
	            }
	        }
	        return specification;
	    }	
	    
	    public static Specification<User> userNotAuthority(final String expression){
	    	  return new Specification<User>(){
	    	  
				private static final long serialVersionUID = 1L;

				public Predicate toPredicate(    Root<User> root,    CriteriaQuery<?> query,    CriteriaBuilder cb){
	    	    	SetJoin<User, Authority>  join = root.join(User_.authorities);
	    	    	return cb.notEqual(join.get(Authority_.name), expression);
	    	    }
	    	  }
	    	;
	    	}
}
