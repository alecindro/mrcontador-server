package br.com.mrcontador.config.liquibase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.mrcontador.config.tenant.TenantConnectionProvider;
import br.com.mrcontador.security.SecurityUtils;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

public class LiquibaseMultiTenantcy extends SpringLiquibase {

	protected Set<String> schemas;
	
	private static final String SELECT_SCHEMAS = "select schema_name from information_schema.schemata ";
	private final Logger log = LoggerFactory.getLogger(LiquibaseMultiTenantcy.class);

	@Override
	public void afterPropertiesSet() throws LiquibaseException {
		String defaultSchema = getDefaultSchema();

		for (String schema : getSchemas()) {
			setDefaultSchema(schema);
			setLiquibaseSchema(schema);
			Map<String,String> parameters = new HashMap<>();
			parameters.put("schema", schema);
			setChangeLogParameters(parameters);
			super.afterPropertiesSet();
		}
		setDefaultSchema(defaultSchema);
	}

	private Set<String> getSchemas() {
		if (this.schemas == null) {
			this.schemas = new HashSet<>();
		}
		return schemas;
	}
	
	public void updateSchema(){
		schemas = getAllSchemas(dataSource);
		schemas = schemas.stream().filter(schema -> !schema.equalsIgnoreCase(SecurityUtils.DEFAULT_TENANT)).collect(Collectors.toSet());
	}
	
	public void updateSchemaDefault() throws Exception{
		verifySchema(dataSource, SecurityUtils.DEFAULT_TENANT);
		getSchemas().add(SecurityUtils.DEFAULT_TENANT);
	}

	public String createSchema(String schema,DataSource dataSource) throws Exception {
		schema = schema.toLowerCase();
		setDataSource(dataSource);
		String defaultSchema = getDefaultSchema();
		verifySchema(dataSource, schema);
		getSchemas().add(schema);
		afterPropertiesSet();
		setDefaultSchema(defaultSchema);
		return schema;
	}

	public void dropSchema(String schema) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			String sql = "DROP schema " + schema + " CASCADE";
			ps = conn.prepareStatement(sql);
			ps.execute();
			if (!conn.getAutoCommit()) {
				conn.commit();
			}
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void verifySchema(DataSource dataSource, String schema) throws Exception {
		String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = ?";
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, schema);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				sql = "CREATE schema " + schema;
				ps = conn.prepareStatement(sql);
				ps.execute();
				if (!conn.getAutoCommit()) {
					conn.commit();
				}
			}
			ps.close();

		} catch (SQLException e) {
			throw new Exception(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	private Set<String> getAllSchemas(DataSource dataSource) {
		Connection conn;
		Statement stm = null;
		ResultSet rs = null;
		Set<String> schemas = new HashSet<String>();
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
		schemas = schemas.stream().filter(auth -> auth.startsWith(SecurityUtils.DS_PREFIX)).collect(Collectors.toSet());
		return schemas;

	}
}
