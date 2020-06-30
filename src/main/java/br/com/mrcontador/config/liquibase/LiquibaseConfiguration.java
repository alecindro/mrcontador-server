package br.com.mrcontador.config.liquibase;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import io.github.jhipster.config.JHipsterConstants;
import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class LiquibaseConfiguration {
	
	@Autowired
	private Environment env;

	
	
	
	private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

	public LiquibaseConfiguration() {
	}
	
	@Bean @Primary
	public SpringLiquibase liquibase(@Lazy @Qualifier("dataSource") DataSource dataSource, LiquibaseProperties liquibaseProperties) {
		LiquibaseMultiTenantcy liquibase = new LiquibaseMultiTenantcy();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:config/liquibase/master.xml");
		liquibase.setContexts(liquibaseProperties.getContexts());
		liquibase.setDropFirst(liquibaseProperties.isDropFirst());
		liquibase.setShouldRun(false);

		 if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE))) {
			liquibase.setShouldRun(false);
		} else {
			liquibase.setShouldRun(liquibaseProperties.isEnabled());
			log.debug("Configuring Liquibase");
			try {
				liquibase.updateSchema();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(),e);
			}
			
		}
		return liquibase;
	}


	@Bean(name = "LiquibaseMrContador")
	public SpringLiquibase processSchemaDefault(@Lazy @Qualifier("dataSource") DataSource dataSource, LiquibaseProperties liquibaseProperties){
		LiquibaseMultiTenantcy liquibase = new LiquibaseMultiTenantcy();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:config/liquibase/master_mrcontador.xml");
		liquibase.setContexts(liquibaseProperties.getContexts());
		liquibase.setDropFirst(liquibaseProperties.isDropFirst());
		liquibase.setShouldRun(liquibaseProperties.isEnabled());
		log.debug("Configuring Liquibase");
		try {
			liquibase.updateSchemaDefault();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		return liquibase;
	}
	
	
	
	 
}
