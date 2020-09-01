package br.com.mrcontador.service;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.util.MrContadorUtil;

@Service
public class InteligentFunction {
	
	@Autowired
	private DataSource dataSource;
	
	private static final String SQL_FUNCTION = "call {0}.processa_inteligente(?,?,?,?)";

	
	public void callInteligent(Long parceiroID, Long agenciaBancariaID, String periodo, String ds_tenant) throws SQLException {
		String sql = "{"+MessageFormat.format(SQL_FUNCTION, ds_tenant)+"}";
		LocalDate begin = MrContadorUtil.initalForPeriodo(periodo);
		LocalDate end = MrContadorUtil.lastForPeriodo(periodo);
		CallableStatement proc = dataSource.getConnection().prepareCall(sql);
		proc.setLong(1, parceiroID);
		proc.setLong(2, agenciaBancariaID);
		proc.setDate(3, Date.valueOf(begin));
		proc.setDate(4, Date.valueOf(end));
		proc.execute();
		proc.close();
		dataSource.getConnection().close();
		
	}
}
