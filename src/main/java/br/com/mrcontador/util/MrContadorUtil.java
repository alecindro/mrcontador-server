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

}
