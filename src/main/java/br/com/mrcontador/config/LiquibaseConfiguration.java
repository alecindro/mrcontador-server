package br.com.mrcontador.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import br.com.mrcontador.security.SecurityUtils;
import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.liquibase.SpringLiquibaseUtil;
import liquibase.integration.spring.SpringLiquibase;

public class LiquibaseConfiguration {

	private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

	private final Environment env;

	private static final String SELECT_SCHEMAS = "select schema_name from information_schema.schemata ";
	private static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS {0}";

	public LiquibaseConfiguration(Environment env) {
		this.env = env;
	}

	
	@Bean
    public SpringLiquibase liquibase(@Qualifier("taskExecutor") Executor executor,
            @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource, LiquibaseProperties liquibaseProperties,
            ObjectProvider<DataSource> dataSource, DataSourceProperties dataSourceProperties) {

        // If you don't want Liquibase to start asynchronously, substitute by this:
        // SpringLiquibase liquibase = SpringLiquibaseUtil.createSpringLiquibase(liquibaseDataSource.getIfAvailable(), liquibaseProperties, dataSource.getIfUnique(), dataSourceProperties);
       SpringLiquibase liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(this.env, executor, liquibaseDataSource.getIfAvailable(), liquibaseProperties, dataSource.getIfUnique(), dataSourceProperties);
       liquibase.setChangeLog("classpath:config/liquibase/master.xml");
       /*  liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE))) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            log.debug("Configuring Liquibase");
        }*/
        for (String schema : getAllSchemas(dataSource.getIfAvailable())) {
			updateSchema(schema, liquibase, liquibaseProperties);
        }
        return liquibase;
    }

	private boolean updateSchema(String schema, SpringLiquibase liquibase, LiquibaseProperties liquibaseProperties) {
		liquibase.setChangeLog("classpath:config/liquibase/master.xml");
		liquibase.setContexts(liquibaseProperties.getContexts());
		liquibase.setDefaultSchema(schema);
		liquibase.setLiquibaseSchema(schema);
		liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
		liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
		liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
		liquibase.setDropFirst(liquibaseProperties.isDropFirst());
		liquibase.setLabels(liquibaseProperties.getLabels());
		liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
		liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
		liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
		if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE))) {
			liquibase.setShouldRun(false);
		} else {
			liquibase.setShouldRun(liquibaseProperties.isEnabled());
			log.debug("Configuring Liquibase");
		}
		return true;
	}
	
	public boolean createSchema(String schema, DataSource dataSource, LiquibaseProperties liquibaseProperties) {
		SpringLiquibase liquibase = new SpringLiquibase();
		Connection conn;
		Statement stm = null;
		try {
			String createSchema = MessageFormat.format(CREATE_SCHEMA, schema); 
			conn = dataSource.getConnection();
			stm = conn.createStatement();
			stm.execute(createSchema);
			return updateSchema(schema, liquibase, liquibaseProperties);
		} catch (Exception e) {
			log.error("Erro ao cria esquema: "+schema, e);
		}
		return false;
	}

	private List<String> getAllSchemas(DataSource dataSource) {
		Connection conn;
		Statement stm = null;
		ResultSet rs = null;
		List<String> schemas = new ArrayList<String>();
		if (dataSource != null) {
			try {
				conn = dataSource.getConnection();
				stm = conn.createStatement();
				rs = stm.executeQuery(SELECT_SCHEMAS);
				while (rs.next()) {
					schemas.add(rs.getString("schema_name"));
				}
			} catch (Exception e) {
				log.error("Erro ao listar esquemas", e);
			} finally {
				try {
					stm.close();
					rs.close();
				} catch (SQLException e) {
					log.error("Erro fechar resultSet GetAllSchemas", e);
				}

			}
		}
		schemas = schemas.stream().filter(auth -> auth.startsWith(SecurityUtils.DS_PREFIX)).collect(Collectors.toList());
		return schemas;

	}

}
