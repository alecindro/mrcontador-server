package br.com.mrcontador.jasper;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

public class Compilador {
	
	public static void main(String[] args) throws JRException {

		// Get currently running directory
		String currentPath = System.getProperty("user.dir");

		System.out.println("Current path is: " + currentPath);

		// Go to directory where all the reports are
		File rootDir = new File("/home/alecindro/git/mrcontador_projects/mrcontador-server/src/main/resources/danfe/nfe/DANFE_NFE_RETRATO.jrxml");

		// Get all *.jrxml files
		

			JasperCompileManager.compileReportToFile(rootDir.getAbsolutePath(),
					"/home/alecindro/git/mrcontador_projects/DANFE_NFE_RETRATO.jasper");
			System.out.println("Compiling: completed!");
		}

}
