package br.com.mrcontador.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import br.com.mrcontador.erros.MrContadorException;

public class SpringUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringUtil.class);

	@SuppressWarnings("deprecation")
	public static  <T> T instantiate(final String className, final Class<T> type) {
		try {
			return type.cast(Class.forName(className).newInstance());
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
			throw new MrContadorException(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			throw new MrContadorException(e.getMessage(),e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			throw new MrContadorException(e.getMessage(),e);
		}
	}
	
	public static <T> T autowired(final String className, final Class<T> type, BeanFactory beanFactory) {
		return beanFactory.getBean(className,type);
	}
}
