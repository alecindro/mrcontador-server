package br.com.mrcontador.util;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.apache.commons.lang3.StringUtils;

import br.com.mrcontador.file.TipoDocumento;

public class MrContadorUtil {

	public static String onlyNumbers(String value) {
		if (value != null) {
			return value.replaceAll("\\D", "");
		}
		return value;
	}
	
	public static boolean only9(String value) {
		if (value != null) {
			value = onlyNumbers(value);
			return StringUtils.containsOnly(value, "9");
		}
		return false;
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
	public static String genFileName(TipoDocumento tipo,Long idParceiro, String contentType, int page) {
		return tipo.getTipoDoc()+"_parc_"+idParceiro+"_data_"+getDateFileName()+"_page_"+"."+getExtensionFile(contentType);
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
	
	public static String removeZerosFromEnd(String value) {
		String result = value;
		while (endsWithsZero(result)) {
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
	private static boolean endsWithsZero(String value) {
		return value.endsWith("0");
	}
	
	private static boolean startWithsZero(String value) {
		return value.startsWith("0");
	}
	
	public static boolean compareWithoutDigit(String pattern, String value) {
		if(pattern == null) {
			return true;
		}
		pattern = org.apache.commons.lang3.StringUtils.deleteWhitespace(pattern);
		value = org.apache.commons.lang3.StringUtils.deleteWhitespace(value);
		value = removeZerosFromInital(value.trim());
		pattern = removeZerosFromInital(pattern.trim());
		pattern = onlyNumbers(pattern);
		value = onlyNumbers(value);
		if(value.equalsIgnoreCase(pattern)) {
			return true;
		}
		if(value.startsWith(pattern)) {
			return true;
		}
		return false;
	}
	
	public static LocalDate initalForPeriodo(String periodo) {
		LocalDate date = LocalDate.now();
		String mes = periodo.substring(0, periodo.length()-4);
		String year = StringUtils.substringAfter(periodo, mes);
		date.withMonth(Integer.valueOf(mes));
		date.withYear(Integer.valueOf(year));
		date.withDayOfMonth(1);
		return date;
	}
	
	public static LocalDate lastForPeriodo(String periodo) {
		LocalDate date = LocalDate.now();
		String mes = periodo.substring(0, periodo.length()-4);
		String year = StringUtils.substringAfter(periodo, mes);
		date.withMonth(Integer.valueOf(mes));
		date.withYear(Integer.valueOf(year));
		date.with(TemporalAdjusters.lastDayOfMonth());
		return date;
	}

}
