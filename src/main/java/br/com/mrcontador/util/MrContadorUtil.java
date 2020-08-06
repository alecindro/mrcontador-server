package br.com.mrcontador.util;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import br.com.mrcontador.file.TipoDocumento;

public class MrContadorUtil {

	public static String onlyNumbers(String cnpj) {
		if (cnpj != null) {
			return cnpj.replaceAll("\\D", "");
		}
		return cnpj;
	}
	
	public static String onlyMoney(String value) {
		if (value != null) {
			value = value.replace("R$", "").trim();
			value = value.replace(".", "");
			return value.replace(",", ".");
		}
		return value;
	}
	
	
	public static String getFolder(String contador,String parceiro, String s3Folder) {
		return MessageFormat.format(s3Folder,contador,parceiro);
	}
	
	public static String getS3Url(String contador,String parceiro, String s3Folder,String fileName, String urlS3) {
		return urlS3+"/"+getFolder(contador, parceiro, s3Folder)+"/"+fileName;
	}
	
	public static String getS3Url(String folder, String urlS3, String fileName) {
		return urlS3+"/"+folder+"/"+fileName;
	}
	
	public static String genFileName(TipoDocumento tipo, String contentType) {
		return tipo.getIndexName()+"_"+getDateFileName()+"."+getExtensionFile(contentType);
	}
	public static String genErroFileName(String ds, String contentType) {
		return ds+"_"+getDateFileName().toString()+"."+getExtensionFile(contentType);
	}
	
	public static String getExtensionFile(String contentType) {
		return com.google.common.net.MediaType.parse(contentType).subtype();
	}
	
	private static String getDateFileName() {
		LocalDateTime z = LocalDateTime.now();
		return z.getYear()+"_"+z.getMonthValue()+"_"+z.getDayOfMonth()+"_"+z.getHour()+"_"+z.getMinute()+"_"+z.getSecond();
	}
	
	public static String removeZerosFromInital(String value) {
		String result = value;
		while (startWithsZero(result)) {
			result = result.substring(1, result.length());
		}
		return result;
	}
	
	private static boolean startWithsZero(String value) {
		return value.startsWith("0");
	}
	
	public static boolean compareWithoutDigit(String pattern, String value) {
		pattern = org.apache.commons.lang3.StringUtils.deleteWhitespace(pattern);
		value = org.apache.commons.lang3.StringUtils.deleteWhitespace(value);
		value = removeZerosFromInital(value.trim());
		pattern = removeZerosFromInital(pattern.trim());
		if(value.equalsIgnoreCase(pattern)) {
			return true;
		}
		if(value.startsWith(pattern)) {
			return true;
		}
		return false;
	}

}
