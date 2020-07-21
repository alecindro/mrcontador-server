package br.com.mrcontador.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "s3", ignoreUnknownFields = false)

public class S3Properties {

	
	private String bucketName;
	private String planoContaFolder;
	private String notaFolder;
	private String errorFolder;
	private String comprovanteFolder;
	private String extratoFolder;
	private String urlS3;
	
	
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getPlanoContaFolder() {
		return planoContaFolder;
	}
	public void setPlanoContaFolder(String planoContaFolder) {
		this.planoContaFolder = planoContaFolder;
	}
	public String getNotaFolder() {
		return notaFolder;
	}
	public void setNotaFolder(String notaFolder) {
		this.notaFolder = notaFolder;
	}
	public String getErrorFolder() {
		return errorFolder;
	}
	public void setErrorFolder(String errorFolder) {
		this.errorFolder = errorFolder;
	}
	public String getComprovanteFolder() {
		return comprovanteFolder;
	}
	public void setComprovanteFolder(String comprovanteFolder) {
		this.comprovanteFolder = comprovanteFolder;
	}
	public String getUrlS3() {
		return urlS3;
	}
	public void setUrlS3(String urlS3) {
		this.urlS3 = urlS3;
	}
	public String getExtratoFolder() {
		return extratoFolder;
	}
	public void setExtratoFolder(String extratoFolder) {
		this.extratoFolder = extratoFolder;
	}
	
}
